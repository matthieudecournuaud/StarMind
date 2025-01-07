package com.nova.star.web.rest;

import static com.nova.star.domain.VoteAsserts.*;
import static com.nova.star.web.rest.TestUtil.createUpdateProxyForBean;
import static com.nova.star.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nova.star.IntegrationTest;
import com.nova.star.domain.Vote;
import com.nova.star.domain.enumeration.VoteOption;
import com.nova.star.repository.UserRepository;
import com.nova.star.repository.VoteRepository;
import com.nova.star.service.dto.VoteDTO;
import com.nova.star.service.mapper.VoteMapper;
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
 * Integration tests for the {@link VoteResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class VoteResourceIT {

    private static final VoteOption DEFAULT_VOTE_OPTION = VoteOption.POUR;
    private static final VoteOption UPDATED_VOTE_OPTION = VoteOption.CONTRE;

    private static final String DEFAULT_COMMENT = "AAAAAAAAAA";
    private static final String UPDATED_COMMENT = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_CREATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String ENTITY_API_URL = "/api/votes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private VoteRepository voteRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VoteMapper voteMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVoteMockMvc;

    private Vote vote;

    private Vote insertedVote;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Vote createEntity() {
        return new Vote().voteOption(DEFAULT_VOTE_OPTION).comment(DEFAULT_COMMENT).createdDate(DEFAULT_CREATED_DATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Vote createUpdatedEntity() {
        return new Vote().voteOption(UPDATED_VOTE_OPTION).comment(UPDATED_COMMENT).createdDate(UPDATED_CREATED_DATE);
    }

    @BeforeEach
    public void initTest() {
        vote = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedVote != null) {
            voteRepository.delete(insertedVote);
            insertedVote = null;
        }
        userRepository.deleteAll();
    }

    @Test
    @Transactional
    void createVote() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Vote
        VoteDTO voteDTO = voteMapper.toDto(vote);
        var returnedVoteDTO = om.readValue(
            restVoteMockMvc
                .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(voteDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            VoteDTO.class
        );

        // Validate the Vote in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedVote = voteMapper.toEntity(returnedVoteDTO);
        assertVoteUpdatableFieldsEquals(returnedVote, getPersistedVote(returnedVote));

        insertedVote = returnedVote;
    }

    @Test
    @Transactional
    void createVoteWithExistingId() throws Exception {
        // Create the Vote with an existing ID
        vote.setId(1L);
        VoteDTO voteDTO = voteMapper.toDto(vote);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restVoteMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(voteDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Vote in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkVoteOptionIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        vote.setVoteOption(null);

        // Create the Vote, which fails.
        VoteDTO voteDTO = voteMapper.toDto(vote);

        restVoteMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(voteDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        vote.setCreatedDate(null);

        // Create the Vote, which fails.
        VoteDTO voteDTO = voteMapper.toDto(vote);

        restVoteMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(voteDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllVotes() throws Exception {
        // Initialize the database
        insertedVote = voteRepository.saveAndFlush(vote);

        // Get all the voteList
        restVoteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vote.getId().intValue())))
            .andExpect(jsonPath("$.[*].voteOption").value(hasItem(DEFAULT_VOTE_OPTION.toString())))
            .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT.toString())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(sameInstant(DEFAULT_CREATED_DATE))));
    }

    @Test
    @Transactional
    void getVote() throws Exception {
        // Initialize the database
        insertedVote = voteRepository.saveAndFlush(vote);

        // Get the vote
        restVoteMockMvc
            .perform(get(ENTITY_API_URL_ID, vote.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(vote.getId().intValue()))
            .andExpect(jsonPath("$.voteOption").value(DEFAULT_VOTE_OPTION.toString()))
            .andExpect(jsonPath("$.comment").value(DEFAULT_COMMENT.toString()))
            .andExpect(jsonPath("$.createdDate").value(sameInstant(DEFAULT_CREATED_DATE)));
    }

    @Test
    @Transactional
    void getNonExistingVote() throws Exception {
        // Get the vote
        restVoteMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingVote() throws Exception {
        // Initialize the database
        insertedVote = voteRepository.saveAndFlush(vote);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the vote
        Vote updatedVote = voteRepository.findById(vote.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedVote are not directly saved in db
        em.detach(updatedVote);
        updatedVote.voteOption(UPDATED_VOTE_OPTION).comment(UPDATED_COMMENT).createdDate(UPDATED_CREATED_DATE);
        VoteDTO voteDTO = voteMapper.toDto(updatedVote);

        restVoteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, voteDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(voteDTO))
            )
            .andExpect(status().isOk());

        // Validate the Vote in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedVoteToMatchAllProperties(updatedVote);
    }

    @Test
    @Transactional
    void putNonExistingVote() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vote.setId(longCount.incrementAndGet());

        // Create the Vote
        VoteDTO voteDTO = voteMapper.toDto(vote);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVoteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, voteDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(voteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vote in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchVote() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vote.setId(longCount.incrementAndGet());

        // Create the Vote
        VoteDTO voteDTO = voteMapper.toDto(vote);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVoteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(voteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vote in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamVote() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vote.setId(longCount.incrementAndGet());

        // Create the Vote
        VoteDTO voteDTO = voteMapper.toDto(vote);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVoteMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(voteDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Vote in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateVoteWithPatch() throws Exception {
        // Initialize the database
        insertedVote = voteRepository.saveAndFlush(vote);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the vote using partial update
        Vote partialUpdatedVote = new Vote();
        partialUpdatedVote.setId(vote.getId());

        partialUpdatedVote.voteOption(UPDATED_VOTE_OPTION).comment(UPDATED_COMMENT);

        restVoteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVote.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedVote))
            )
            .andExpect(status().isOk());

        // Validate the Vote in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertVoteUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedVote, vote), getPersistedVote(vote));
    }

    @Test
    @Transactional
    void fullUpdateVoteWithPatch() throws Exception {
        // Initialize the database
        insertedVote = voteRepository.saveAndFlush(vote);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the vote using partial update
        Vote partialUpdatedVote = new Vote();
        partialUpdatedVote.setId(vote.getId());

        partialUpdatedVote.voteOption(UPDATED_VOTE_OPTION).comment(UPDATED_COMMENT).createdDate(UPDATED_CREATED_DATE);

        restVoteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVote.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedVote))
            )
            .andExpect(status().isOk());

        // Validate the Vote in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertVoteUpdatableFieldsEquals(partialUpdatedVote, getPersistedVote(partialUpdatedVote));
    }

    @Test
    @Transactional
    void patchNonExistingVote() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vote.setId(longCount.incrementAndGet());

        // Create the Vote
        VoteDTO voteDTO = voteMapper.toDto(vote);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVoteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, voteDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(voteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vote in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchVote() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vote.setId(longCount.incrementAndGet());

        // Create the Vote
        VoteDTO voteDTO = voteMapper.toDto(vote);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVoteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(voteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vote in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamVote() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vote.setId(longCount.incrementAndGet());

        // Create the Vote
        VoteDTO voteDTO = voteMapper.toDto(vote);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVoteMockMvc
            .perform(patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(voteDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Vote in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteVote() throws Exception {
        // Initialize the database
        insertedVote = voteRepository.saveAndFlush(vote);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the vote
        restVoteMockMvc
            .perform(delete(ENTITY_API_URL_ID, vote.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return voteRepository.count();
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

    protected Vote getPersistedVote(Vote vote) {
        return voteRepository.findById(vote.getId()).orElseThrow();
    }

    protected void assertPersistedVoteToMatchAllProperties(Vote expectedVote) {
        assertVoteAllPropertiesEquals(expectedVote, getPersistedVote(expectedVote));
    }

    protected void assertPersistedVoteToMatchUpdatableProperties(Vote expectedVote) {
        assertVoteAllUpdatablePropertiesEquals(expectedVote, getPersistedVote(expectedVote));
    }
}
