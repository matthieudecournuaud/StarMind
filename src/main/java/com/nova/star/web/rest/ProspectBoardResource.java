package com.nova.star.web.rest;

import com.nova.star.repository.ProspectBoardRepository;
import com.nova.star.service.ProspectBoardService;
import com.nova.star.service.dto.ProspectBoardDTO;
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
 * REST controller for managing {@link com.nova.star.domain.ProspectBoard}.
 */
@RestController
@RequestMapping("/api/prospect-boards")
public class ProspectBoardResource {

    private static final Logger LOG = LoggerFactory.getLogger(ProspectBoardResource.class);

    private static final String ENTITY_NAME = "prospectBoard";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProspectBoardService prospectBoardService;

    private final ProspectBoardRepository prospectBoardRepository;

    public ProspectBoardResource(ProspectBoardService prospectBoardService, ProspectBoardRepository prospectBoardRepository) {
        this.prospectBoardService = prospectBoardService;
        this.prospectBoardRepository = prospectBoardRepository;
    }

    /**
     * {@code POST  /prospect-boards} : Create a new prospectBoard.
     *
     * @param prospectBoardDTO the prospectBoardDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new prospectBoardDTO, or with status {@code 400 (Bad Request)} if the prospectBoard has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ProspectBoardDTO> createProspectBoard(@Valid @RequestBody ProspectBoardDTO prospectBoardDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save ProspectBoard : {}", prospectBoardDTO);
        if (prospectBoardDTO.getId() != null) {
            throw new BadRequestAlertException("A new prospectBoard cannot already have an ID", ENTITY_NAME, "idexists");
        }
        prospectBoardDTO = prospectBoardService.save(prospectBoardDTO);
        return ResponseEntity.created(new URI("/api/prospect-boards/" + prospectBoardDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, prospectBoardDTO.getId().toString()))
            .body(prospectBoardDTO);
    }

    /**
     * {@code PUT  /prospect-boards/:id} : Updates an existing prospectBoard.
     *
     * @param id the id of the prospectBoardDTO to save.
     * @param prospectBoardDTO the prospectBoardDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated prospectBoardDTO,
     * or with status {@code 400 (Bad Request)} if the prospectBoardDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the prospectBoardDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProspectBoardDTO> updateProspectBoard(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ProspectBoardDTO prospectBoardDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ProspectBoard : {}, {}", id, prospectBoardDTO);
        if (prospectBoardDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, prospectBoardDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!prospectBoardRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        prospectBoardDTO = prospectBoardService.update(prospectBoardDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, prospectBoardDTO.getId().toString()))
            .body(prospectBoardDTO);
    }

    /**
     * {@code PATCH  /prospect-boards/:id} : Partial updates given fields of an existing prospectBoard, field will ignore if it is null
     *
     * @param id the id of the prospectBoardDTO to save.
     * @param prospectBoardDTO the prospectBoardDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated prospectBoardDTO,
     * or with status {@code 400 (Bad Request)} if the prospectBoardDTO is not valid,
     * or with status {@code 404 (Not Found)} if the prospectBoardDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the prospectBoardDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProspectBoardDTO> partialUpdateProspectBoard(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ProspectBoardDTO prospectBoardDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ProspectBoard partially : {}, {}", id, prospectBoardDTO);
        if (prospectBoardDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, prospectBoardDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!prospectBoardRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProspectBoardDTO> result = prospectBoardService.partialUpdate(prospectBoardDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, prospectBoardDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /prospect-boards} : get all the prospectBoards.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of prospectBoards in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ProspectBoardDTO>> getAllProspectBoards(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of ProspectBoards");
        Page<ProspectBoardDTO> page = prospectBoardService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /prospect-boards/:id} : get the "id" prospectBoard.
     *
     * @param id the id of the prospectBoardDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the prospectBoardDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProspectBoardDTO> getProspectBoard(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ProspectBoard : {}", id);
        Optional<ProspectBoardDTO> prospectBoardDTO = prospectBoardService.findOne(id);
        return ResponseUtil.wrapOrNotFound(prospectBoardDTO);
    }

    /**
     * {@code DELETE  /prospect-boards/:id} : delete the "id" prospectBoard.
     *
     * @param id the id of the prospectBoardDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProspectBoard(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ProspectBoard : {}", id);
        prospectBoardService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
