package com.nova.star.web.rest;

import static com.nova.star.domain.IdeaAsserts.*;
import static com.nova.star.web.rest.TestUtil.createUpdateProxyForBean;
import static com.nova.star.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nova.star.IntegrationTest;
import com.nova.star.domain.Idea;
import com.nova.star.domain.enumeration.IdeaStatus;
import com.nova.star.domain.enumeration.RewardType;
import com.nova.star.repository.IdeaRepository;
import com.nova.star.repository.UserRepository;
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
 * Integration tests for the {@link IdeaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class IdeaResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final IdeaStatus DEFAULT_STATUS = IdeaStatus.OPEN;
    private static final IdeaStatus UPDATED_STATUS = IdeaStatus.IN_PROGRESS;

    private static final Boolean DEFAULT_VALIDATION = false;
    private static final Boolean UPDATED_VALIDATION = true;

    private static final RewardType DEFAULT_REWARD_TYPE = RewardType.BRONZE;
    private static final RewardType UPDATED_REWARD_TYPE = RewardType.ARGENT;

    private static final String DEFAULT_LIKES = "AAAAAAAAAA";
    private static final String UPDATED_LIKES = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_CREATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_MODIFIED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_MODIFIED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String ENTITY_API_URL = "/api/ideas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private IdeaRepository ideaRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restIdeaMockMvc;

    private Idea idea;

    private Idea insertedIdea;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Idea createEntity() {
        return new Idea()
            .title(DEFAULT_TITLE)
            .description(DEFAULT_DESCRIPTION)
            .status(DEFAULT_STATUS)
            .validation(DEFAULT_VALIDATION)
            .rewardType(DEFAULT_REWARD_TYPE)
            .likes(DEFAULT_LIKES)
            .createdDate(DEFAULT_CREATED_DATE)
            .modifiedDate(DEFAULT_MODIFIED_DATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Idea createUpdatedEntity() {
        return new Idea()
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .status(UPDATED_STATUS)
            .validation(UPDATED_VALIDATION)
            .rewardType(UPDATED_REWARD_TYPE)
            .likes(UPDATED_LIKES)
            .createdDate(UPDATED_CREATED_DATE)
            .modifiedDate(UPDATED_MODIFIED_DATE);
    }

    @BeforeEach
    public void initTest() {
        idea = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedIdea != null) {
            ideaRepository.delete(insertedIdea);
            insertedIdea = null;
        }
        userRepository.deleteAll();
    }

    @Test
    @Transactional
    void createIdea() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Idea
        var returnedIdea = om.readValue(
            restIdeaMockMvc
                .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(idea)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Idea.class
        );

        // Validate the Idea in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertIdeaUpdatableFieldsEquals(returnedIdea, getPersistedIdea(returnedIdea));

        insertedIdea = returnedIdea;
    }

    @Test
    @Transactional
    void createIdeaWithExistingId() throws Exception {
        // Create the Idea with an existing ID
        idea.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restIdeaMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(idea)))
            .andExpect(status().isBadRequest());

        // Validate the Idea in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        idea.setTitle(null);

        // Create the Idea, which fails.

        restIdeaMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(idea)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        idea.setStatus(null);

        // Create the Idea, which fails.

        restIdeaMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(idea)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllIdeas() throws Exception {
        // Initialize the database
        insertedIdea = ideaRepository.saveAndFlush(idea);

        // Get all the ideaList
        restIdeaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(idea.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].validation").value(hasItem(DEFAULT_VALIDATION.booleanValue())))
            .andExpect(jsonPath("$.[*].rewardType").value(hasItem(DEFAULT_REWARD_TYPE.toString())))
            .andExpect(jsonPath("$.[*].likes").value(hasItem(DEFAULT_LIKES)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(sameInstant(DEFAULT_CREATED_DATE))))
            .andExpect(jsonPath("$.[*].modifiedDate").value(hasItem(sameInstant(DEFAULT_MODIFIED_DATE))));
    }

    @Test
    @Transactional
    void getIdea() throws Exception {
        // Initialize the database
        insertedIdea = ideaRepository.saveAndFlush(idea);

        // Get the idea
        restIdeaMockMvc
            .perform(get(ENTITY_API_URL_ID, idea.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(idea.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.validation").value(DEFAULT_VALIDATION.booleanValue()))
            .andExpect(jsonPath("$.rewardType").value(DEFAULT_REWARD_TYPE.toString()))
            .andExpect(jsonPath("$.likes").value(DEFAULT_LIKES))
            .andExpect(jsonPath("$.createdDate").value(sameInstant(DEFAULT_CREATED_DATE)))
            .andExpect(jsonPath("$.modifiedDate").value(sameInstant(DEFAULT_MODIFIED_DATE)));
    }

    @Test
    @Transactional
    void getNonExistingIdea() throws Exception {
        // Get the idea
        restIdeaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingIdea() throws Exception {
        // Initialize the database
        insertedIdea = ideaRepository.saveAndFlush(idea);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the idea
        Idea updatedIdea = ideaRepository.findById(idea.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedIdea are not directly saved in db
        em.detach(updatedIdea);
        updatedIdea
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .status(UPDATED_STATUS)
            .validation(UPDATED_VALIDATION)
            .rewardType(UPDATED_REWARD_TYPE)
            .likes(UPDATED_LIKES)
            .createdDate(UPDATED_CREATED_DATE)
            .modifiedDate(UPDATED_MODIFIED_DATE);

        restIdeaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedIdea.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedIdea))
            )
            .andExpect(status().isOk());

        // Validate the Idea in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedIdeaToMatchAllProperties(updatedIdea);
    }

    @Test
    @Transactional
    void putNonExistingIdea() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        idea.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIdeaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, idea.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(idea))
            )
            .andExpect(status().isBadRequest());

        // Validate the Idea in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchIdea() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        idea.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIdeaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(idea))
            )
            .andExpect(status().isBadRequest());

        // Validate the Idea in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamIdea() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        idea.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIdeaMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(idea)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Idea in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateIdeaWithPatch() throws Exception {
        // Initialize the database
        insertedIdea = ideaRepository.saveAndFlush(idea);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the idea using partial update
        Idea partialUpdatedIdea = new Idea();
        partialUpdatedIdea.setId(idea.getId());

        partialUpdatedIdea
            .title(UPDATED_TITLE)
            .status(UPDATED_STATUS)
            .validation(UPDATED_VALIDATION)
            .rewardType(UPDATED_REWARD_TYPE)
            .createdDate(UPDATED_CREATED_DATE)
            .modifiedDate(UPDATED_MODIFIED_DATE);

        restIdeaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIdea.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedIdea))
            )
            .andExpect(status().isOk());

        // Validate the Idea in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertIdeaUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedIdea, idea), getPersistedIdea(idea));
    }

    @Test
    @Transactional
    void fullUpdateIdeaWithPatch() throws Exception {
        // Initialize the database
        insertedIdea = ideaRepository.saveAndFlush(idea);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the idea using partial update
        Idea partialUpdatedIdea = new Idea();
        partialUpdatedIdea.setId(idea.getId());

        partialUpdatedIdea
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .status(UPDATED_STATUS)
            .validation(UPDATED_VALIDATION)
            .rewardType(UPDATED_REWARD_TYPE)
            .likes(UPDATED_LIKES)
            .createdDate(UPDATED_CREATED_DATE)
            .modifiedDate(UPDATED_MODIFIED_DATE);

        restIdeaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIdea.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedIdea))
            )
            .andExpect(status().isOk());

        // Validate the Idea in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertIdeaUpdatableFieldsEquals(partialUpdatedIdea, getPersistedIdea(partialUpdatedIdea));
    }

    @Test
    @Transactional
    void patchNonExistingIdea() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        idea.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIdeaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, idea.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(idea))
            )
            .andExpect(status().isBadRequest());

        // Validate the Idea in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchIdea() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        idea.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIdeaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(idea))
            )
            .andExpect(status().isBadRequest());

        // Validate the Idea in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamIdea() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        idea.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIdeaMockMvc
            .perform(patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(idea)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Idea in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteIdea() throws Exception {
        // Initialize the database
        insertedIdea = ideaRepository.saveAndFlush(idea);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the idea
        restIdeaMockMvc
            .perform(delete(ENTITY_API_URL_ID, idea.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return ideaRepository.count();
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

    protected Idea getPersistedIdea(Idea idea) {
        return ideaRepository.findById(idea.getId()).orElseThrow();
    }

    protected void assertPersistedIdeaToMatchAllProperties(Idea expectedIdea) {
        assertIdeaAllPropertiesEquals(expectedIdea, getPersistedIdea(expectedIdea));
    }

    protected void assertPersistedIdeaToMatchUpdatableProperties(Idea expectedIdea) {
        assertIdeaAllUpdatablePropertiesEquals(expectedIdea, getPersistedIdea(expectedIdea));
    }
}
