package com.nova.star.service;

import com.nova.star.domain.Reward;
import com.nova.star.repository.RewardRepository;
import com.nova.star.service.dto.RewardDTO;
import com.nova.star.service.mapper.RewardMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.nova.star.domain.Reward}.
 */
@Service
@Transactional
public class RewardService {

    private static final Logger LOG = LoggerFactory.getLogger(RewardService.class);

    private final RewardRepository rewardRepository;

    private final RewardMapper rewardMapper;

    public RewardService(RewardRepository rewardRepository, RewardMapper rewardMapper) {
        this.rewardRepository = rewardRepository;
        this.rewardMapper = rewardMapper;
    }

    /**
     * Save a reward.
     *
     * @param rewardDTO the entity to save.
     * @return the persisted entity.
     */
    public RewardDTO save(RewardDTO rewardDTO) {
        LOG.debug("Request to save Reward : {}", rewardDTO);
        Reward reward = rewardMapper.toEntity(rewardDTO);
        reward = rewardRepository.save(reward);
        return rewardMapper.toDto(reward);
    }

    /**
     * Update a reward.
     *
     * @param rewardDTO the entity to save.
     * @return the persisted entity.
     */
    public RewardDTO update(RewardDTO rewardDTO) {
        LOG.debug("Request to update Reward : {}", rewardDTO);
        Reward reward = rewardMapper.toEntity(rewardDTO);
        reward = rewardRepository.save(reward);
        return rewardMapper.toDto(reward);
    }

    /**
     * Partially update a reward.
     *
     * @param rewardDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<RewardDTO> partialUpdate(RewardDTO rewardDTO) {
        LOG.debug("Request to partially update Reward : {}", rewardDTO);

        return rewardRepository
            .findById(rewardDTO.getId())
            .map(existingReward -> {
                rewardMapper.partialUpdate(existingReward, rewardDTO);

                return existingReward;
            })
            .map(rewardRepository::save)
            .map(rewardMapper::toDto);
    }

    /**
     * Get all the rewards.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<RewardDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Rewards");
        return rewardRepository.findAll(pageable).map(rewardMapper::toDto);
    }

    /**
     * Get one reward by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<RewardDTO> findOne(Long id) {
        LOG.debug("Request to get Reward : {}", id);
        return rewardRepository.findById(id).map(rewardMapper::toDto);
    }

    /**
     * Delete the reward by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Reward : {}", id);
        rewardRepository.deleteById(id);
    }
}
