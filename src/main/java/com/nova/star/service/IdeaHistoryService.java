package com.nova.star.service;

import com.nova.star.domain.IdeaHistory;
import com.nova.star.repository.IdeaHistoryRepository;
import com.nova.star.service.dto.IdeaHistoryDTO;
import com.nova.star.service.mapper.IdeaHistoryMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.nova.star.domain.IdeaHistory}.
 */
@Service
@Transactional
public class IdeaHistoryService {

    private static final Logger LOG = LoggerFactory.getLogger(IdeaHistoryService.class);

    private final IdeaHistoryRepository ideaHistoryRepository;

    private final IdeaHistoryMapper ideaHistoryMapper;

    public IdeaHistoryService(IdeaHistoryRepository ideaHistoryRepository, IdeaHistoryMapper ideaHistoryMapper) {
        this.ideaHistoryRepository = ideaHistoryRepository;
        this.ideaHistoryMapper = ideaHistoryMapper;
    }

    /**
     * Save a ideaHistory.
     *
     * @param ideaHistoryDTO the entity to save.
     * @return the persisted entity.
     */
    public IdeaHistoryDTO save(IdeaHistoryDTO ideaHistoryDTO) {
        LOG.debug("Request to save IdeaHistory : {}", ideaHistoryDTO);
        IdeaHistory ideaHistory = ideaHistoryMapper.toEntity(ideaHistoryDTO);
        ideaHistory = ideaHistoryRepository.save(ideaHistory);
        return ideaHistoryMapper.toDto(ideaHistory);
    }

    /**
     * Update a ideaHistory.
     *
     * @param ideaHistoryDTO the entity to save.
     * @return the persisted entity.
     */
    public IdeaHistoryDTO update(IdeaHistoryDTO ideaHistoryDTO) {
        LOG.debug("Request to update IdeaHistory : {}", ideaHistoryDTO);
        IdeaHistory ideaHistory = ideaHistoryMapper.toEntity(ideaHistoryDTO);
        ideaHistory = ideaHistoryRepository.save(ideaHistory);
        return ideaHistoryMapper.toDto(ideaHistory);
    }

    /**
     * Partially update a ideaHistory.
     *
     * @param ideaHistoryDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<IdeaHistoryDTO> partialUpdate(IdeaHistoryDTO ideaHistoryDTO) {
        LOG.debug("Request to partially update IdeaHistory : {}", ideaHistoryDTO);

        return ideaHistoryRepository
            .findById(ideaHistoryDTO.getId())
            .map(existingIdeaHistory -> {
                ideaHistoryMapper.partialUpdate(existingIdeaHistory, ideaHistoryDTO);

                return existingIdeaHistory;
            })
            .map(ideaHistoryRepository::save)
            .map(ideaHistoryMapper::toDto);
    }

    /**
     * Get all the ideaHistories.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<IdeaHistoryDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all IdeaHistories");
        return ideaHistoryRepository.findAll(pageable).map(ideaHistoryMapper::toDto);
    }

    /**
     * Get one ideaHistory by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<IdeaHistoryDTO> findOne(Long id) {
        LOG.debug("Request to get IdeaHistory : {}", id);
        return ideaHistoryRepository.findById(id).map(ideaHistoryMapper::toDto);
    }

    /**
     * Delete the ideaHistory by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete IdeaHistory : {}", id);
        ideaHistoryRepository.deleteById(id);
    }
}
