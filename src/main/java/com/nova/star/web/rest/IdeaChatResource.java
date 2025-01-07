package com.nova.star.web.rest;

import com.nova.star.repository.IdeaChatRepository;
import com.nova.star.service.IdeaChatService;
import com.nova.star.service.dto.IdeaChatDTO;
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
 * REST controller for managing {@link com.nova.star.domain.IdeaChat}.
 */
@RestController
@RequestMapping("/api/idea-chats")
public class IdeaChatResource {

    private static final Logger LOG = LoggerFactory.getLogger(IdeaChatResource.class);

    private static final String ENTITY_NAME = "ideaChat";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final IdeaChatService ideaChatService;

    private final IdeaChatRepository ideaChatRepository;

    public IdeaChatResource(IdeaChatService ideaChatService, IdeaChatRepository ideaChatRepository) {
        this.ideaChatService = ideaChatService;
        this.ideaChatRepository = ideaChatRepository;
    }

    /**
     * {@code POST  /idea-chats} : Create a new ideaChat.
     *
     * @param ideaChatDTO the ideaChatDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ideaChatDTO, or with status {@code 400 (Bad Request)} if the ideaChat has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<IdeaChatDTO> createIdeaChat(@Valid @RequestBody IdeaChatDTO ideaChatDTO) throws URISyntaxException {
        LOG.debug("REST request to save IdeaChat : {}", ideaChatDTO);
        if (ideaChatDTO.getId() != null) {
            throw new BadRequestAlertException("A new ideaChat cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ideaChatDTO = ideaChatService.save(ideaChatDTO);
        return ResponseEntity.created(new URI("/api/idea-chats/" + ideaChatDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, ideaChatDTO.getId().toString()))
            .body(ideaChatDTO);
    }

    /**
     * {@code PUT  /idea-chats/:id} : Updates an existing ideaChat.
     *
     * @param id the id of the ideaChatDTO to save.
     * @param ideaChatDTO the ideaChatDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ideaChatDTO,
     * or with status {@code 400 (Bad Request)} if the ideaChatDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ideaChatDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<IdeaChatDTO> updateIdeaChat(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody IdeaChatDTO ideaChatDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update IdeaChat : {}, {}", id, ideaChatDTO);
        if (ideaChatDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ideaChatDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ideaChatRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ideaChatDTO = ideaChatService.update(ideaChatDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ideaChatDTO.getId().toString()))
            .body(ideaChatDTO);
    }

    /**
     * {@code PATCH  /idea-chats/:id} : Partial updates given fields of an existing ideaChat, field will ignore if it is null
     *
     * @param id the id of the ideaChatDTO to save.
     * @param ideaChatDTO the ideaChatDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ideaChatDTO,
     * or with status {@code 400 (Bad Request)} if the ideaChatDTO is not valid,
     * or with status {@code 404 (Not Found)} if the ideaChatDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the ideaChatDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<IdeaChatDTO> partialUpdateIdeaChat(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody IdeaChatDTO ideaChatDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update IdeaChat partially : {}, {}", id, ideaChatDTO);
        if (ideaChatDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ideaChatDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ideaChatRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<IdeaChatDTO> result = ideaChatService.partialUpdate(ideaChatDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ideaChatDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /idea-chats} : get all the ideaChats.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ideaChats in body.
     */
    @GetMapping("")
    public ResponseEntity<List<IdeaChatDTO>> getAllIdeaChats(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get a page of IdeaChats");
        Page<IdeaChatDTO> page;
        if (eagerload) {
            page = ideaChatService.findAllWithEagerRelationships(pageable);
        } else {
            page = ideaChatService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /idea-chats/:id} : get the "id" ideaChat.
     *
     * @param id the id of the ideaChatDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ideaChatDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<IdeaChatDTO> getIdeaChat(@PathVariable("id") Long id) {
        LOG.debug("REST request to get IdeaChat : {}", id);
        Optional<IdeaChatDTO> ideaChatDTO = ideaChatService.findOne(id);
        return ResponseUtil.wrapOrNotFound(ideaChatDTO);
    }

    /**
     * {@code DELETE  /idea-chats/:id} : delete the "id" ideaChat.
     *
     * @param id the id of the ideaChatDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIdeaChat(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete IdeaChat : {}", id);
        ideaChatService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
