package com.nova.star.web.rest;

import com.nova.star.repository.RewardHistoryRepository;
import com.nova.star.service.RewardHistoryService;
import com.nova.star.service.dto.RewardHistoryDTO;
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
 * REST controller for managing {@link com.nova.star.domain.RewardHistory}.
 */
@RestController
@RequestMapping("/api/reward-histories")
public class RewardHistoryResource {

    private static final Logger LOG = LoggerFactory.getLogger(RewardHistoryResource.class);

    private static final String ENTITY_NAME = "rewardHistory";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RewardHistoryService rewardHistoryService;

    private final RewardHistoryRepository rewardHistoryRepository;

    public RewardHistoryResource(RewardHistoryService rewardHistoryService, RewardHistoryRepository rewardHistoryRepository) {
        this.rewardHistoryService = rewardHistoryService;
        this.rewardHistoryRepository = rewardHistoryRepository;
    }

    /**
     * {@code POST  /reward-histories} : Create a new rewardHistory.
     *
     * @param rewardHistoryDTO the rewardHistoryDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new rewardHistoryDTO, or with status {@code 400 (Bad Request)} if the rewardHistory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<RewardHistoryDTO> createRewardHistory(@Valid @RequestBody RewardHistoryDTO rewardHistoryDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save RewardHistory : {}", rewardHistoryDTO);
        if (rewardHistoryDTO.getId() != null) {
            throw new BadRequestAlertException("A new rewardHistory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        rewardHistoryDTO = rewardHistoryService.save(rewardHistoryDTO);
        return ResponseEntity.created(new URI("/api/reward-histories/" + rewardHistoryDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, rewardHistoryDTO.getId().toString()))
            .body(rewardHistoryDTO);
    }

    /**
     * {@code PUT  /reward-histories/:id} : Updates an existing rewardHistory.
     *
     * @param id the id of the rewardHistoryDTO to save.
     * @param rewardHistoryDTO the rewardHistoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rewardHistoryDTO,
     * or with status {@code 400 (Bad Request)} if the rewardHistoryDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the rewardHistoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<RewardHistoryDTO> updateRewardHistory(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody RewardHistoryDTO rewardHistoryDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update RewardHistory : {}, {}", id, rewardHistoryDTO);
        if (rewardHistoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, rewardHistoryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!rewardHistoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        rewardHistoryDTO = rewardHistoryService.update(rewardHistoryDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, rewardHistoryDTO.getId().toString()))
            .body(rewardHistoryDTO);
    }

    /**
     * {@code PATCH  /reward-histories/:id} : Partial updates given fields of an existing rewardHistory, field will ignore if it is null
     *
     * @param id the id of the rewardHistoryDTO to save.
     * @param rewardHistoryDTO the rewardHistoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rewardHistoryDTO,
     * or with status {@code 400 (Bad Request)} if the rewardHistoryDTO is not valid,
     * or with status {@code 404 (Not Found)} if the rewardHistoryDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the rewardHistoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<RewardHistoryDTO> partialUpdateRewardHistory(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody RewardHistoryDTO rewardHistoryDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update RewardHistory partially : {}, {}", id, rewardHistoryDTO);
        if (rewardHistoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, rewardHistoryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!rewardHistoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RewardHistoryDTO> result = rewardHistoryService.partialUpdate(rewardHistoryDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, rewardHistoryDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /reward-histories} : get all the rewardHistories.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of rewardHistories in body.
     */
    @GetMapping("")
    public ResponseEntity<List<RewardHistoryDTO>> getAllRewardHistories(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of RewardHistories");
        Page<RewardHistoryDTO> page = rewardHistoryService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /reward-histories/:id} : get the "id" rewardHistory.
     *
     * @param id the id of the rewardHistoryDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the rewardHistoryDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<RewardHistoryDTO> getRewardHistory(@PathVariable("id") Long id) {
        LOG.debug("REST request to get RewardHistory : {}", id);
        Optional<RewardHistoryDTO> rewardHistoryDTO = rewardHistoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(rewardHistoryDTO);
    }

    /**
     * {@code DELETE  /reward-histories/:id} : delete the "id" rewardHistory.
     *
     * @param id the id of the rewardHistoryDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRewardHistory(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete RewardHistory : {}", id);
        rewardHistoryService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
