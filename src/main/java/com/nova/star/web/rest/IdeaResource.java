package com.nova.star.web.rest;

import com.nova.star.domain.Idea;
import com.nova.star.repository.IdeaRepository;
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
 * REST controller for managing {@link com.nova.star.domain.Idea}.
 */
@RestController
@RequestMapping("/api/ideas")
@Transactional
public class IdeaResource {

    private static final Logger LOG = LoggerFactory.getLogger(IdeaResource.class);

    private static final String ENTITY_NAME = "idea";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final IdeaRepository ideaRepository;

    public IdeaResource(IdeaRepository ideaRepository) {
        this.ideaRepository = ideaRepository;
    }

    /**
     * {@code POST  /ideas} : Create a new idea.
     *
     * @param idea the idea to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new idea, or with status {@code 400 (Bad Request)} if the idea has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Idea> createIdea(@Valid @RequestBody Idea idea) throws URISyntaxException {
        LOG.debug("REST request to save Idea : {}", idea);
        if (idea.getId() != null) {
            throw new BadRequestAlertException("A new idea cannot already have an ID", ENTITY_NAME, "idexists");
        }
        idea = ideaRepository.save(idea);
        return ResponseEntity.created(new URI("/api/ideas/" + idea.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, idea.getId().toString()))
            .body(idea);
    }

    /**
     * {@code PUT  /ideas/:id} : Updates an existing idea.
     *
     * @param id the id of the idea to save.
     * @param idea the idea to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated idea,
     * or with status {@code 400 (Bad Request)} if the idea is not valid,
     * or with status {@code 500 (Internal Server Error)} if the idea couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Idea> updateIdea(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody Idea idea)
        throws URISyntaxException {
        LOG.debug("REST request to update Idea : {}, {}", id, idea);
        if (idea.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, idea.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ideaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        idea = ideaRepository.save(idea);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, idea.getId().toString()))
            .body(idea);
    }

    /**
     * {@code PATCH  /ideas/:id} : Partial updates given fields of an existing idea, field will ignore if it is null
     *
     * @param id the id of the idea to save.
     * @param idea the idea to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated idea,
     * or with status {@code 400 (Bad Request)} if the idea is not valid,
     * or with status {@code 404 (Not Found)} if the idea is not found,
     * or with status {@code 500 (Internal Server Error)} if the idea couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Idea> partialUpdateIdea(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Idea idea
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Idea partially : {}, {}", id, idea);
        if (idea.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, idea.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ideaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Idea> result = ideaRepository
            .findById(idea.getId())
            .map(existingIdea -> {
                if (idea.getTitle() != null) {
                    existingIdea.setTitle(idea.getTitle());
                }
                if (idea.getDescription() != null) {
                    existingIdea.setDescription(idea.getDescription());
                }
                if (idea.getStatus() != null) {
                    existingIdea.setStatus(idea.getStatus());
                }
                if (idea.getValidation() != null) {
                    existingIdea.setValidation(idea.getValidation());
                }
                if (idea.getRewardType() != null) {
                    existingIdea.setRewardType(idea.getRewardType());
                }
                if (idea.getLikes() != null) {
                    existingIdea.setLikes(idea.getLikes());
                }
                if (idea.getCreatedDate() != null) {
                    existingIdea.setCreatedDate(idea.getCreatedDate());
                }
                if (idea.getModifiedDate() != null) {
                    existingIdea.setModifiedDate(idea.getModifiedDate());
                }

                return existingIdea;
            })
            .map(ideaRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, idea.getId().toString())
        );
    }

    /**
     * {@code GET  /ideas} : get all the ideas.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ideas in body.
     */
    @GetMapping("")
    public ResponseEntity<List<Idea>> getAllIdeas(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of Ideas");
        Page<Idea> page = ideaRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /ideas/:id} : get the "id" idea.
     *
     * @param id the id of the idea to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the idea, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Idea> getIdea(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Idea : {}", id);
        Optional<Idea> idea = ideaRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(idea);
    }

    /**
     * {@code DELETE  /ideas/:id} : delete the "id" idea.
     *
     * @param id the id of the idea to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIdea(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Idea : {}", id);
        ideaRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
