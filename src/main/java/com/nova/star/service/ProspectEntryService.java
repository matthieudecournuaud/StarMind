package com.nova.star.service;

import com.nova.star.domain.ProspectEntry;
import com.nova.star.repository.ProspectEntryRepository;
import com.nova.star.service.dto.ProspectEntryDTO;
import com.nova.star.service.mapper.ProspectEntryMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.nova.star.domain.ProspectEntry}.
 */
@Service
@Transactional
public class ProspectEntryService {

    private static final Logger LOG = LoggerFactory.getLogger(ProspectEntryService.class);

    private final ProspectEntryRepository prospectEntryRepository;

    private final ProspectEntryMapper prospectEntryMapper;

    public ProspectEntryService(ProspectEntryRepository prospectEntryRepository, ProspectEntryMapper prospectEntryMapper) {
        this.prospectEntryRepository = prospectEntryRepository;
        this.prospectEntryMapper = prospectEntryMapper;
    }

    /**
     * Save a prospectEntry.
     *
     * @param prospectEntryDTO the entity to save.
     * @return the persisted entity.
     */
    public ProspectEntryDTO save(ProspectEntryDTO prospectEntryDTO) {
        LOG.debug("Request to save ProspectEntry : {}", prospectEntryDTO);
        ProspectEntry prospectEntry = prospectEntryMapper.toEntity(prospectEntryDTO);
        prospectEntry = prospectEntryRepository.save(prospectEntry);
        return prospectEntryMapper.toDto(prospectEntry);
    }

    /**
     * Update a prospectEntry.
     *
     * @param prospectEntryDTO the entity to save.
     * @return the persisted entity.
     */
    public ProspectEntryDTO update(ProspectEntryDTO prospectEntryDTO) {
        LOG.debug("Request to update ProspectEntry : {}", prospectEntryDTO);
        ProspectEntry prospectEntry = prospectEntryMapper.toEntity(prospectEntryDTO);
        prospectEntry = prospectEntryRepository.save(prospectEntry);
        return prospectEntryMapper.toDto(prospectEntry);
    }

    /**
     * Partially update a prospectEntry.
     *
     * @param prospectEntryDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ProspectEntryDTO> partialUpdate(ProspectEntryDTO prospectEntryDTO) {
        LOG.debug("Request to partially update ProspectEntry : {}", prospectEntryDTO);

        return prospectEntryRepository
            .findById(prospectEntryDTO.getId())
            .map(existingProspectEntry -> {
                prospectEntryMapper.partialUpdate(existingProspectEntry, prospectEntryDTO);

                return existingProspectEntry;
            })
            .map(prospectEntryRepository::save)
            .map(prospectEntryMapper::toDto);
    }

    /**
     * Get all the prospectEntries.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ProspectEntryDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all ProspectEntries");
        return prospectEntryRepository.findAll(pageable).map(prospectEntryMapper::toDto);
    }

    /**
     * Get one prospectEntry by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ProspectEntryDTO> findOne(Long id) {
        LOG.debug("Request to get ProspectEntry : {}", id);
        return prospectEntryRepository.findById(id).map(prospectEntryMapper::toDto);
    }

    /**
     * Delete the prospectEntry by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ProspectEntry : {}", id);
        prospectEntryRepository.deleteById(id);
    }
}
