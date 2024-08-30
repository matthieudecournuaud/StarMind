package com.nova.star.web.rest;

import com.nova.star.domain.RewardHistory;
import com.nova.star.repository.RewardHistoryRepository;
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
import org.springframework.transaction.annotation.Transactional;
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
@Transactional
public class RewardHistoryResource {

    private static final Logger LOG = LoggerFactory.getLogger(RewardHistoryResource.class);

    private static final String ENTITY_NAME = "rewardHistory";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RewardHistoryRepository rewardHistoryRepository;

    public RewardHistoryResource(RewardHistoryRepository rewardHistoryRepository) {
        this.rewardHistoryRepository = rewardHistoryRepository;
    }

    /**
     * {@code POST  /reward-histories} : Create a new rewardHistory.
     *
     * @param rewardHistory the rewardHistory to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new rewardHistory, or with status {@code 400 (Bad Request)} if the rewardHistory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<RewardHistory> createRewardHistory(@Valid @RequestBody RewardHistory rewardHistory) throws URISyntaxException {
        LOG.debug("REST request to save RewardHistory : {}", rewardHistory);
        if (rewardHistory.getId() != null) {
            throw new BadRequestAlertException("A new rewardHistory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        rewardHistory = rewardHistoryRepository.save(rewardHistory);
        return ResponseEntity.created(new URI("/api/reward-histories/" + rewardHistory.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, rewardHistory.getId().toString()))
            .body(rewardHistory);
    }

    /**
     * {@code PUT  /reward-histories/:id} : Updates an existing rewardHistory.
     *
     * @param id the id of the rewardHistory to save.
     * @param rewardHistory the rewardHistory to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rewardHistory,
     * or with status {@code 400 (Bad Request)} if the rewardHistory is not valid,
     * or with status {@code 500 (Internal Server Error)} if the rewardHistory couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<RewardHistory> updateRewardHistory(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody RewardHistory rewardHistory
    ) throws URISyntaxException {
        LOG.debug("REST request to update RewardHistory : {}, {}", id, rewardHistory);
        if (rewardHistory.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, rewardHistory.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!rewardHistoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        rewardHistory = rewardHistoryRepository.save(rewardHistory);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, rewardHistory.getId().toString()))
            .body(rewardHistory);
    }

    /**
     * {@code PATCH  /reward-histories/:id} : Partial updates given fields of an existing rewardHistory, field will ignore if it is null
     *
     * @param id the id of the rewardHistory to save.
     * @param rewardHistory the rewardHistory to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rewardHistory,
     * or with status {@code 400 (Bad Request)} if the rewardHistory is not valid,
     * or with status {@code 404 (Not Found)} if the rewardHistory is not found,
     * or with status {@code 500 (Internal Server Error)} if the rewardHistory couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<RewardHistory> partialUpdateRewardHistory(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody RewardHistory rewardHistory
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update RewardHistory partially : {}, {}", id, rewardHistory);
        if (rewardHistory.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, rewardHistory.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!rewardHistoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RewardHistory> result = rewardHistoryRepository
            .findById(rewardHistory.getId())
            .map(existingRewardHistory -> {
                if (rewardHistory.getAction() != null) {
                    existingRewardHistory.setAction(rewardHistory.getAction());
                }
                if (rewardHistory.getActionDate() != null) {
                    existingRewardHistory.setActionDate(rewardHistory.getActionDate());
                }
                if (rewardHistory.getDescription() != null) {
                    existingRewardHistory.setDescription(rewardHistory.getDescription());
                }

                return existingRewardHistory;
            })
            .map(rewardHistoryRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, rewardHistory.getId().toString())
        );
    }

    /**
     * {@code GET  /reward-histories} : get all the rewardHistories.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of rewardHistories in body.
     */
    @GetMapping("")
    public ResponseEntity<List<RewardHistory>> getAllRewardHistories(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of RewardHistories");
        Page<RewardHistory> page = rewardHistoryRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /reward-histories/:id} : get the "id" rewardHistory.
     *
     * @param id the id of the rewardHistory to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the rewardHistory, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<RewardHistory> getRewardHistory(@PathVariable("id") Long id) {
        LOG.debug("REST request to get RewardHistory : {}", id);
        Optional<RewardHistory> rewardHistory = rewardHistoryRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(rewardHistory);
    }

    /**
     * {@code DELETE  /reward-histories/:id} : delete the "id" rewardHistory.
     *
     * @param id the id of the rewardHistory to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRewardHistory(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete RewardHistory : {}", id);
        rewardHistoryRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
