package com.nova.star.service;

import com.nova.star.domain.IdeaChat;
import com.nova.star.repository.IdeaChatRepository;
import com.nova.star.service.dto.IdeaChatDTO;
import com.nova.star.service.mapper.IdeaChatMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.nova.star.domain.IdeaChat}.
 */
@Service
@Transactional
public class IdeaChatService {

    private static final Logger LOG = LoggerFactory.getLogger(IdeaChatService.class);

    private final IdeaChatRepository ideaChatRepository;

    private final IdeaChatMapper ideaChatMapper;

    public IdeaChatService(IdeaChatRepository ideaChatRepository, IdeaChatMapper ideaChatMapper) {
        this.ideaChatRepository = ideaChatRepository;
        this.ideaChatMapper = ideaChatMapper;
    }

    /**
     * Save a ideaChat.
     *
     * @param ideaChatDTO the entity to save.
     * @return the persisted entity.
     */
    public IdeaChatDTO save(IdeaChatDTO ideaChatDTO) {
        LOG.debug("Request to save IdeaChat : {}", ideaChatDTO);
        IdeaChat ideaChat = ideaChatMapper.toEntity(ideaChatDTO);
        ideaChat = ideaChatRepository.save(ideaChat);
        return ideaChatMapper.toDto(ideaChat);
    }

    /**
     * Update a ideaChat.
     *
     * @param ideaChatDTO the entity to save.
     * @return the persisted entity.
     */
    public IdeaChatDTO update(IdeaChatDTO ideaChatDTO) {
        LOG.debug("Request to update IdeaChat : {}", ideaChatDTO);
        IdeaChat ideaChat = ideaChatMapper.toEntity(ideaChatDTO);
        ideaChat = ideaChatRepository.save(ideaChat);
        return ideaChatMapper.toDto(ideaChat);
    }

    /**
     * Partially update a ideaChat.
     *
     * @param ideaChatDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<IdeaChatDTO> partialUpdate(IdeaChatDTO ideaChatDTO) {
        LOG.debug("Request to partially update IdeaChat : {}", ideaChatDTO);

        return ideaChatRepository
            .findById(ideaChatDTO.getId())
            .map(existingIdeaChat -> {
                ideaChatMapper.partialUpdate(existingIdeaChat, ideaChatDTO);

                return existingIdeaChat;
            })
            .map(ideaChatRepository::save)
            .map(ideaChatMapper::toDto);
    }

    /**
     * Get all the ideaChats.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<IdeaChatDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all IdeaChats");
        return ideaChatRepository.findAll(pageable).map(ideaChatMapper::toDto);
    }

    /**
     * Get all the ideaChats with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<IdeaChatDTO> findAllWithEagerRelationships(Pageable pageable) {
        return ideaChatRepository.findAllWithEagerRelationships(pageable).map(ideaChatMapper::toDto);
    }

    /**
     * Get one ideaChat by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<IdeaChatDTO> findOne(Long id) {
        LOG.debug("Request to get IdeaChat : {}", id);
        return ideaChatRepository.findOneWithEagerRelationships(id).map(ideaChatMapper::toDto);
    }

    /**
     * Delete the ideaChat by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete IdeaChat : {}", id);
        ideaChatRepository.deleteById(id);
    }
}
