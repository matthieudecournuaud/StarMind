package com.nova.star.service;

import com.nova.star.domain.Vote;
import com.nova.star.repository.VoteRepository;
import com.nova.star.service.dto.VoteDTO;
import com.nova.star.service.mapper.VoteMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.nova.star.domain.Vote}.
 */
@Service
@Transactional
public class VoteService {

    private static final Logger LOG = LoggerFactory.getLogger(VoteService.class);

    private final VoteRepository voteRepository;

    private final VoteMapper voteMapper;

    public VoteService(VoteRepository voteRepository, VoteMapper voteMapper) {
        this.voteRepository = voteRepository;
        this.voteMapper = voteMapper;
    }

    /**
     * Save a vote.
     *
     * @param voteDTO the entity to save.
     * @return the persisted entity.
     */
    public VoteDTO save(VoteDTO voteDTO) {
        LOG.debug("Request to save Vote : {}", voteDTO);
        Vote vote = voteMapper.toEntity(voteDTO);
        vote = voteRepository.save(vote);
        return voteMapper.toDto(vote);
    }

    /**
     * Update a vote.
     *
     * @param voteDTO the entity to save.
     * @return the persisted entity.
     */
    public VoteDTO update(VoteDTO voteDTO) {
        LOG.debug("Request to update Vote : {}", voteDTO);
        Vote vote = voteMapper.toEntity(voteDTO);
        vote = voteRepository.save(vote);
        return voteMapper.toDto(vote);
    }

    /**
     * Partially update a vote.
     *
     * @param voteDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<VoteDTO> partialUpdate(VoteDTO voteDTO) {
        LOG.debug("Request to partially update Vote : {}", voteDTO);

        return voteRepository
            .findById(voteDTO.getId())
            .map(existingVote -> {
                voteMapper.partialUpdate(existingVote, voteDTO);

                return existingVote;
            })
            .map(voteRepository::save)
            .map(voteMapper::toDto);
    }

    /**
     * Get all the votes.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<VoteDTO> findAll() {
        LOG.debug("Request to get all Votes");
        return voteRepository.findAll().stream().map(voteMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one vote by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<VoteDTO> findOne(Long id) {
        LOG.debug("Request to get Vote : {}", id);
        return voteRepository.findById(id).map(voteMapper::toDto);
    }

    /**
     * Delete the vote by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Vote : {}", id);
        voteRepository.deleteById(id);
    }
}
