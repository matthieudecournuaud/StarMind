package com.nova.star.web.rest;

import com.nova.star.repository.ProspectEntryRepository;
import com.nova.star.service.ProspectEntryService;
import com.nova.star.service.dto.ProspectEntryDTO;
import com.nova.star.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.nova.star.domain.ProspectEntry}.
 */
@RestController
@RequestMapping("/api/prospect-entries")
public class ProspectEntryResource {

    private static final Logger LOG = LoggerFactory.getLogger(ProspectEntryResource.class);

    private static final String ENTITY_NAME = "prospectEntry";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProspectEntryService prospectEntryService;

    private final ProspectEntryRepository prospectEntryRepository;

    public ProspectEntryResource(ProspectEntryService prospectEntryService, ProspectEntryRepository prospectEntryRepository) {
        this.prospectEntryService = prospectEntryService;
        this.prospectEntryRepository = prospectEntryRepository;
    }

    /**
     * {@code POST  /prospect-entries} : Create a new prospectEntry.
     *
     * @param prospectEntryDTO the prospectEntryDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new prospectEntryDTO, or with status {@code 400 (Bad Request)} if the prospectEntry has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ProspectEntryDTO> createProspectEntry(@Valid @RequestBody ProspectEntryDTO prospectEntryDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save ProspectEntry : {}", prospectEntryDTO);
        if (prospectEntryDTO.getId() != null) {
            throw new BadRequestAlertException("A new prospectEntry cannot already have an ID", ENTITY_NAME, "idexists");
        }
        prospectEntryDTO = prospectEntryService.save(prospectEntryDTO);
        return ResponseEntity.created(new URI("/api/prospect-entries/" + prospectEntryDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, prospectEntryDTO.getId().toString()))
            .body(prospectEntryDTO);
    }

    /**
     * {@code PUT  /prospect-entries/:id} : Updates an existing prospectEntry.
     *
     * @param id the id of the prospectEntryDTO to save.
     * @param prospectEntryDTO the prospectEntryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated prospectEntryDTO,
     * or with status {@code 400 (Bad Request)} if the prospectEntryDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the prospectEntryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProspectEntryDTO> updateProspectEntry(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ProspectEntryDTO prospectEntryDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ProspectEntry : {}, {}", id, prospectEntryDTO);
        if (prospectEntryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, prospectEntryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!prospectEntryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        prospectEntryDTO = prospectEntryService.update(prospectEntryDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, prospectEntryDTO.getId().toString()))
            .body(prospectEntryDTO);
    }

    /**
     * {@code PATCH  /prospect-entries/:id} : Partial updates given fields of an existing prospectEntry, field will ignore if it is null
     *
     * @param id the id of the prospectEntryDTO to save.
     * @param prospectEntryDTO the prospectEntryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated prospectEntryDTO,
     * or with status {@code 400 (Bad Request)} if the prospectEntryDTO is not valid,
     * or with status {@code 404 (Not Found)} if the prospectEntryDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the prospectEntryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProspectEntryDTO> partialUpdateProspectEntry(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ProspectEntryDTO prospectEntryDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ProspectEntry partially : {}, {}", id, prospectEntryDTO);
        if (prospectEntryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, prospectEntryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!prospectEntryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProspectEntryDTO> result = prospectEntryService.partialUpdate(prospectEntryDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, prospectEntryDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /prospect-entries} : get all the prospectEntries.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of prospectEntries in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ProspectEntryDTO>> getAllProspectEntries(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of ProspectEntries");
        Page<ProspectEntryDTO> page = prospectEntryService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /prospect-entries/:id} : get the "id" prospectEntry.
     *
     * @param id the id of the prospectEntryDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the prospectEntryDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProspectEntryDTO> getProspectEntry(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ProspectEntry : {}", id);
        Optional<ProspectEntryDTO> prospectEntryDTO = prospectEntryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(prospectEntryDTO);
    }

    /**
     * {@code DELETE  /prospect-entries/:id} : delete the "id" prospectEntry.
     *
     * @param id the id of the prospectEntryDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProspectEntry(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ProspectEntry : {}", id);
        prospectEntryService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
