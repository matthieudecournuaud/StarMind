package com.nova.star.web.rest;

import com.nova.star.repository.IdeaHistoryRepository;
import com.nova.star.service.IdeaHistoryService;
import com.nova.star.service.dto.IdeaHistoryDTO;
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
 * REST controller for managing {@link com.nova.star.domain.IdeaHistory}.
 */
@RestController
@RequestMapping("/api/idea-histories")
public class IdeaHistoryResource {

    private static final Logger LOG = LoggerFactory.getLogger(IdeaHistoryResource.class);

    private static final String ENTITY_NAME = "ideaHistory";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final IdeaHistoryService ideaHistoryService;

    private final IdeaHistoryRepository ideaHistoryRepository;

    public IdeaHistoryResource(IdeaHistoryService ideaHistoryService, IdeaHistoryRepository ideaHistoryRepository) {
        this.ideaHistoryService = ideaHistoryService;
        this.ideaHistoryRepository = ideaHistoryRepository;
    }

    /**
     * {@code POST  /idea-histories} : Create a new ideaHistory.
     *
     * @param ideaHistoryDTO the ideaHistoryDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ideaHistoryDTO, or with status {@code 400 (Bad Request)} if the ideaHistory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<IdeaHistoryDTO> createIdeaHistory(@Valid @RequestBody IdeaHistoryDTO ideaHistoryDTO) throws URISyntaxException {
        LOG.debug("REST request to save IdeaHistory : {}", ideaHistoryDTO);
        if (ideaHistoryDTO.getId() != null) {
            throw new BadRequestAlertException("A new ideaHistory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ideaHistoryDTO = ideaHistoryService.save(ideaHistoryDTO);
        return ResponseEntity.created(new URI("/api/idea-histories/" + ideaHistoryDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, ideaHistoryDTO.getId().toString()))
            .body(ideaHistoryDTO);
    }

    /**
     * {@code PUT  /idea-histories/:id} : Updates an existing ideaHistory.
     *
     * @param id the id of the ideaHistoryDTO to save.
     * @param ideaHistoryDTO the ideaHistoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ideaHistoryDTO,
     * or with status {@code 400 (Bad Request)} if the ideaHistoryDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ideaHistoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<IdeaHistoryDTO> updateIdeaHistory(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody IdeaHistoryDTO ideaHistoryDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update IdeaHistory : {}, {}", id, ideaHistoryDTO);
        if (ideaHistoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ideaHistoryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ideaHistoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ideaHistoryDTO = ideaHistoryService.update(ideaHistoryDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ideaHistoryDTO.getId().toString()))
            .body(ideaHistoryDTO);
    }

    /**
     * {@code PATCH  /idea-histories/:id} : Partial updates given fields of an existing ideaHistory, field will ignore if it is null
     *
     * @param id the id of the ideaHistoryDTO to save.
     * @param ideaHistoryDTO the ideaHistoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ideaHistoryDTO,
     * or with status {@code 400 (Bad Request)} if the ideaHistoryDTO is not valid,
     * or with status {@code 404 (Not Found)} if the ideaHistoryDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the ideaHistoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<IdeaHistoryDTO> partialUpdateIdeaHistory(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody IdeaHistoryDTO ideaHistoryDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update IdeaHistory partially : {}, {}", id, ideaHistoryDTO);
        if (ideaHistoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ideaHistoryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ideaHistoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<IdeaHistoryDTO> result = ideaHistoryService.partialUpdate(ideaHistoryDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ideaHistoryDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /idea-histories} : get all the ideaHistories.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ideaHistories in body.
     */
    @GetMapping("")
    public ResponseEntity<List<IdeaHistoryDTO>> getAllIdeaHistories(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of IdeaHistories");
        Page<IdeaHistoryDTO> page = ideaHistoryService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /idea-histories/:id} : get the "id" ideaHistory.
     *
     * @param id the id of the ideaHistoryDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ideaHistoryDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<IdeaHistoryDTO> getIdeaHistory(@PathVariable("id") Long id) {
        LOG.debug("REST request to get IdeaHistory : {}", id);
        Optional<IdeaHistoryDTO> ideaHistoryDTO = ideaHistoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(ideaHistoryDTO);
    }

    /**
     * {@code DELETE  /idea-histories/:id} : delete the "id" ideaHistory.
     *
     * @param id the id of the ideaHistoryDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIdeaHistory(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete IdeaHistory : {}", id);
        ideaHistoryService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
