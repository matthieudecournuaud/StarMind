package com.nova.star.service;

import com.nova.star.domain.ProspectBoard;
import com.nova.star.repository.ProspectBoardRepository;
import com.nova.star.service.dto.ProspectBoardDTO;
import com.nova.star.service.mapper.ProspectBoardMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.nova.star.domain.ProspectBoard}.
 */
@Service
@Transactional
public class ProspectBoardService {

    private static final Logger LOG = LoggerFactory.getLogger(ProspectBoardService.class);

    private final ProspectBoardRepository prospectBoardRepository;

    private final ProspectBoardMapper prospectBoardMapper;

    public ProspectBoardService(ProspectBoardRepository prospectBoardRepository, ProspectBoardMapper prospectBoardMapper) {
        this.prospectBoardRepository = prospectBoardRepository;
        this.prospectBoardMapper = prospectBoardMapper;
    }

    /**
     * Save a prospectBoard.
     *
     * @param prospectBoardDTO the entity to save.
     * @return the persisted entity.
     */
    public ProspectBoardDTO save(ProspectBoardDTO prospectBoardDTO) {
        LOG.debug("Request to save ProspectBoard : {}", prospectBoardDTO);
        ProspectBoard prospectBoard = prospectBoardMapper.toEntity(prospectBoardDTO);
        prospectBoard = prospectBoardRepository.save(prospectBoard);
        return prospectBoardMapper.toDto(prospectBoard);
    }

    /**
     * Update a prospectBoard.
     *
     * @param prospectBoardDTO the entity to save.
     * @return the persisted entity.
     */
    public ProspectBoardDTO update(ProspectBoardDTO prospectBoardDTO) {
        LOG.debug("Request to update ProspectBoard : {}", prospectBoardDTO);
        ProspectBoard prospectBoard = prospectBoardMapper.toEntity(prospectBoardDTO);
        prospectBoard = prospectBoardRepository.save(prospectBoard);
        return prospectBoardMapper.toDto(prospectBoard);
    }

    /**
     * Partially update a prospectBoard.
     *
     * @param prospectBoardDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ProspectBoardDTO> partialUpdate(ProspectBoardDTO prospectBoardDTO) {
        LOG.debug("Request to partially update ProspectBoard : {}", prospectBoardDTO);

        return prospectBoardRepository
            .findById(prospectBoardDTO.getId())
            .map(existingProspectBoard -> {
                prospectBoardMapper.partialUpdate(existingProspectBoard, prospectBoardDTO);

                return existingProspectBoard;
            })
            .map(prospectBoardRepository::save)
            .map(prospectBoardMapper::toDto);
    }

    /**
     * Get all the prospectBoards.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ProspectBoardDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all ProspectBoards");
        return prospectBoardRepository.findAll(pageable).map(prospectBoardMapper::toDto);
    }

    /**
     * Get one prospectBoard by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ProspectBoardDTO> findOne(Long id) {
        LOG.debug("Request to get ProspectBoard : {}", id);
        return prospectBoardRepository.findById(id).map(prospectBoardMapper::toDto);
    }

    /**
     * Delete the prospectBoard by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ProspectBoard : {}", id);
        prospectBoardRepository.deleteById(id);
    }
}
