package com.nova.star.web.rest;

import static com.nova.star.domain.IdeaChatAsserts.*;
import static com.nova.star.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nova.star.IntegrationTest;
import com.nova.star.domain.IdeaChat;
import com.nova.star.repository.IdeaChatRepository;
import com.nova.star.repository.UserRepository;
import com.nova.star.service.IdeaChatService;
import com.nova.star.service.dto.IdeaChatDTO;
import com.nova.star.service.mapper.IdeaChatMapper;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link IdeaChatResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class IdeaChatResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/idea-chats";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private IdeaChatRepository ideaChatRepository;

    @Autowired
    private UserRepository userRepository;

    @Mock
    private IdeaChatRepository ideaChatRepositoryMock;

    @Autowired
    private IdeaChatMapper ideaChatMapper;

    @Mock
    private IdeaChatService ideaChatServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restIdeaChatMockMvc;

    private IdeaChat ideaChat;

    private IdeaChat insertedIdeaChat;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static IdeaChat createEntity() {
        return new IdeaChat().name(DEFAULT_NAME);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static IdeaChat createUpdatedEntity() {
        return new IdeaChat().name(UPDATED_NAME);
    }

    @BeforeEach
    public void initTest() {
        ideaChat = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedIdeaChat != null) {
            ideaChatRepository.delete(insertedIdeaChat);
            insertedIdeaChat = null;
        }
        userRepository.deleteAll();
    }

    @Test
    @Transactional
    void createIdeaChat() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the IdeaChat
        IdeaChatDTO ideaChatDTO = ideaChatMapper.toDto(ideaChat);
        var returnedIdeaChatDTO = om.readValue(
            restIdeaChatMockMvc
                .perform(
                    post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ideaChatDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            IdeaChatDTO.class
        );

        // Validate the IdeaChat in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedIdeaChat = ideaChatMapper.toEntity(returnedIdeaChatDTO);
        assertIdeaChatUpdatableFieldsEquals(returnedIdeaChat, getPersistedIdeaChat(returnedIdeaChat));

        insertedIdeaChat = returnedIdeaChat;
    }

    @Test
    @Transactional
    void createIdeaChatWithExistingId() throws Exception {
        // Create the IdeaChat with an existing ID
        ideaChat.setId(1L);
        IdeaChatDTO ideaChatDTO = ideaChatMapper.toDto(ideaChat);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restIdeaChatMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ideaChatDTO)))
            .andExpect(status().isBadRequest());

        // Validate the IdeaChat in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        ideaChat.setName(null);

        // Create the IdeaChat, which fails.
        IdeaChatDTO ideaChatDTO = ideaChatMapper.toDto(ideaChat);

        restIdeaChatMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ideaChatDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllIdeaChats() throws Exception {
        // Initialize the database
        insertedIdeaChat = ideaChatRepository.saveAndFlush(ideaChat);

        // Get all the ideaChatList
        restIdeaChatMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ideaChat.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllIdeaChatsWithEagerRelationshipsIsEnabled() throws Exception {
        when(ideaChatServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restIdeaChatMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(ideaChatServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllIdeaChatsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(ideaChatServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restIdeaChatMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(ideaChatRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getIdeaChat() throws Exception {
        // Initialize the database
        insertedIdeaChat = ideaChatRepository.saveAndFlush(ideaChat);

        // Get the ideaChat
        restIdeaChatMockMvc
            .perform(get(ENTITY_API_URL_ID, ideaChat.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ideaChat.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingIdeaChat() throws Exception {
        // Get the ideaChat
        restIdeaChatMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingIdeaChat() throws Exception {
        // Initialize the database
        insertedIdeaChat = ideaChatRepository.saveAndFlush(ideaChat);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the ideaChat
        IdeaChat updatedIdeaChat = ideaChatRepository.findById(ideaChat.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedIdeaChat are not directly saved in db
        em.detach(updatedIdeaChat);
        updatedIdeaChat.name(UPDATED_NAME);
        IdeaChatDTO ideaChatDTO = ideaChatMapper.toDto(updatedIdeaChat);

        restIdeaChatMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ideaChatDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(ideaChatDTO))
            )
            .andExpect(status().isOk());

        // Validate the IdeaChat in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedIdeaChatToMatchAllProperties(updatedIdeaChat);
    }

    @Test
    @Transactional
    void putNonExistingIdeaChat() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ideaChat.setId(longCount.incrementAndGet());

        // Create the IdeaChat
        IdeaChatDTO ideaChatDTO = ideaChatMapper.toDto(ideaChat);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIdeaChatMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ideaChatDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(ideaChatDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the IdeaChat in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchIdeaChat() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ideaChat.setId(longCount.incrementAndGet());

        // Create the IdeaChat
        IdeaChatDTO ideaChatDTO = ideaChatMapper.toDto(ideaChat);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIdeaChatMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(ideaChatDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the IdeaChat in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamIdeaChat() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ideaChat.setId(longCount.incrementAndGet());

        // Create the IdeaChat
        IdeaChatDTO ideaChatDTO = ideaChatMapper.toDto(ideaChat);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIdeaChatMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ideaChatDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the IdeaChat in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateIdeaChatWithPatch() throws Exception {
        // Initialize the database
        insertedIdeaChat = ideaChatRepository.saveAndFlush(ideaChat);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the ideaChat using partial update
        IdeaChat partialUpdatedIdeaChat = new IdeaChat();
        partialUpdatedIdeaChat.setId(ideaChat.getId());

        restIdeaChatMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIdeaChat.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedIdeaChat))
            )
            .andExpect(status().isOk());

        // Validate the IdeaChat in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertIdeaChatUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedIdeaChat, ideaChat), getPersistedIdeaChat(ideaChat));
    }

    @Test
    @Transactional
    void fullUpdateIdeaChatWithPatch() throws Exception {
        // Initialize the database
        insertedIdeaChat = ideaChatRepository.saveAndFlush(ideaChat);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the ideaChat using partial update
        IdeaChat partialUpdatedIdeaChat = new IdeaChat();
        partialUpdatedIdeaChat.setId(ideaChat.getId());

        partialUpdatedIdeaChat.name(UPDATED_NAME);

        restIdeaChatMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIdeaChat.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedIdeaChat))
            )
            .andExpect(status().isOk());

        // Validate the IdeaChat in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertIdeaChatUpdatableFieldsEquals(partialUpdatedIdeaChat, getPersistedIdeaChat(partialUpdatedIdeaChat));
    }

    @Test
    @Transactional
    void patchNonExistingIdeaChat() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ideaChat.setId(longCount.incrementAndGet());

        // Create the IdeaChat
        IdeaChatDTO ideaChatDTO = ideaChatMapper.toDto(ideaChat);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIdeaChatMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, ideaChatDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(ideaChatDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the IdeaChat in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchIdeaChat() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ideaChat.setId(longCount.incrementAndGet());

        // Create the IdeaChat
        IdeaChatDTO ideaChatDTO = ideaChatMapper.toDto(ideaChat);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIdeaChatMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(ideaChatDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the IdeaChat in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamIdeaChat() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ideaChat.setId(longCount.incrementAndGet());

        // Create the IdeaChat
        IdeaChatDTO ideaChatDTO = ideaChatMapper.toDto(ideaChat);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIdeaChatMockMvc
            .perform(
                patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(ideaChatDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the IdeaChat in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteIdeaChat() throws Exception {
        // Initialize the database
        insertedIdeaChat = ideaChatRepository.saveAndFlush(ideaChat);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the ideaChat
        restIdeaChatMockMvc
            .perform(delete(ENTITY_API_URL_ID, ideaChat.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return ideaChatRepository.count();
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

    protected IdeaChat getPersistedIdeaChat(IdeaChat ideaChat) {
        return ideaChatRepository.findById(ideaChat.getId()).orElseThrow();
    }

    protected void assertPersistedIdeaChatToMatchAllProperties(IdeaChat expectedIdeaChat) {
        assertIdeaChatAllPropertiesEquals(expectedIdeaChat, getPersistedIdeaChat(expectedIdeaChat));
    }

    protected void assertPersistedIdeaChatToMatchUpdatableProperties(IdeaChat expectedIdeaChat) {
        assertIdeaChatAllUpdatablePropertiesEquals(expectedIdeaChat, getPersistedIdeaChat(expectedIdeaChat));
    }
}
