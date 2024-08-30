package com.nova.star.web.rest;

import static com.nova.star.domain.LikeHistoryAsserts.*;
import static com.nova.star.web.rest.TestUtil.createUpdateProxyForBean;
import static com.nova.star.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nova.star.IntegrationTest;
import com.nova.star.domain.LikeHistory;
import com.nova.star.repository.LikeHistoryRepository;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link LikeHistoryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class LikeHistoryResourceIT {

    private static final String DEFAULT_ACTION = "AAAAAAAAAA";
    private static final String UPDATED_ACTION = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_ACTION_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_ACTION_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_OLD_LIKES = "AAAAAAAAAA";
    private static final String UPDATED_OLD_LIKES = "BBBBBBBBBB";

    private static final String DEFAULT_NEW_LIKES = "AAAAAAAAAA";
    private static final String UPDATED_NEW_LIKES = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/like-histories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private LikeHistoryRepository likeHistoryRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLikeHistoryMockMvc;

    private LikeHistory likeHistory;

    private LikeHistory insertedLikeHistory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LikeHistory createEntity() {
        return new LikeHistory()
            .action(DEFAULT_ACTION)
            .actionDate(DEFAULT_ACTION_DATE)
            .oldLikes(DEFAULT_OLD_LIKES)
            .newLikes(DEFAULT_NEW_LIKES);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LikeHistory createUpdatedEntity() {
        return new LikeHistory()
            .action(UPDATED_ACTION)
            .actionDate(UPDATED_ACTION_DATE)
            .oldLikes(UPDATED_OLD_LIKES)
            .newLikes(UPDATED_NEW_LIKES);
    }

    @BeforeEach
    public void initTest() {
        likeHistory = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedLikeHistory != null) {
            likeHistoryRepository.delete(insertedLikeHistory);
            insertedLikeHistory = null;
        }
    }

    @Test
    @Transactional
    void createLikeHistory() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the LikeHistory
        var returnedLikeHistory = om.readValue(
            restLikeHistoryMockMvc
                .perform(
                    post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(likeHistory))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            LikeHistory.class
        );

        // Validate the LikeHistory in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertLikeHistoryUpdatableFieldsEquals(returnedLikeHistory, getPersistedLikeHistory(returnedLikeHistory));

        insertedLikeHistory = returnedLikeHistory;
    }

    @Test
    @Transactional
    void createLikeHistoryWithExistingId() throws Exception {
        // Create the LikeHistory with an existing ID
        likeHistory.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLikeHistoryMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(likeHistory)))
            .andExpect(status().isBadRequest());

        // Validate the LikeHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkActionIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        likeHistory.setAction(null);

        // Create the LikeHistory, which fails.

        restLikeHistoryMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(likeHistory)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkActionDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        likeHistory.setActionDate(null);

        // Create the LikeHistory, which fails.

        restLikeHistoryMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(likeHistory)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllLikeHistories() throws Exception {
        // Initialize the database
        insertedLikeHistory = likeHistoryRepository.saveAndFlush(likeHistory);

        // Get all the likeHistoryList
        restLikeHistoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(likeHistory.getId().intValue())))
            .andExpect(jsonPath("$.[*].action").value(hasItem(DEFAULT_ACTION)))
            .andExpect(jsonPath("$.[*].actionDate").value(hasItem(sameInstant(DEFAULT_ACTION_DATE))))
            .andExpect(jsonPath("$.[*].oldLikes").value(hasItem(DEFAULT_OLD_LIKES)))
            .andExpect(jsonPath("$.[*].newLikes").value(hasItem(DEFAULT_NEW_LIKES)));
    }

    @Test
    @Transactional
    void getLikeHistory() throws Exception {
        // Initialize the database
        insertedLikeHistory = likeHistoryRepository.saveAndFlush(likeHistory);

        // Get the likeHistory
        restLikeHistoryMockMvc
            .perform(get(ENTITY_API_URL_ID, likeHistory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(likeHistory.getId().intValue()))
            .andExpect(jsonPath("$.action").value(DEFAULT_ACTION))
            .andExpect(jsonPath("$.actionDate").value(sameInstant(DEFAULT_ACTION_DATE)))
            .andExpect(jsonPath("$.oldLikes").value(DEFAULT_OLD_LIKES))
            .andExpect(jsonPath("$.newLikes").value(DEFAULT_NEW_LIKES));
    }

    @Test
    @Transactional
    void getNonExistingLikeHistory() throws Exception {
        // Get the likeHistory
        restLikeHistoryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingLikeHistory() throws Exception {
        // Initialize the database
        insertedLikeHistory = likeHistoryRepository.saveAndFlush(likeHistory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the likeHistory
        LikeHistory updatedLikeHistory = likeHistoryRepository.findById(likeHistory.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedLikeHistory are not directly saved in db
        em.detach(updatedLikeHistory);
        updatedLikeHistory.action(UPDATED_ACTION).actionDate(UPDATED_ACTION_DATE).oldLikes(UPDATED_OLD_LIKES).newLikes(UPDATED_NEW_LIKES);

        restLikeHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedLikeHistory.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedLikeHistory))
            )
            .andExpect(status().isOk());

        // Validate the LikeHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedLikeHistoryToMatchAllProperties(updatedLikeHistory);
    }

    @Test
    @Transactional
    void putNonExistingLikeHistory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        likeHistory.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLikeHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, likeHistory.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(likeHistory))
            )
            .andExpect(status().isBadRequest());

        // Validate the LikeHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLikeHistory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        likeHistory.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLikeHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(likeHistory))
            )
            .andExpect(status().isBadRequest());

        // Validate the LikeHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLikeHistory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        likeHistory.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLikeHistoryMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(likeHistory)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the LikeHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLikeHistoryWithPatch() throws Exception {
        // Initialize the database
        insertedLikeHistory = likeHistoryRepository.saveAndFlush(likeHistory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the likeHistory using partial update
        LikeHistory partialUpdatedLikeHistory = new LikeHistory();
        partialUpdatedLikeHistory.setId(likeHistory.getId());

        partialUpdatedLikeHistory
            .action(UPDATED_ACTION)
            .actionDate(UPDATED_ACTION_DATE)
            .oldLikes(UPDATED_OLD_LIKES)
            .newLikes(UPDATED_NEW_LIKES);

        restLikeHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLikeHistory.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedLikeHistory))
            )
            .andExpect(status().isOk());

        // Validate the LikeHistory in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertLikeHistoryUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedLikeHistory, likeHistory),
            getPersistedLikeHistory(likeHistory)
        );
    }

    @Test
    @Transactional
    void fullUpdateLikeHistoryWithPatch() throws Exception {
        // Initialize the database
        insertedLikeHistory = likeHistoryRepository.saveAndFlush(likeHistory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the likeHistory using partial update
        LikeHistory partialUpdatedLikeHistory = new LikeHistory();
        partialUpdatedLikeHistory.setId(likeHistory.getId());

        partialUpdatedLikeHistory
            .action(UPDATED_ACTION)
            .actionDate(UPDATED_ACTION_DATE)
            .oldLikes(UPDATED_OLD_LIKES)
            .newLikes(UPDATED_NEW_LIKES);

        restLikeHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLikeHistory.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedLikeHistory))
            )
            .andExpect(status().isOk());

        // Validate the LikeHistory in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertLikeHistoryUpdatableFieldsEquals(partialUpdatedLikeHistory, getPersistedLikeHistory(partialUpdatedLikeHistory));
    }

    @Test
    @Transactional
    void patchNonExistingLikeHistory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        likeHistory.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLikeHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, likeHistory.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(likeHistory))
            )
            .andExpect(status().isBadRequest());

        // Validate the LikeHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLikeHistory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        likeHistory.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLikeHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(likeHistory))
            )
            .andExpect(status().isBadRequest());

        // Validate the LikeHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLikeHistory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        likeHistory.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLikeHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(likeHistory))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LikeHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLikeHistory() throws Exception {
        // Initialize the database
        insertedLikeHistory = likeHistoryRepository.saveAndFlush(likeHistory);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the likeHistory
        restLikeHistoryMockMvc
            .perform(delete(ENTITY_API_URL_ID, likeHistory.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return likeHistoryRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected LikeHistory getPersistedLikeHistory(LikeHistory likeHistory) {
        return likeHistoryRepository.findById(likeHistory.getId()).orElseThrow();
    }

    protected void assertPersistedLikeHistoryToMatchAllProperties(LikeHistory expectedLikeHistory) {
        assertLikeHistoryAllPropertiesEquals(expectedLikeHistory, getPersistedLikeHistory(expectedLikeHistory));
    }

    protected void assertPersistedLikeHistoryToMatchUpdatableProperties(LikeHistory expectedLikeHistory) {
        assertLikeHistoryAllUpdatablePropertiesEquals(expectedLikeHistory, getPersistedLikeHistory(expectedLikeHistory));
    }
}
