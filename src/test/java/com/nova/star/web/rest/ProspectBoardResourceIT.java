package com.nova.star.web.rest;

import static com.nova.star.domain.ProspectBoardAsserts.*;
import static com.nova.star.web.rest.TestUtil.createUpdateProxyForBean;
import static com.nova.star.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nova.star.IntegrationTest;
import com.nova.star.domain.ProspectBoard;
import com.nova.star.repository.ProspectBoardRepository;
import com.nova.star.repository.UserRepository;
import com.nova.star.service.dto.ProspectBoardDTO;
import com.nova.star.service.mapper.ProspectBoardMapper;
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
 * Integration tests for the {@link ProspectBoardResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProspectBoardResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_CREATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_MODIFIED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_MODIFIED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String ENTITY_API_URL = "/api/prospect-boards";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ProspectBoardRepository prospectBoardRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProspectBoardMapper prospectBoardMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProspectBoardMockMvc;

    private ProspectBoard prospectBoard;

    private ProspectBoard insertedProspectBoard;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProspectBoard createEntity() {
        return new ProspectBoard()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .createdDate(DEFAULT_CREATED_DATE)
            .modifiedDate(DEFAULT_MODIFIED_DATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProspectBoard createUpdatedEntity() {
        return new ProspectBoard()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .createdDate(UPDATED_CREATED_DATE)
            .modifiedDate(UPDATED_MODIFIED_DATE);
    }

    @BeforeEach
    public void initTest() {
        prospectBoard = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedProspectBoard != null) {
            prospectBoardRepository.delete(insertedProspectBoard);
            insertedProspectBoard = null;
        }
        userRepository.deleteAll();
    }

    @Test
    @Transactional
    void createProspectBoard() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ProspectBoard
        ProspectBoardDTO prospectBoardDTO = prospectBoardMapper.toDto(prospectBoard);
        var returnedProspectBoardDTO = om.readValue(
            restProspectBoardMockMvc
                .perform(
                    post(ENTITY_API_URL)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsBytes(prospectBoardDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ProspectBoardDTO.class
        );

        // Validate the ProspectBoard in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedProspectBoard = prospectBoardMapper.toEntity(returnedProspectBoardDTO);
        assertProspectBoardUpdatableFieldsEquals(returnedProspectBoard, getPersistedProspectBoard(returnedProspectBoard));

        insertedProspectBoard = returnedProspectBoard;
    }

    @Test
    @Transactional
    void createProspectBoardWithExistingId() throws Exception {
        // Create the ProspectBoard with an existing ID
        prospectBoard.setId(1L);
        ProspectBoardDTO prospectBoardDTO = prospectBoardMapper.toDto(prospectBoard);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProspectBoardMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(prospectBoardDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProspectBoard in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        prospectBoard.setName(null);

        // Create the ProspectBoard, which fails.
        ProspectBoardDTO prospectBoardDTO = prospectBoardMapper.toDto(prospectBoard);

        restProspectBoardMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(prospectBoardDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        prospectBoard.setCreatedDate(null);

        // Create the ProspectBoard, which fails.
        ProspectBoardDTO prospectBoardDTO = prospectBoardMapper.toDto(prospectBoard);

        restProspectBoardMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(prospectBoardDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkModifiedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        prospectBoard.setModifiedDate(null);

        // Create the ProspectBoard, which fails.
        ProspectBoardDTO prospectBoardDTO = prospectBoardMapper.toDto(prospectBoard);

        restProspectBoardMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(prospectBoardDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllProspectBoards() throws Exception {
        // Initialize the database
        insertedProspectBoard = prospectBoardRepository.saveAndFlush(prospectBoard);

        // Get all the prospectBoardList
        restProspectBoardMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(prospectBoard.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(sameInstant(DEFAULT_CREATED_DATE))))
            .andExpect(jsonPath("$.[*].modifiedDate").value(hasItem(sameInstant(DEFAULT_MODIFIED_DATE))));
    }

    @Test
    @Transactional
    void getProspectBoard() throws Exception {
        // Initialize the database
        insertedProspectBoard = prospectBoardRepository.saveAndFlush(prospectBoard);

        // Get the prospectBoard
        restProspectBoardMockMvc
            .perform(get(ENTITY_API_URL_ID, prospectBoard.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(prospectBoard.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.createdDate").value(sameInstant(DEFAULT_CREATED_DATE)))
            .andExpect(jsonPath("$.modifiedDate").value(sameInstant(DEFAULT_MODIFIED_DATE)));
    }

    @Test
    @Transactional
    void getNonExistingProspectBoard() throws Exception {
        // Get the prospectBoard
        restProspectBoardMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProspectBoard() throws Exception {
        // Initialize the database
        insertedProspectBoard = prospectBoardRepository.saveAndFlush(prospectBoard);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the prospectBoard
        ProspectBoard updatedProspectBoard = prospectBoardRepository.findById(prospectBoard.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedProspectBoard are not directly saved in db
        em.detach(updatedProspectBoard);
        updatedProspectBoard
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .createdDate(UPDATED_CREATED_DATE)
            .modifiedDate(UPDATED_MODIFIED_DATE);
        ProspectBoardDTO prospectBoardDTO = prospectBoardMapper.toDto(updatedProspectBoard);

        restProspectBoardMockMvc
            .perform(
                put(ENTITY_API_URL_ID, prospectBoardDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(prospectBoardDTO))
            )
            .andExpect(status().isOk());

        // Validate the ProspectBoard in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedProspectBoardToMatchAllProperties(updatedProspectBoard);
    }

    @Test
    @Transactional
    void putNonExistingProspectBoard() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        prospectBoard.setId(longCount.incrementAndGet());

        // Create the ProspectBoard
        ProspectBoardDTO prospectBoardDTO = prospectBoardMapper.toDto(prospectBoard);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProspectBoardMockMvc
            .perform(
                put(ENTITY_API_URL_ID, prospectBoardDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(prospectBoardDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProspectBoard in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProspectBoard() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        prospectBoard.setId(longCount.incrementAndGet());

        // Create the ProspectBoard
        ProspectBoardDTO prospectBoardDTO = prospectBoardMapper.toDto(prospectBoard);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProspectBoardMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(prospectBoardDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProspectBoard in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProspectBoard() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        prospectBoard.setId(longCount.incrementAndGet());

        // Create the ProspectBoard
        ProspectBoardDTO prospectBoardDTO = prospectBoardMapper.toDto(prospectBoard);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProspectBoardMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(prospectBoardDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProspectBoard in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProspectBoardWithPatch() throws Exception {
        // Initialize the database
        insertedProspectBoard = prospectBoardRepository.saveAndFlush(prospectBoard);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the prospectBoard using partial update
        ProspectBoard partialUpdatedProspectBoard = new ProspectBoard();
        partialUpdatedProspectBoard.setId(prospectBoard.getId());

        partialUpdatedProspectBoard.name(UPDATED_NAME).createdDate(UPDATED_CREATED_DATE).modifiedDate(UPDATED_MODIFIED_DATE);

        restProspectBoardMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProspectBoard.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProspectBoard))
            )
            .andExpect(status().isOk());

        // Validate the ProspectBoard in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProspectBoardUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedProspectBoard, prospectBoard),
            getPersistedProspectBoard(prospectBoard)
        );
    }

    @Test
    @Transactional
    void fullUpdateProspectBoardWithPatch() throws Exception {
        // Initialize the database
        insertedProspectBoard = prospectBoardRepository.saveAndFlush(prospectBoard);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the prospectBoard using partial update
        ProspectBoard partialUpdatedProspectBoard = new ProspectBoard();
        partialUpdatedProspectBoard.setId(prospectBoard.getId());

        partialUpdatedProspectBoard
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .createdDate(UPDATED_CREATED_DATE)
            .modifiedDate(UPDATED_MODIFIED_DATE);

        restProspectBoardMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProspectBoard.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProspectBoard))
            )
            .andExpect(status().isOk());

        // Validate the ProspectBoard in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProspectBoardUpdatableFieldsEquals(partialUpdatedProspectBoard, getPersistedProspectBoard(partialUpdatedProspectBoard));
    }

    @Test
    @Transactional
    void patchNonExistingProspectBoard() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        prospectBoard.setId(longCount.incrementAndGet());

        // Create the ProspectBoard
        ProspectBoardDTO prospectBoardDTO = prospectBoardMapper.toDto(prospectBoard);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProspectBoardMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, prospectBoardDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(prospectBoardDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProspectBoard in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProspectBoard() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        prospectBoard.setId(longCount.incrementAndGet());

        // Create the ProspectBoard
        ProspectBoardDTO prospectBoardDTO = prospectBoardMapper.toDto(prospectBoard);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProspectBoardMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(prospectBoardDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProspectBoard in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProspectBoard() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        prospectBoard.setId(longCount.incrementAndGet());

        // Create the ProspectBoard
        ProspectBoardDTO prospectBoardDTO = prospectBoardMapper.toDto(prospectBoard);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProspectBoardMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(prospectBoardDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProspectBoard in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProspectBoard() throws Exception {
        // Initialize the database
        insertedProspectBoard = prospectBoardRepository.saveAndFlush(prospectBoard);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the prospectBoard
        restProspectBoardMockMvc
            .perform(delete(ENTITY_API_URL_ID, prospectBoard.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return prospectBoardRepository.count();
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

    protected ProspectBoard getPersistedProspectBoard(ProspectBoard prospectBoard) {
        return prospectBoardRepository.findById(prospectBoard.getId()).orElseThrow();
    }

    protected void assertPersistedProspectBoardToMatchAllProperties(ProspectBoard expectedProspectBoard) {
        assertProspectBoardAllPropertiesEquals(expectedProspectBoard, getPersistedProspectBoard(expectedProspectBoard));
    }

    protected void assertPersistedProspectBoardToMatchUpdatableProperties(ProspectBoard expectedProspectBoard) {
        assertProspectBoardAllUpdatablePropertiesEquals(expectedProspectBoard, getPersistedProspectBoard(expectedProspectBoard));
    }
}
