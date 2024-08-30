package com.nova.star.web.rest;

import static com.nova.star.domain.IdeaHistoryAsserts.*;
import static com.nova.star.web.rest.TestUtil.createUpdateProxyForBean;
import static com.nova.star.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nova.star.IntegrationTest;
import com.nova.star.domain.IdeaHistory;
import com.nova.star.domain.enumeration.RewardType;
import com.nova.star.repository.IdeaHistoryRepository;
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
 * Integration tests for the {@link IdeaHistoryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class IdeaHistoryResourceIT {

    private static final String DEFAULT_ACTION = "AAAAAAAAAA";
    private static final String UPDATED_ACTION = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_ACTION_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_ACTION_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final RewardType DEFAULT_REWARD_TYPE = RewardType.BRONZE;
    private static final RewardType UPDATED_REWARD_TYPE = RewardType.ARGENT;

    private static final String DEFAULT_LIKES = "AAAAAAAAAA";
    private static final String UPDATED_LIKES = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/idea-histories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private IdeaHistoryRepository ideaHistoryRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restIdeaHistoryMockMvc;

    private IdeaHistory ideaHistory;

    private IdeaHistory insertedIdeaHistory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static IdeaHistory createEntity() {
        return new IdeaHistory()
            .action(DEFAULT_ACTION)
            .actionDate(DEFAULT_ACTION_DATE)
            .description(DEFAULT_DESCRIPTION)
            .rewardType(DEFAULT_REWARD_TYPE)
            .likes(DEFAULT_LIKES);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static IdeaHistory createUpdatedEntity() {
        return new IdeaHistory()
            .action(UPDATED_ACTION)
            .actionDate(UPDATED_ACTION_DATE)
            .description(UPDATED_DESCRIPTION)
            .rewardType(UPDATED_REWARD_TYPE)
            .likes(UPDATED_LIKES);
    }

    @BeforeEach
    public void initTest() {
        ideaHistory = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedIdeaHistory != null) {
            ideaHistoryRepository.delete(insertedIdeaHistory);
            insertedIdeaHistory = null;
        }
    }

    @Test
    @Transactional
    void createIdeaHistory() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the IdeaHistory
        var returnedIdeaHistory = om.readValue(
            restIdeaHistoryMockMvc
                .perform(
                    post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ideaHistory))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            IdeaHistory.class
        );

        // Validate the IdeaHistory in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertIdeaHistoryUpdatableFieldsEquals(returnedIdeaHistory, getPersistedIdeaHistory(returnedIdeaHistory));

        insertedIdeaHistory = returnedIdeaHistory;
    }

    @Test
    @Transactional
    void createIdeaHistoryWithExistingId() throws Exception {
        // Create the IdeaHistory with an existing ID
        ideaHistory.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restIdeaHistoryMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ideaHistory)))
            .andExpect(status().isBadRequest());

        // Validate the IdeaHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkActionIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        ideaHistory.setAction(null);

        // Create the IdeaHistory, which fails.

        restIdeaHistoryMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ideaHistory)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkActionDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        ideaHistory.setActionDate(null);

        // Create the IdeaHistory, which fails.

        restIdeaHistoryMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ideaHistory)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllIdeaHistories() throws Exception {
        // Initialize the database
        insertedIdeaHistory = ideaHistoryRepository.saveAndFlush(ideaHistory);

        // Get all the ideaHistoryList
        restIdeaHistoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ideaHistory.getId().intValue())))
            .andExpect(jsonPath("$.[*].action").value(hasItem(DEFAULT_ACTION)))
            .andExpect(jsonPath("$.[*].actionDate").value(hasItem(sameInstant(DEFAULT_ACTION_DATE))))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].rewardType").value(hasItem(DEFAULT_REWARD_TYPE.toString())))
            .andExpect(jsonPath("$.[*].likes").value(hasItem(DEFAULT_LIKES)));
    }

    @Test
    @Transactional
    void getIdeaHistory() throws Exception {
        // Initialize the database
        insertedIdeaHistory = ideaHistoryRepository.saveAndFlush(ideaHistory);

        // Get the ideaHistory
        restIdeaHistoryMockMvc
            .perform(get(ENTITY_API_URL_ID, ideaHistory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ideaHistory.getId().intValue()))
            .andExpect(jsonPath("$.action").value(DEFAULT_ACTION))
            .andExpect(jsonPath("$.actionDate").value(sameInstant(DEFAULT_ACTION_DATE)))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.rewardType").value(DEFAULT_REWARD_TYPE.toString()))
            .andExpect(jsonPath("$.likes").value(DEFAULT_LIKES));
    }

    @Test
    @Transactional
    void getNonExistingIdeaHistory() throws Exception {
        // Get the ideaHistory
        restIdeaHistoryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingIdeaHistory() throws Exception {
        // Initialize the database
        insertedIdeaHistory = ideaHistoryRepository.saveAndFlush(ideaHistory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the ideaHistory
        IdeaHistory updatedIdeaHistory = ideaHistoryRepository.findById(ideaHistory.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedIdeaHistory are not directly saved in db
        em.detach(updatedIdeaHistory);
        updatedIdeaHistory
            .action(UPDATED_ACTION)
            .actionDate(UPDATED_ACTION_DATE)
            .description(UPDATED_DESCRIPTION)
            .rewardType(UPDATED_REWARD_TYPE)
            .likes(UPDATED_LIKES);

        restIdeaHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedIdeaHistory.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedIdeaHistory))
            )
            .andExpect(status().isOk());

        // Validate the IdeaHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedIdeaHistoryToMatchAllProperties(updatedIdeaHistory);
    }

    @Test
    @Transactional
    void putNonExistingIdeaHistory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ideaHistory.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIdeaHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ideaHistory.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(ideaHistory))
            )
            .andExpect(status().isBadRequest());

        // Validate the IdeaHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchIdeaHistory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ideaHistory.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIdeaHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(ideaHistory))
            )
            .andExpect(status().isBadRequest());

        // Validate the IdeaHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamIdeaHistory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ideaHistory.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIdeaHistoryMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ideaHistory)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the IdeaHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateIdeaHistoryWithPatch() throws Exception {
        // Initialize the database
        insertedIdeaHistory = ideaHistoryRepository.saveAndFlush(ideaHistory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the ideaHistory using partial update
        IdeaHistory partialUpdatedIdeaHistory = new IdeaHistory();
        partialUpdatedIdeaHistory.setId(ideaHistory.getId());

        partialUpdatedIdeaHistory.actionDate(UPDATED_ACTION_DATE);

        restIdeaHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIdeaHistory.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedIdeaHistory))
            )
            .andExpect(status().isOk());

        // Validate the IdeaHistory in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertIdeaHistoryUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedIdeaHistory, ideaHistory),
            getPersistedIdeaHistory(ideaHistory)
        );
    }

    @Test
    @Transactional
    void fullUpdateIdeaHistoryWithPatch() throws Exception {
        // Initialize the database
        insertedIdeaHistory = ideaHistoryRepository.saveAndFlush(ideaHistory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the ideaHistory using partial update
        IdeaHistory partialUpdatedIdeaHistory = new IdeaHistory();
        partialUpdatedIdeaHistory.setId(ideaHistory.getId());

        partialUpdatedIdeaHistory
            .action(UPDATED_ACTION)
            .actionDate(UPDATED_ACTION_DATE)
            .description(UPDATED_DESCRIPTION)
            .rewardType(UPDATED_REWARD_TYPE)
            .likes(UPDATED_LIKES);

        restIdeaHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIdeaHistory.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedIdeaHistory))
            )
            .andExpect(status().isOk());

        // Validate the IdeaHistory in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertIdeaHistoryUpdatableFieldsEquals(partialUpdatedIdeaHistory, getPersistedIdeaHistory(partialUpdatedIdeaHistory));
    }

    @Test
    @Transactional
    void patchNonExistingIdeaHistory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ideaHistory.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIdeaHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, ideaHistory.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(ideaHistory))
            )
            .andExpect(status().isBadRequest());

        // Validate the IdeaHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchIdeaHistory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ideaHistory.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIdeaHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(ideaHistory))
            )
            .andExpect(status().isBadRequest());

        // Validate the IdeaHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamIdeaHistory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ideaHistory.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIdeaHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(ideaHistory))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the IdeaHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteIdeaHistory() throws Exception {
        // Initialize the database
        insertedIdeaHistory = ideaHistoryRepository.saveAndFlush(ideaHistory);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the ideaHistory
        restIdeaHistoryMockMvc
            .perform(delete(ENTITY_API_URL_ID, ideaHistory.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return ideaHistoryRepository.count();
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

    protected IdeaHistory getPersistedIdeaHistory(IdeaHistory ideaHistory) {
        return ideaHistoryRepository.findById(ideaHistory.getId()).orElseThrow();
    }

    protected void assertPersistedIdeaHistoryToMatchAllProperties(IdeaHistory expectedIdeaHistory) {
        assertIdeaHistoryAllPropertiesEquals(expectedIdeaHistory, getPersistedIdeaHistory(expectedIdeaHistory));
    }

    protected void assertPersistedIdeaHistoryToMatchUpdatableProperties(IdeaHistory expectedIdeaHistory) {
        assertIdeaHistoryAllUpdatablePropertiesEquals(expectedIdeaHistory, getPersistedIdeaHistory(expectedIdeaHistory));
    }
}
