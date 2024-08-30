package com.nova.star.web.rest;

import com.nova.star.domain.LikeHistory;
import com.nova.star.repository.LikeHistoryRepository;
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
 * REST controller for managing {@link com.nova.star.domain.LikeHistory}.
 */
@RestController
@RequestMapping("/api/like-histories")
@Transactional
public class LikeHistoryResource {

    private static final Logger LOG = LoggerFactory.getLogger(LikeHistoryResource.class);

    private static final String ENTITY_NAME = "likeHistory";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LikeHistoryRepository likeHistoryRepository;

    public LikeHistoryResource(LikeHistoryRepository likeHistoryRepository) {
        this.likeHistoryRepository = likeHistoryRepository;
    }

    /**
     * {@code POST  /like-histories} : Create a new likeHistory.
     *
     * @param likeHistory the likeHistory to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new likeHistory, or with status {@code 400 (Bad Request)} if the likeHistory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<LikeHistory> createLikeHistory(@Valid @RequestBody LikeHistory likeHistory) throws URISyntaxException {
        LOG.debug("REST request to save LikeHistory : {}", likeHistory);
        if (likeHistory.getId() != null) {
            throw new BadRequestAlertException("A new likeHistory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        likeHistory = likeHistoryRepository.save(likeHistory);
        return ResponseEntity.created(new URI("/api/like-histories/" + likeHistory.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, likeHistory.getId().toString()))
            .body(likeHistory);
    }

    /**
     * {@code PUT  /like-histories/:id} : Updates an existing likeHistory.
     *
     * @param id the id of the likeHistory to save.
     * @param likeHistory the likeHistory to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated likeHistory,
     * or with status {@code 400 (Bad Request)} if the likeHistory is not valid,
     * or with status {@code 500 (Internal Server Error)} if the likeHistory couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<LikeHistory> updateLikeHistory(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody LikeHistory likeHistory
    ) throws URISyntaxException {
        LOG.debug("REST request to update LikeHistory : {}, {}", id, likeHistory);
        if (likeHistory.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, likeHistory.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!likeHistoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        likeHistory = likeHistoryRepository.save(likeHistory);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, likeHistory.getId().toString()))
            .body(likeHistory);
    }

    /**
     * {@code PATCH  /like-histories/:id} : Partial updates given fields of an existing likeHistory, field will ignore if it is null
     *
     * @param id the id of the likeHistory to save.
     * @param likeHistory the likeHistory to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated likeHistory,
     * or with status {@code 400 (Bad Request)} if the likeHistory is not valid,
     * or with status {@code 404 (Not Found)} if the likeHistory is not found,
     * or with status {@code 500 (Internal Server Error)} if the likeHistory couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<LikeHistory> partialUpdateLikeHistory(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody LikeHistory likeHistory
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update LikeHistory partially : {}, {}", id, likeHistory);
        if (likeHistory.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, likeHistory.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!likeHistoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<LikeHistory> result = likeHistoryRepository
            .findById(likeHistory.getId())
            .map(existingLikeHistory -> {
                if (likeHistory.getAction() != null) {
                    existingLikeHistory.setAction(likeHistory.getAction());
                }
                if (likeHistory.getActionDate() != null) {
                    existingLikeHistory.setActionDate(likeHistory.getActionDate());
                }
                if (likeHistory.getOldLikes() != null) {
                    existingLikeHistory.setOldLikes(likeHistory.getOldLikes());
                }
                if (likeHistory.getNewLikes() != null) {
                    existingLikeHistory.setNewLikes(likeHistory.getNewLikes());
                }

                return existingLikeHistory;
            })
            .map(likeHistoryRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, likeHistory.getId().toString())
        );
    }

    /**
     * {@code GET  /like-histories} : get all the likeHistories.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of likeHistories in body.
     */
    @GetMapping("")
    public ResponseEntity<List<LikeHistory>> getAllLikeHistories(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of LikeHistories");
        Page<LikeHistory> page = likeHistoryRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /like-histories/:id} : get the "id" likeHistory.
     *
     * @param id the id of the likeHistory to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the likeHistory, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<LikeHistory> getLikeHistory(@PathVariable("id") Long id) {
        LOG.debug("REST request to get LikeHistory : {}", id);
        Optional<LikeHistory> likeHistory = likeHistoryRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(likeHistory);
    }

    /**
     * {@code DELETE  /like-histories/:id} : delete the "id" likeHistory.
     *
     * @param id the id of the likeHistory to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLikeHistory(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete LikeHistory : {}", id);
        likeHistoryRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
