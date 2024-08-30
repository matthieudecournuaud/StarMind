package com.nova.star.web.rest;

import static com.nova.star.domain.RewardHistoryAsserts.*;
import static com.nova.star.web.rest.TestUtil.createUpdateProxyForBean;
import static com.nova.star.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nova.star.IntegrationTest;
import com.nova.star.domain.RewardHistory;
import com.nova.star.repository.RewardHistoryRepository;
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
 * Integration tests for the {@link RewardHistoryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RewardHistoryResourceIT {

    private static final String DEFAULT_ACTION = "AAAAAAAAAA";
    private static final String UPDATED_ACTION = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_ACTION_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_ACTION_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/reward-histories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private RewardHistoryRepository rewardHistoryRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRewardHistoryMockMvc;

    private RewardHistory rewardHistory;

    private RewardHistory insertedRewardHistory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RewardHistory createEntity() {
        return new RewardHistory().action(DEFAULT_ACTION).actionDate(DEFAULT_ACTION_DATE).description(DEFAULT_DESCRIPTION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RewardHistory createUpdatedEntity() {
        return new RewardHistory().action(UPDATED_ACTION).actionDate(UPDATED_ACTION_DATE).description(UPDATED_DESCRIPTION);
    }

    @BeforeEach
    public void initTest() {
        rewardHistory = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedRewardHistory != null) {
            rewardHistoryRepository.delete(insertedRewardHistory);
            insertedRewardHistory = null;
        }
    }

    @Test
    @Transactional
    void createRewardHistory() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the RewardHistory
        var returnedRewardHistory = om.readValue(
            restRewardHistoryMockMvc
                .perform(
                    post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(rewardHistory))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            RewardHistory.class
        );

        // Validate the RewardHistory in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertRewardHistoryUpdatableFieldsEquals(returnedRewardHistory, getPersistedRewardHistory(returnedRewardHistory));

        insertedRewardHistory = returnedRewardHistory;
    }

    @Test
    @Transactional
    void createRewardHistoryWithExistingId() throws Exception {
        // Create the RewardHistory with an existing ID
        rewardHistory.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRewardHistoryMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(rewardHistory)))
            .andExpect(status().isBadRequest());

        // Validate the RewardHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkActionIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        rewardHistory.setAction(null);

        // Create the RewardHistory, which fails.

        restRewardHistoryMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(rewardHistory)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkActionDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        rewardHistory.setActionDate(null);

        // Create the RewardHistory, which fails.

        restRewardHistoryMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(rewardHistory)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllRewardHistories() throws Exception {
        // Initialize the database
        insertedRewardHistory = rewardHistoryRepository.saveAndFlush(rewardHistory);

        // Get all the rewardHistoryList
        restRewardHistoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rewardHistory.getId().intValue())))
            .andExpect(jsonPath("$.[*].action").value(hasItem(DEFAULT_ACTION)))
            .andExpect(jsonPath("$.[*].actionDate").value(hasItem(sameInstant(DEFAULT_ACTION_DATE))))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    void getRewardHistory() throws Exception {
        // Initialize the database
        insertedRewardHistory = rewardHistoryRepository.saveAndFlush(rewardHistory);

        // Get the rewardHistory
        restRewardHistoryMockMvc
            .perform(get(ENTITY_API_URL_ID, rewardHistory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(rewardHistory.getId().intValue()))
            .andExpect(jsonPath("$.action").value(DEFAULT_ACTION))
            .andExpect(jsonPath("$.actionDate").value(sameInstant(DEFAULT_ACTION_DATE)))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    void getNonExistingRewardHistory() throws Exception {
        // Get the rewardHistory
        restRewardHistoryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingRewardHistory() throws Exception {
        // Initialize the database
        insertedRewardHistory = rewardHistoryRepository.saveAndFlush(rewardHistory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the rewardHistory
        RewardHistory updatedRewardHistory = rewardHistoryRepository.findById(rewardHistory.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedRewardHistory are not directly saved in db
        em.detach(updatedRewardHistory);
        updatedRewardHistory.action(UPDATED_ACTION).actionDate(UPDATED_ACTION_DATE).description(UPDATED_DESCRIPTION);

        restRewardHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedRewardHistory.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedRewardHistory))
            )
            .andExpect(status().isOk());

        // Validate the RewardHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedRewardHistoryToMatchAllProperties(updatedRewardHistory);
    }

    @Test
    @Transactional
    void putNonExistingRewardHistory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        rewardHistory.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRewardHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, rewardHistory.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(rewardHistory))
            )
            .andExpect(status().isBadRequest());

        // Validate the RewardHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRewardHistory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        rewardHistory.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRewardHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(rewardHistory))
            )
            .andExpect(status().isBadRequest());

        // Validate the RewardHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRewardHistory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        rewardHistory.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRewardHistoryMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(rewardHistory)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the RewardHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRewardHistoryWithPatch() throws Exception {
        // Initialize the database
        insertedRewardHistory = rewardHistoryRepository.saveAndFlush(rewardHistory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the rewardHistory using partial update
        RewardHistory partialUpdatedRewardHistory = new RewardHistory();
        partialUpdatedRewardHistory.setId(rewardHistory.getId());

        partialUpdatedRewardHistory.actionDate(UPDATED_ACTION_DATE).description(UPDATED_DESCRIPTION);

        restRewardHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRewardHistory.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedRewardHistory))
            )
            .andExpect(status().isOk());

        // Validate the RewardHistory in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertRewardHistoryUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedRewardHistory, rewardHistory),
            getPersistedRewardHistory(rewardHistory)
        );
    }

    @Test
    @Transactional
    void fullUpdateRewardHistoryWithPatch() throws Exception {
        // Initialize the database
        insertedRewardHistory = rewardHistoryRepository.saveAndFlush(rewardHistory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the rewardHistory using partial update
        RewardHistory partialUpdatedRewardHistory = new RewardHistory();
        partialUpdatedRewardHistory.setId(rewardHistory.getId());

        partialUpdatedRewardHistory.action(UPDATED_ACTION).actionDate(UPDATED_ACTION_DATE).description(UPDATED_DESCRIPTION);

        restRewardHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRewardHistory.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedRewardHistory))
            )
            .andExpect(status().isOk());

        // Validate the RewardHistory in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertRewardHistoryUpdatableFieldsEquals(partialUpdatedRewardHistory, getPersistedRewardHistory(partialUpdatedRewardHistory));
    }

    @Test
    @Transactional
    void patchNonExistingRewardHistory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        rewardHistory.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRewardHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, rewardHistory.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(rewardHistory))
            )
            .andExpect(status().isBadRequest());

        // Validate the RewardHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRewardHistory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        rewardHistory.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRewardHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(rewardHistory))
            )
            .andExpect(status().isBadRequest());

        // Validate the RewardHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRewardHistory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        rewardHistory.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRewardHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(rewardHistory))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the RewardHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRewardHistory() throws Exception {
        // Initialize the database
        insertedRewardHistory = rewardHistoryRepository.saveAndFlush(rewardHistory);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the rewardHistory
        restRewardHistoryMockMvc
            .perform(delete(ENTITY_API_URL_ID, rewardHistory.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return rewardHistoryRepository.count();
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

    protected RewardHistory getPersistedRewardHistory(RewardHistory rewardHistory) {
        return rewardHistoryRepository.findById(rewardHistory.getId()).orElseThrow();
    }

    protected void assertPersistedRewardHistoryToMatchAllProperties(RewardHistory expectedRewardHistory) {
        assertRewardHistoryAllPropertiesEquals(expectedRewardHistory, getPersistedRewardHistory(expectedRewardHistory));
    }

    protected void assertPersistedRewardHistoryToMatchUpdatableProperties(RewardHistory expectedRewardHistory) {
        assertRewardHistoryAllUpdatablePropertiesEquals(expectedRewardHistory, getPersistedRewardHistory(expectedRewardHistory));
    }
}
