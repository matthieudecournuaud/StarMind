package com.nova.star.web.rest;

import static com.nova.star.domain.ProspectEntryAsserts.*;
import static com.nova.star.web.rest.TestUtil.createUpdateProxyForBean;
import static com.nova.star.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nova.star.IntegrationTest;
import com.nova.star.domain.ProspectEntry;
import com.nova.star.domain.enumeration.ProspectStatus;
import com.nova.star.repository.ProspectEntryRepository;
import com.nova.star.repository.UserRepository;
import com.nova.star.service.dto.ProspectEntryDTO;
import com.nova.star.service.mapper.ProspectEntryMapper;
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
 * Integration tests for the {@link ProspectEntryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProspectEntryResourceIT {

    private static final ProspectStatus DEFAULT_STATUS = ProspectStatus.NEW;
    private static final ProspectStatus UPDATED_STATUS = ProspectStatus.CONTACTED;

    private static final ZonedDateTime DEFAULT_CREATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_MODIFIED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_MODIFIED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String ENTITY_API_URL = "/api/prospect-entries";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ProspectEntryRepository prospectEntryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProspectEntryMapper prospectEntryMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProspectEntryMockMvc;

    private ProspectEntry prospectEntry;

    private ProspectEntry insertedProspectEntry;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProspectEntry createEntity() {
        return new ProspectEntry().status(DEFAULT_STATUS).createdDate(DEFAULT_CREATED_DATE).modifiedDate(DEFAULT_MODIFIED_DATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProspectEntry createUpdatedEntity() {
        return new ProspectEntry().status(UPDATED_STATUS).createdDate(UPDATED_CREATED_DATE).modifiedDate(UPDATED_MODIFIED_DATE);
    }

    @BeforeEach
    public void initTest() {
        prospectEntry = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedProspectEntry != null) {
            prospectEntryRepository.delete(insertedProspectEntry);
            insertedProspectEntry = null;
        }
        userRepository.deleteAll();
    }

    @Test
    @Transactional
    void createProspectEntry() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ProspectEntry
        ProspectEntryDTO prospectEntryDTO = prospectEntryMapper.toDto(prospectEntry);
        var returnedProspectEntryDTO = om.readValue(
            restProspectEntryMockMvc
                .perform(
                    post(ENTITY_API_URL)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsBytes(prospectEntryDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ProspectEntryDTO.class
        );

        // Validate the ProspectEntry in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedProspectEntry = prospectEntryMapper.toEntity(returnedProspectEntryDTO);
        assertProspectEntryUpdatableFieldsEquals(returnedProspectEntry, getPersistedProspectEntry(returnedProspectEntry));

        insertedProspectEntry = returnedProspectEntry;
    }

    @Test
    @Transactional
    void createProspectEntryWithExistingId() throws Exception {
        // Create the ProspectEntry with an existing ID
        prospectEntry.setId(1L);
        ProspectEntryDTO prospectEntryDTO = prospectEntryMapper.toDto(prospectEntry);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProspectEntryMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(prospectEntryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProspectEntry in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        prospectEntry.setStatus(null);

        // Create the ProspectEntry, which fails.
        ProspectEntryDTO prospectEntryDTO = prospectEntryMapper.toDto(prospectEntry);

        restProspectEntryMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(prospectEntryDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        prospectEntry.setCreatedDate(null);

        // Create the ProspectEntry, which fails.
        ProspectEntryDTO prospectEntryDTO = prospectEntryMapper.toDto(prospectEntry);

        restProspectEntryMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(prospectEntryDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkModifiedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        prospectEntry.setModifiedDate(null);

        // Create the ProspectEntry, which fails.
        ProspectEntryDTO prospectEntryDTO = prospectEntryMapper.toDto(prospectEntry);

        restProspectEntryMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(prospectEntryDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllProspectEntries() throws Exception {
        // Initialize the database
        insertedProspectEntry = prospectEntryRepository.saveAndFlush(prospectEntry);

        // Get all the prospectEntryList
        restProspectEntryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(prospectEntry.getId().intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(sameInstant(DEFAULT_CREATED_DATE))))
            .andExpect(jsonPath("$.[*].modifiedDate").value(hasItem(sameInstant(DEFAULT_MODIFIED_DATE))));
    }

    @Test
    @Transactional
    void getProspectEntry() throws Exception {
        // Initialize the database
        insertedProspectEntry = prospectEntryRepository.saveAndFlush(prospectEntry);

        // Get the prospectEntry
        restProspectEntryMockMvc
            .perform(get(ENTITY_API_URL_ID, prospectEntry.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(prospectEntry.getId().intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.createdDate").value(sameInstant(DEFAULT_CREATED_DATE)))
            .andExpect(jsonPath("$.modifiedDate").value(sameInstant(DEFAULT_MODIFIED_DATE)));
    }

    @Test
    @Transactional
    void getNonExistingProspectEntry() throws Exception {
        // Get the prospectEntry
        restProspectEntryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProspectEntry() throws Exception {
        // Initialize the database
        insertedProspectEntry = prospectEntryRepository.saveAndFlush(prospectEntry);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the prospectEntry
        ProspectEntry updatedProspectEntry = prospectEntryRepository.findById(prospectEntry.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedProspectEntry are not directly saved in db
        em.detach(updatedProspectEntry);
        updatedProspectEntry.status(UPDATED_STATUS).createdDate(UPDATED_CREATED_DATE).modifiedDate(UPDATED_MODIFIED_DATE);
        ProspectEntryDTO prospectEntryDTO = prospectEntryMapper.toDto(updatedProspectEntry);

        restProspectEntryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, prospectEntryDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(prospectEntryDTO))
            )
            .andExpect(status().isOk());

        // Validate the ProspectEntry in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedProspectEntryToMatchAllProperties(updatedProspectEntry);
    }

    @Test
    @Transactional
    void putNonExistingProspectEntry() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        prospectEntry.setId(longCount.incrementAndGet());

        // Create the ProspectEntry
        ProspectEntryDTO prospectEntryDTO = prospectEntryMapper.toDto(prospectEntry);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProspectEntryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, prospectEntryDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(prospectEntryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProspectEntry in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProspectEntry() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        prospectEntry.setId(longCount.incrementAndGet());

        // Create the ProspectEntry
        ProspectEntryDTO prospectEntryDTO = prospectEntryMapper.toDto(prospectEntry);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProspectEntryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(prospectEntryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProspectEntry in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProspectEntry() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        prospectEntry.setId(longCount.incrementAndGet());

        // Create the ProspectEntry
        ProspectEntryDTO prospectEntryDTO = prospectEntryMapper.toDto(prospectEntry);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProspectEntryMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(prospectEntryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProspectEntry in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProspectEntryWithPatch() throws Exception {
        // Initialize the database
        insertedProspectEntry = prospectEntryRepository.saveAndFlush(prospectEntry);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the prospectEntry using partial update
        ProspectEntry partialUpdatedProspectEntry = new ProspectEntry();
        partialUpdatedProspectEntry.setId(prospectEntry.getId());

        partialUpdatedProspectEntry.status(UPDATED_STATUS).modifiedDate(UPDATED_MODIFIED_DATE);

        restProspectEntryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProspectEntry.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProspectEntry))
            )
            .andExpect(status().isOk());

        // Validate the ProspectEntry in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProspectEntryUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedProspectEntry, prospectEntry),
            getPersistedProspectEntry(prospectEntry)
        );
    }

    @Test
    @Transactional
    void fullUpdateProspectEntryWithPatch() throws Exception {
        // Initialize the database
        insertedProspectEntry = prospectEntryRepository.saveAndFlush(prospectEntry);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the prospectEntry using partial update
        ProspectEntry partialUpdatedProspectEntry = new ProspectEntry();
        partialUpdatedProspectEntry.setId(prospectEntry.getId());

        partialUpdatedProspectEntry.status(UPDATED_STATUS).createdDate(UPDATED_CREATED_DATE).modifiedDate(UPDATED_MODIFIED_DATE);

        restProspectEntryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProspectEntry.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProspectEntry))
            )
            .andExpect(status().isOk());

        // Validate the ProspectEntry in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProspectEntryUpdatableFieldsEquals(partialUpdatedProspectEntry, getPersistedProspectEntry(partialUpdatedProspectEntry));
    }

    @Test
    @Transactional
    void patchNonExistingProspectEntry() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        prospectEntry.setId(longCount.incrementAndGet());

        // Create the ProspectEntry
        ProspectEntryDTO prospectEntryDTO = prospectEntryMapper.toDto(prospectEntry);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProspectEntryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, prospectEntryDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(prospectEntryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProspectEntry in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProspectEntry() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        prospectEntry.setId(longCount.incrementAndGet());

        // Create the ProspectEntry
        ProspectEntryDTO prospectEntryDTO = prospectEntryMapper.toDto(prospectEntry);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProspectEntryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(prospectEntryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProspectEntry in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProspectEntry() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        prospectEntry.setId(longCount.incrementAndGet());

        // Create the ProspectEntry
        ProspectEntryDTO prospectEntryDTO = prospectEntryMapper.toDto(prospectEntry);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProspectEntryMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(prospectEntryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProspectEntry in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProspectEntry() throws Exception {
        // Initialize the database
        insertedProspectEntry = prospectEntryRepository.saveAndFlush(prospectEntry);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the prospectEntry
        restProspectEntryMockMvc
            .perform(delete(ENTITY_API_URL_ID, prospectEntry.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return prospectEntryRepository.count();
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

    protected ProspectEntry getPersistedProspectEntry(ProspectEntry prospectEntry) {
        return prospectEntryRepository.findById(prospectEntry.getId()).orElseThrow();
    }

    protected void assertPersistedProspectEntryToMatchAllProperties(ProspectEntry expectedProspectEntry) {
        assertProspectEntryAllPropertiesEquals(expectedProspectEntry, getPersistedProspectEntry(expectedProspectEntry));
    }

    protected void assertPersistedProspectEntryToMatchUpdatableProperties(ProspectEntry expectedProspectEntry) {
        assertProspectEntryAllUpdatablePropertiesEquals(expectedProspectEntry, getPersistedProspectEntry(expectedProspectEntry));
    }
}
