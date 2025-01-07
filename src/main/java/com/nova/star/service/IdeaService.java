package com.nova.star.service;

import com.nova.star.domain.Idea;
import com.nova.star.repository.IdeaRepository;
import com.nova.star.service.dto.IdeaDTO;
import com.nova.star.service.mapper.IdeaMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.nova.star.domain.Idea}.
 */
@Service
@Transactional
public class IdeaService {

    private static final Logger LOG = LoggerFactory.getLogger(IdeaService.class);

    private final IdeaRepository ideaRepository;

    private final IdeaMapper ideaMapper;

    public IdeaService(IdeaRepository ideaRepository, IdeaMapper ideaMapper) {
        this.ideaRepository = ideaRepository;
        this.ideaMapper = ideaMapper;
    }

    /**
     * Save a idea.
     *
     * @param ideaDTO the entity to save.
     * @return the persisted entity.
     */
    public IdeaDTO save(IdeaDTO ideaDTO) {
        LOG.debug("Request to save Idea : {}", ideaDTO);
        Idea idea = ideaMapper.toEntity(ideaDTO);
        idea = ideaRepository.save(idea);
        return ideaMapper.toDto(idea);
    }

    /**
     * Update a idea.
     *
     * @param ideaDTO the entity to save.
     * @return the persisted entity.
     */
    public IdeaDTO update(IdeaDTO ideaDTO) {
        LOG.debug("Request to update Idea : {}", ideaDTO);
        Idea idea = ideaMapper.toEntity(ideaDTO);
        idea = ideaRepository.save(idea);
        return ideaMapper.toDto(idea);
    }

    /**
     * Partially update a idea.
     *
     * @param ideaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<IdeaDTO> partialUpdate(IdeaDTO ideaDTO) {
        LOG.debug("Request to partially update Idea : {}", ideaDTO);

        return ideaRepository
            .findById(ideaDTO.getId())
            .map(existingIdea -> {
                ideaMapper.partialUpdate(existingIdea, ideaDTO);

                return existingIdea;
            })
            .map(ideaRepository::save)
            .map(ideaMapper::toDto);
    }

    /**
     * Get all the ideas.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<IdeaDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Ideas");
        return ideaRepository.findAll(pageable).map(ideaMapper::toDto);
    }

    /**
     * Get one idea by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<IdeaDTO> findOne(Long id) {
        LOG.debug("Request to get Idea : {}", id);
        return ideaRepository.findById(id).map(ideaMapper::toDto);
    }

    /**
     * Delete the idea by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Idea : {}", id);
        ideaRepository.deleteById(id);
    }
}
