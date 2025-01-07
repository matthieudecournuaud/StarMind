package com.nova.star.web.rest;

import com.nova.star.repository.GlobalChatRepository;
import com.nova.star.service.GlobalChatService;
import com.nova.star.service.dto.GlobalChatDTO;
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
 * REST controller for managing {@link com.nova.star.domain.GlobalChat}.
 */
@RestController
@RequestMapping("/api/global-chats")
public class GlobalChatResource {

    private static final Logger LOG = LoggerFactory.getLogger(GlobalChatResource.class);

    private static final String ENTITY_NAME = "globalChat";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final GlobalChatService globalChatService;

    private final GlobalChatRepository globalChatRepository;

    public GlobalChatResource(GlobalChatService globalChatService, GlobalChatRepository globalChatRepository) {
        this.globalChatService = globalChatService;
        this.globalChatRepository = globalChatRepository;
    }

    /**
     * {@code POST  /global-chats} : Create a new globalChat.
     *
     * @param globalChatDTO the globalChatDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new globalChatDTO, or with status {@code 400 (Bad Request)} if the globalChat has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<GlobalChatDTO> createGlobalChat(@Valid @RequestBody GlobalChatDTO globalChatDTO) throws URISyntaxException {
        LOG.debug("REST request to save GlobalChat : {}", globalChatDTO);
        if (globalChatDTO.getId() != null) {
            throw new BadRequestAlertException("A new globalChat cannot already have an ID", ENTITY_NAME, "idexists");
        }
        globalChatDTO = globalChatService.save(globalChatDTO);
        return ResponseEntity.created(new URI("/api/global-chats/" + globalChatDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, globalChatDTO.getId().toString()))
            .body(globalChatDTO);
    }

    /**
     * {@code PUT  /global-chats/:id} : Updates an existing globalChat.
     *
     * @param id the id of the globalChatDTO to save.
     * @param globalChatDTO the globalChatDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated globalChatDTO,
     * or with status {@code 400 (Bad Request)} if the globalChatDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the globalChatDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<GlobalChatDTO> updateGlobalChat(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody GlobalChatDTO globalChatDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update GlobalChat : {}, {}", id, globalChatDTO);
        if (globalChatDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, globalChatDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!globalChatRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        globalChatDTO = globalChatService.update(globalChatDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, globalChatDTO.getId().toString()))
            .body(globalChatDTO);
    }

    /**
     * {@code PATCH  /global-chats/:id} : Partial updates given fields of an existing globalChat, field will ignore if it is null
     *
     * @param id the id of the globalChatDTO to save.
     * @param globalChatDTO the globalChatDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated globalChatDTO,
     * or with status {@code 400 (Bad Request)} if the globalChatDTO is not valid,
     * or with status {@code 404 (Not Found)} if the globalChatDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the globalChatDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<GlobalChatDTO> partialUpdateGlobalChat(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody GlobalChatDTO globalChatDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update GlobalChat partially : {}, {}", id, globalChatDTO);
        if (globalChatDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, globalChatDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!globalChatRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<GlobalChatDTO> result = globalChatService.partialUpdate(globalChatDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, globalChatDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /global-chats} : get all the globalChats.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of globalChats in body.
     */
    @GetMapping("")
    public ResponseEntity<List<GlobalChatDTO>> getAllGlobalChats(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get a page of GlobalChats");
        Page<GlobalChatDTO> page;
        if (eagerload) {
            page = globalChatService.findAllWithEagerRelationships(pageable);
        } else {
            page = globalChatService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /global-chats/:id} : get the "id" globalChat.
     *
     * @param id the id of the globalChatDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the globalChatDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<GlobalChatDTO> getGlobalChat(@PathVariable("id") Long id) {
        LOG.debug("REST request to get GlobalChat : {}", id);
        Optional<GlobalChatDTO> globalChatDTO = globalChatService.findOne(id);
        return ResponseUtil.wrapOrNotFound(globalChatDTO);
    }

    /**
     * {@code DELETE  /global-chats/:id} : delete the "id" globalChat.
     *
     * @param id the id of the globalChatDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGlobalChat(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete GlobalChat : {}", id);
        globalChatService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
