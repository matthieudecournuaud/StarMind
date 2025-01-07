package com.nova.star.service;

import com.nova.star.domain.LikeHistory;
import com.nova.star.repository.LikeHistoryRepository;
import com.nova.star.service.dto.LikeHistoryDTO;
import com.nova.star.service.mapper.LikeHistoryMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.nova.star.domain.LikeHistory}.
 */
@Service
@Transactional
public class LikeHistoryService {

    private static final Logger LOG = LoggerFactory.getLogger(LikeHistoryService.class);

    private final LikeHistoryRepository likeHistoryRepository;

    private final LikeHistoryMapper likeHistoryMapper;

    public LikeHistoryService(LikeHistoryRepository likeHistoryRepository, LikeHistoryMapper likeHistoryMapper) {
        this.likeHistoryRepository = likeHistoryRepository;
        this.likeHistoryMapper = likeHistoryMapper;
    }

    /**
     * Save a likeHistory.
     *
     * @param likeHistoryDTO the entity to save.
     * @return the persisted entity.
     */
    public LikeHistoryDTO save(LikeHistoryDTO likeHistoryDTO) {
        LOG.debug("Request to save LikeHistory : {}", likeHistoryDTO);
        LikeHistory likeHistory = likeHistoryMapper.toEntity(likeHistoryDTO);
        likeHistory = likeHistoryRepository.save(likeHistory);
        return likeHistoryMapper.toDto(likeHistory);
    }

    /**
     * Update a likeHistory.
     *
     * @param likeHistoryDTO the entity to save.
     * @return the persisted entity.
     */
    public LikeHistoryDTO update(LikeHistoryDTO likeHistoryDTO) {
        LOG.debug("Request to update LikeHistory : {}", likeHistoryDTO);
        LikeHistory likeHistory = likeHistoryMapper.toEntity(likeHistoryDTO);
        likeHistory = likeHistoryRepository.save(likeHistory);
        return likeHistoryMapper.toDto(likeHistory);
    }

    /**
     * Partially update a likeHistory.
     *
     * @param likeHistoryDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<LikeHistoryDTO> partialUpdate(LikeHistoryDTO likeHistoryDTO) {
        LOG.debug("Request to partially update LikeHistory : {}", likeHistoryDTO);

        return likeHistoryRepository
            .findById(likeHistoryDTO.getId())
            .map(existingLikeHistory -> {
                likeHistoryMapper.partialUpdate(existingLikeHistory, likeHistoryDTO);

                return existingLikeHistory;
            })
            .map(likeHistoryRepository::save)
            .map(likeHistoryMapper::toDto);
    }

    /**
     * Get all the likeHistories.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<LikeHistoryDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all LikeHistories");
        return likeHistoryRepository.findAll(pageable).map(likeHistoryMapper::toDto);
    }

    /**
     * Get one likeHistory by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<LikeHistoryDTO> findOne(Long id) {
        LOG.debug("Request to get LikeHistory : {}", id);
        return likeHistoryRepository.findById(id).map(likeHistoryMapper::toDto);
    }

    /**
     * Delete the likeHistory by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete LikeHistory : {}", id);
        likeHistoryRepository.deleteById(id);
    }
}
