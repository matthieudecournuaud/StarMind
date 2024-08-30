package com.nova.star.web.rest;

import com.nova.star.domain.IdeaHistory;
import com.nova.star.repository.IdeaHistoryRepository;
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
 * REST controller for managing {@link com.nova.star.domain.IdeaHistory}.
 */
@RestController
@RequestMapping("/api/idea-histories")
@Transactional
public class IdeaHistoryResource {

    private static final Logger LOG = LoggerFactory.getLogger(IdeaHistoryResource.class);

    private static final String ENTITY_NAME = "ideaHistory";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final IdeaHistoryRepository ideaHistoryRepository;

    public IdeaHistoryResource(IdeaHistoryRepository ideaHistoryRepository) {
        this.ideaHistoryRepository = ideaHistoryRepository;
    }

    /**
     * {@code POST  /idea-histories} : Create a new ideaHistory.
     *
     * @param ideaHistory the ideaHistory to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ideaHistory, or with status {@code 400 (Bad Request)} if the ideaHistory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<IdeaHistory> createIdeaHistory(@Valid @RequestBody IdeaHistory ideaHistory) throws URISyntaxException {
        LOG.debug("REST request to save IdeaHistory : {}", ideaHistory);
        if (ideaHistory.getId() != null) {
            throw new BadRequestAlertException("A new ideaHistory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ideaHistory = ideaHistoryRepository.save(ideaHistory);
        return ResponseEntity.created(new URI("/api/idea-histories/" + ideaHistory.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, ideaHistory.getId().toString()))
            .body(ideaHistory);
    }

    /**
     * {@code PUT  /idea-histories/:id} : Updates an existing ideaHistory.
     *
     * @param id the id of the ideaHistory to save.
     * @param ideaHistory the ideaHistory to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ideaHistory,
     * or with status {@code 400 (Bad Request)} if the ideaHistory is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ideaHistory couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<IdeaHistory> updateIdeaHistory(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody IdeaHistory ideaHistory
    ) throws URISyntaxException {
        LOG.debug("REST request to update IdeaHistory : {}, {}", id, ideaHistory);
        if (ideaHistory.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ideaHistory.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ideaHistoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ideaHistory = ideaHistoryRepository.save(ideaHistory);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ideaHistory.getId().toString()))
            .body(ideaHistory);
    }

    /**
     * {@code PATCH  /idea-histories/:id} : Partial updates given fields of an existing ideaHistory, field will ignore if it is null
     *
     * @param id the id of the ideaHistory to save.
     * @param ideaHistory the ideaHistory to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ideaHistory,
     * or with status {@code 400 (Bad Request)} if the ideaHistory is not valid,
     * or with status {@code 404 (Not Found)} if the ideaHistory is not found,
     * or with status {@code 500 (Internal Server Error)} if the ideaHistory couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<IdeaHistory> partialUpdateIdeaHistory(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody IdeaHistory ideaHistory
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update IdeaHistory partially : {}, {}", id, ideaHistory);
        if (ideaHistory.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ideaHistory.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ideaHistoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<IdeaHistory> result = ideaHistoryRepository
            .findById(ideaHistory.getId())
            .map(existingIdeaHistory -> {
                if (ideaHistory.getAction() != null) {
                    existingIdeaHistory.setAction(ideaHistory.getAction());
                }
                if (ideaHistory.getActionDate() != null) {
                    existingIdeaHistory.setActionDate(ideaHistory.getActionDate());
                }
                if (ideaHistory.getDescription() != null) {
                    existingIdeaHistory.setDescription(ideaHistory.getDescription());
                }
                if (ideaHistory.getRewardType() != null) {
                    existingIdeaHistory.setRewardType(ideaHistory.getRewardType());
                }
                if (ideaHistory.getLikes() != null) {
                    existingIdeaHistory.setLikes(ideaHistory.getLikes());
                }

                return existingIdeaHistory;
            })
            .map(ideaHistoryRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ideaHistory.getId().toString())
        );
    }

    /**
     * {@code GET  /idea-histories} : get all the ideaHistories.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ideaHistories in body.
     */
    @GetMapping("")
    public ResponseEntity<List<IdeaHistory>> getAllIdeaHistories(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of IdeaHistories");
        Page<IdeaHistory> page = ideaHistoryRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /idea-histories/:id} : get the "id" ideaHistory.
     *
     * @param id the id of the ideaHistory to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ideaHistory, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<IdeaHistory> getIdeaHistory(@PathVariable("id") Long id) {
        LOG.debug("REST request to get IdeaHistory : {}", id);
        Optional<IdeaHistory> ideaHistory = ideaHistoryRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(ideaHistory);
    }

    /**
     * {@code DELETE  /idea-histories/:id} : delete the "id" ideaHistory.
     *
     * @param id the id of the ideaHistory to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIdeaHistory(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete IdeaHistory : {}", id);
        ideaHistoryRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
