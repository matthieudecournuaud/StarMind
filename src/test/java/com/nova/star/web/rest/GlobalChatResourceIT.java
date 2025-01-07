package com.nova.star.web.rest;

import static com.nova.star.domain.GlobalChatAsserts.*;
import static com.nova.star.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nova.star.IntegrationTest;
import com.nova.star.domain.GlobalChat;
import com.nova.star.repository.GlobalChatRepository;
import com.nova.star.repository.UserRepository;
import com.nova.star.service.GlobalChatService;
import com.nova.star.service.dto.GlobalChatDTO;
import com.nova.star.service.mapper.GlobalChatMapper;
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
 * Integration tests for the {@link GlobalChatResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class GlobalChatResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/global-chats";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private GlobalChatRepository globalChatRepository;

    @Autowired
    private UserRepository userRepository;

    @Mock
    private GlobalChatRepository globalChatRepositoryMock;

    @Autowired
    private GlobalChatMapper globalChatMapper;

    @Mock
    private GlobalChatService globalChatServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restGlobalChatMockMvc;

    private GlobalChat globalChat;

    private GlobalChat insertedGlobalChat;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GlobalChat createEntity() {
        return new GlobalChat().name(DEFAULT_NAME);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GlobalChat createUpdatedEntity() {
        return new GlobalChat().name(UPDATED_NAME);
    }

    @BeforeEach
    public void initTest() {
        globalChat = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedGlobalChat != null) {
            globalChatRepository.delete(insertedGlobalChat);
            insertedGlobalChat = null;
        }
        userRepository.deleteAll();
    }

    @Test
    @Transactional
    void createGlobalChat() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the GlobalChat
        GlobalChatDTO globalChatDTO = globalChatMapper.toDto(globalChat);
        var returnedGlobalChatDTO = om.readValue(
            restGlobalChatMockMvc
                .perform(
                    post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(globalChatDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            GlobalChatDTO.class
        );

        // Validate the GlobalChat in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedGlobalChat = globalChatMapper.toEntity(returnedGlobalChatDTO);
        assertGlobalChatUpdatableFieldsEquals(returnedGlobalChat, getPersistedGlobalChat(returnedGlobalChat));

        insertedGlobalChat = returnedGlobalChat;
    }

    @Test
    @Transactional
    void createGlobalChatWithExistingId() throws Exception {
        // Create the GlobalChat with an existing ID
        globalChat.setId(1L);
        GlobalChatDTO globalChatDTO = globalChatMapper.toDto(globalChat);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restGlobalChatMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(globalChatDTO)))
            .andExpect(status().isBadRequest());

        // Validate the GlobalChat in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        globalChat.setName(null);

        // Create the GlobalChat, which fails.
        GlobalChatDTO globalChatDTO = globalChatMapper.toDto(globalChat);

        restGlobalChatMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(globalChatDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllGlobalChats() throws Exception {
        // Initialize the database
        insertedGlobalChat = globalChatRepository.saveAndFlush(globalChat);

        // Get all the globalChatList
        restGlobalChatMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(globalChat.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllGlobalChatsWithEagerRelationshipsIsEnabled() throws Exception {
        when(globalChatServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restGlobalChatMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(globalChatServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllGlobalChatsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(globalChatServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restGlobalChatMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(globalChatRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getGlobalChat() throws Exception {
        // Initialize the database
        insertedGlobalChat = globalChatRepository.saveAndFlush(globalChat);

        // Get the globalChat
        restGlobalChatMockMvc
            .perform(get(ENTITY_API_URL_ID, globalChat.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(globalChat.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingGlobalChat() throws Exception {
        // Get the globalChat
        restGlobalChatMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingGlobalChat() throws Exception {
        // Initialize the database
        insertedGlobalChat = globalChatRepository.saveAndFlush(globalChat);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the globalChat
        GlobalChat updatedGlobalChat = globalChatRepository.findById(globalChat.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedGlobalChat are not directly saved in db
        em.detach(updatedGlobalChat);
        updatedGlobalChat.name(UPDATED_NAME);
        GlobalChatDTO globalChatDTO = globalChatMapper.toDto(updatedGlobalChat);

        restGlobalChatMockMvc
            .perform(
                put(ENTITY_API_URL_ID, globalChatDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(globalChatDTO))
            )
            .andExpect(status().isOk());

        // Validate the GlobalChat in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedGlobalChatToMatchAllProperties(updatedGlobalChat);
    }

    @Test
    @Transactional
    void putNonExistingGlobalChat() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        globalChat.setId(longCount.incrementAndGet());

        // Create the GlobalChat
        GlobalChatDTO globalChatDTO = globalChatMapper.toDto(globalChat);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGlobalChatMockMvc
            .perform(
                put(ENTITY_API_URL_ID, globalChatDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(globalChatDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the GlobalChat in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchGlobalChat() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        globalChat.setId(longCount.incrementAndGet());

        // Create the GlobalChat
        GlobalChatDTO globalChatDTO = globalChatMapper.toDto(globalChat);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGlobalChatMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(globalChatDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the GlobalChat in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamGlobalChat() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        globalChat.setId(longCount.incrementAndGet());

        // Create the GlobalChat
        GlobalChatDTO globalChatDTO = globalChatMapper.toDto(globalChat);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGlobalChatMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(globalChatDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the GlobalChat in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateGlobalChatWithPatch() throws Exception {
        // Initialize the database
        insertedGlobalChat = globalChatRepository.saveAndFlush(globalChat);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the globalChat using partial update
        GlobalChat partialUpdatedGlobalChat = new GlobalChat();
        partialUpdatedGlobalChat.setId(globalChat.getId());

        restGlobalChatMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGlobalChat.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedGlobalChat))
            )
            .andExpect(status().isOk());

        // Validate the GlobalChat in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertGlobalChatUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedGlobalChat, globalChat),
            getPersistedGlobalChat(globalChat)
        );
    }

    @Test
    @Transactional
    void fullUpdateGlobalChatWithPatch() throws Exception {
        // Initialize the database
        insertedGlobalChat = globalChatRepository.saveAndFlush(globalChat);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the globalChat using partial update
        GlobalChat partialUpdatedGlobalChat = new GlobalChat();
        partialUpdatedGlobalChat.setId(globalChat.getId());

        partialUpdatedGlobalChat.name(UPDATED_NAME);

        restGlobalChatMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGlobalChat.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedGlobalChat))
            )
            .andExpect(status().isOk());

        // Validate the GlobalChat in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertGlobalChatUpdatableFieldsEquals(partialUpdatedGlobalChat, getPersistedGlobalChat(partialUpdatedGlobalChat));
    }

    @Test
    @Transactional
    void patchNonExistingGlobalChat() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        globalChat.setId(longCount.incrementAndGet());

        // Create the GlobalChat
        GlobalChatDTO globalChatDTO = globalChatMapper.toDto(globalChat);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGlobalChatMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, globalChatDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(globalChatDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the GlobalChat in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchGlobalChat() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        globalChat.setId(longCount.incrementAndGet());

        // Create the GlobalChat
        GlobalChatDTO globalChatDTO = globalChatMapper.toDto(globalChat);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGlobalChatMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(globalChatDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the GlobalChat in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamGlobalChat() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        globalChat.setId(longCount.incrementAndGet());

        // Create the GlobalChat
        GlobalChatDTO globalChatDTO = globalChatMapper.toDto(globalChat);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGlobalChatMockMvc
            .perform(
                patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(globalChatDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the GlobalChat in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteGlobalChat() throws Exception {
        // Initialize the database
        insertedGlobalChat = globalChatRepository.saveAndFlush(globalChat);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the globalChat
        restGlobalChatMockMvc
            .perform(delete(ENTITY_API_URL_ID, globalChat.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return globalChatRepository.count();
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

    protected GlobalChat getPersistedGlobalChat(GlobalChat globalChat) {
        return globalChatRepository.findById(globalChat.getId()).orElseThrow();
    }

    protected void assertPersistedGlobalChatToMatchAllProperties(GlobalChat expectedGlobalChat) {
        assertGlobalChatAllPropertiesEquals(expectedGlobalChat, getPersistedGlobalChat(expectedGlobalChat));
    }

    protected void assertPersistedGlobalChatToMatchUpdatableProperties(GlobalChat expectedGlobalChat) {
        assertGlobalChatAllUpdatablePropertiesEquals(expectedGlobalChat, getPersistedGlobalChat(expectedGlobalChat));
    }
}
