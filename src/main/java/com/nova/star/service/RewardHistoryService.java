package com.nova.star.service;

import com.nova.star.domain.RewardHistory;
import com.nova.star.repository.RewardHistoryRepository;
import com.nova.star.service.dto.RewardHistoryDTO;
import com.nova.star.service.mapper.RewardHistoryMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.nova.star.domain.RewardHistory}.
 */
@Service
@Transactional
public class RewardHistoryService {

    private static final Logger LOG = LoggerFactory.getLogger(RewardHistoryService.class);

    private final RewardHistoryRepository rewardHistoryRepository;

    private final RewardHistoryMapper rewardHistoryMapper;

    public RewardHistoryService(RewardHistoryRepository rewardHistoryRepository, RewardHistoryMapper rewardHistoryMapper) {
        this.rewardHistoryRepository = rewardHistoryRepository;
        this.rewardHistoryMapper = rewardHistoryMapper;
    }

    /**
     * Save a rewardHistory.
     *
     * @param rewardHistoryDTO the entity to save.
     * @return the persisted entity.
     */
    public RewardHistoryDTO save(RewardHistoryDTO rewardHistoryDTO) {
        LOG.debug("Request to save RewardHistory : {}", rewardHistoryDTO);
        RewardHistory rewardHistory = rewardHistoryMapper.toEntity(rewardHistoryDTO);
        rewardHistory = rewardHistoryRepository.save(rewardHistory);
        return rewardHistoryMapper.toDto(rewardHistory);
    }

    /**
     * Update a rewardHistory.
     *
     * @param rewardHistoryDTO the entity to save.
     * @return the persisted entity.
     */
    public RewardHistoryDTO update(RewardHistoryDTO rewardHistoryDTO) {
        LOG.debug("Request to update RewardHistory : {}", rewardHistoryDTO);
        RewardHistory rewardHistory = rewardHistoryMapper.toEntity(rewardHistoryDTO);
        rewardHistory = rewardHistoryRepository.save(rewardHistory);
        return rewardHistoryMapper.toDto(rewardHistory);
    }

    /**
     * Partially update a rewardHistory.
     *
     * @param rewardHistoryDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<RewardHistoryDTO> partialUpdate(RewardHistoryDTO rewardHistoryDTO) {
        LOG.debug("Request to partially update RewardHistory : {}", rewardHistoryDTO);

        return rewardHistoryRepository
            .findById(rewardHistoryDTO.getId())
            .map(existingRewardHistory -> {
                rewardHistoryMapper.partialUpdate(existingRewardHistory, rewardHistoryDTO);

                return existingRewardHistory;
            })
            .map(rewardHistoryRepository::save)
            .map(rewardHistoryMapper::toDto);
    }

    /**
     * Get all the rewardHistories.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<RewardHistoryDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all RewardHistories");
        return rewardHistoryRepository.findAll(pageable).map(rewardHistoryMapper::toDto);
    }

    /**
     * Get one rewardHistory by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<RewardHistoryDTO> findOne(Long id) {
        LOG.debug("Request to get RewardHistory : {}", id);
        return rewardHistoryRepository.findById(id).map(rewardHistoryMapper::toDto);
    }

    /**
     * Delete the rewardHistory by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete RewardHistory : {}", id);
        rewardHistoryRepository.deleteById(id);
    }
}
