package com.nova.star.service;

import com.nova.star.domain.GlobalChat;
import com.nova.star.repository.GlobalChatRepository;
import com.nova.star.service.dto.GlobalChatDTO;
import com.nova.star.service.mapper.GlobalChatMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.nova.star.domain.GlobalChat}.
 */
@Service
@Transactional
public class GlobalChatService {

    private static final Logger LOG = LoggerFactory.getLogger(GlobalChatService.class);

    private final GlobalChatRepository globalChatRepository;

    private final GlobalChatMapper globalChatMapper;

    public GlobalChatService(GlobalChatRepository globalChatRepository, GlobalChatMapper globalChatMapper) {
        this.globalChatRepository = globalChatRepository;
        this.globalChatMapper = globalChatMapper;
    }

    /**
     * Save a globalChat.
     *
     * @param globalChatDTO the entity to save.
     * @return the persisted entity.
     */
    public GlobalChatDTO save(GlobalChatDTO globalChatDTO) {
        LOG.debug("Request to save GlobalChat : {}", globalChatDTO);
        GlobalChat globalChat = globalChatMapper.toEntity(globalChatDTO);
        globalChat = globalChatRepository.save(globalChat);
        return globalChatMapper.toDto(globalChat);
    }

    /**
     * Update a globalChat.
     *
     * @param globalChatDTO the entity to save.
     * @return the persisted entity.
     */
    public GlobalChatDTO update(GlobalChatDTO globalChatDTO) {
        LOG.debug("Request to update GlobalChat : {}", globalChatDTO);
        GlobalChat globalChat = globalChatMapper.toEntity(globalChatDTO);
        globalChat = globalChatRepository.save(globalChat);
        return globalChatMapper.toDto(globalChat);
    }

    /**
     * Partially update a globalChat.
     *
     * @param globalChatDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<GlobalChatDTO> partialUpdate(GlobalChatDTO globalChatDTO) {
        LOG.debug("Request to partially update GlobalChat : {}", globalChatDTO);

        return globalChatRepository
            .findById(globalChatDTO.getId())
            .map(existingGlobalChat -> {
                globalChatMapper.partialUpdate(existingGlobalChat, globalChatDTO);

                return existingGlobalChat;
            })
            .map(globalChatRepository::save)
            .map(globalChatMapper::toDto);
    }

    /**
     * Get all the globalChats.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<GlobalChatDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all GlobalChats");
        return globalChatRepository.findAll(pageable).map(globalChatMapper::toDto);
    }

    /**
     * Get all the globalChats with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<GlobalChatDTO> findAllWithEagerRelationships(Pageable pageable) {
        return globalChatRepository.findAllWithEagerRelationships(pageable).map(globalChatMapper::toDto);
    }

    /**
     * Get one globalChat by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<GlobalChatDTO> findOne(Long id) {
        LOG.debug("Request to get GlobalChat : {}", id);
        return globalChatRepository.findOneWithEagerRelationships(id).map(globalChatMapper::toDto);
    }

    /**
     * Delete the globalChat by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete GlobalChat : {}", id);
        globalChatRepository.deleteById(id);
    }
}
