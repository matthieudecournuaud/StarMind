package com.nova.star.web.rest;

import static com.nova.star.domain.RewardAsserts.*;
import static com.nova.star.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nova.star.IntegrationTest;
import com.nova.star.domain.Reward;
import com.nova.star.repository.RewardRepository;
import jakarta.persistence.EntityManager;
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
 * Integration tests for the {@link RewardResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RewardResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/rewards";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private RewardRepository rewardRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRewardMockMvc;

    private Reward reward;

    private Reward insertedReward;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Reward createEntity() {
        return new Reward().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Reward createUpdatedEntity() {
        return new Reward().name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
    }

    @BeforeEach
    public void initTest() {
        reward = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedReward != null) {
            rewardRepository.delete(insertedReward);
            insertedReward = null;
        }
    }

    @Test
    @Transactional
    void createReward() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Reward
        var returnedReward = om.readValue(
            restRewardMockMvc
                .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reward)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Reward.class
        );

        // Validate the Reward in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertRewardUpdatableFieldsEquals(returnedReward, getPersistedReward(returnedReward));

        insertedReward = returnedReward;
    }

    @Test
    @Transactional
    void createRewardWithExistingId() throws Exception {
        // Create the Reward with an existing ID
        reward.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRewardMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reward)))
            .andExpect(status().isBadRequest());

        // Validate the Reward in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        reward.setName(null);

        // Create the Reward, which fails.

        restRewardMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reward)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllRewards() throws Exception {
        // Initialize the database
        insertedReward = rewardRepository.saveAndFlush(reward);

        // Get all the rewardList
        restRewardMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reward.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getReward() throws Exception {
        // Initialize the database
        insertedReward = rewardRepository.saveAndFlush(reward);

        // Get the reward
        restRewardMockMvc
            .perform(get(ENTITY_API_URL_ID, reward.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(reward.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getNonExistingReward() throws Exception {
        // Get the reward
        restRewardMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingReward() throws Exception {
        // Initialize the database
        insertedReward = rewardRepository.saveAndFlush(reward);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the reward
        Reward updatedReward = rewardRepository.findById(reward.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedReward are not directly saved in db
        em.detach(updatedReward);
        updatedReward.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restRewardMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedReward.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedReward))
            )
            .andExpect(status().isOk());

        // Validate the Reward in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedRewardToMatchAllProperties(updatedReward);
    }

    @Test
    @Transactional
    void putNonExistingReward() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reward.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRewardMockMvc
            .perform(
                put(ENTITY_API_URL_ID, reward.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(reward))
            )
            .andExpect(status().isBadRequest());

        // Validate the Reward in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchReward() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reward.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRewardMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(reward))
            )
            .andExpect(status().isBadRequest());

        // Validate the Reward in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamReward() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reward.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRewardMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reward)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Reward in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRewardWithPatch() throws Exception {
        // Initialize the database
        insertedReward = rewardRepository.saveAndFlush(reward);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the reward using partial update
        Reward partialUpdatedReward = new Reward();
        partialUpdatedReward.setId(reward.getId());

        partialUpdatedReward.name(UPDATED_NAME);

        restRewardMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReward.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedReward))
            )
            .andExpect(status().isOk());

        // Validate the Reward in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertRewardUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedReward, reward), getPersistedReward(reward));
    }

    @Test
    @Transactional
    void fullUpdateRewardWithPatch() throws Exception {
        // Initialize the database
        insertedReward = rewardRepository.saveAndFlush(reward);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the reward using partial update
        Reward partialUpdatedReward = new Reward();
        partialUpdatedReward.setId(reward.getId());

        partialUpdatedReward.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restRewardMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReward.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedReward))
            )
            .andExpect(status().isOk());

        // Validate the Reward in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertRewardUpdatableFieldsEquals(partialUpdatedReward, getPersistedReward(partialUpdatedReward));
    }

    @Test
    @Transactional
    void patchNonExistingReward() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reward.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRewardMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, reward.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(reward))
            )
            .andExpect(status().isBadRequest());

        // Validate the Reward in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchReward() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reward.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRewardMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(reward))
            )
            .andExpect(status().isBadRequest());

        // Validate the Reward in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamReward() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reward.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRewardMockMvc
            .perform(patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(reward)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Reward in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteReward() throws Exception {
        // Initialize the database
        insertedReward = rewardRepository.saveAndFlush(reward);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the reward
        restRewardMockMvc
            .perform(delete(ENTITY_API_URL_ID, reward.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return rewardRepository.count();
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

    protected Reward getPersistedReward(Reward reward) {
        return rewardRepository.findById(reward.getId()).orElseThrow();
    }

    protected void assertPersistedRewardToMatchAllProperties(Reward expectedReward) {
        assertRewardAllPropertiesEquals(expectedReward, getPersistedReward(expectedReward));
    }

    protected void assertPersistedRewardToMatchUpdatableProperties(Reward expectedReward) {
        assertRewardAllUpdatablePropertiesEquals(expectedReward, getPersistedReward(expectedReward));
    }
}
