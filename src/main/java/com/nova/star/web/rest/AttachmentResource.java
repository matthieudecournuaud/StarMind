package com.nova.star.web.rest;

import com.nova.star.domain.Attachment;
import com.nova.star.repository.AttachmentRepository;
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
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.nova.star.domain.Attachment}.
 */
@RestController
@RequestMapping("/api/attachments")
@Transactional
public class AttachmentResource {

    private static final Logger LOG = LoggerFactory.getLogger(AttachmentResource.class);

    private static final String ENTITY_NAME = "attachment";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AttachmentRepository attachmentRepository;

    public AttachmentResource(AttachmentRepository attachmentRepository) {
        this.attachmentRepository = attachmentRepository;
    }

    /**
     * {@code POST  /attachments} : Create a new attachment.
     *
     * @param attachment the attachment to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new attachment, or with status {@code 400 (Bad Request)} if the attachment has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Attachment> createAttachment(@Valid @RequestBody Attachment attachment) throws URISyntaxException {
        LOG.debug("REST request to save Attachment : {}", attachment);
        if (attachment.getId() != null) {
            throw new BadRequestAlertException("A new attachment cannot already have an ID", ENTITY_NAME, "idexists");
        }
        attachment = attachmentRepository.save(attachment);
        return ResponseEntity.created(new URI("/api/attachments/" + attachment.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, attachment.getId().toString()))
            .body(attachment);
    }

    /**
     * {@code PUT  /attachments/:id} : Updates an existing attachment.
     *
     * @param id the id of the attachment to save.
     * @param attachment the attachment to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated attachment,
     * or with status {@code 400 (Bad Request)} if the attachment is not valid,
     * or with status {@code 500 (Internal Server Error)} if the attachment couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Attachment> updateAttachment(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Attachment attachment
    ) throws URISyntaxException {
        LOG.debug("REST request to update Attachment : {}, {}", id, attachment);
        if (attachment.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, attachment.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!attachmentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        attachment = attachmentRepository.save(attachment);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, attachment.getId().toString()))
            .body(attachment);
    }

    /**
     * {@code PATCH  /attachments/:id} : Partial updates given fields of an existing attachment, field will ignore if it is null
     *
     * @param id the id of the attachment to save.
     * @param attachment the attachment to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated attachment,
     * or with status {@code 400 (Bad Request)} if the attachment is not valid,
     * or with status {@code 404 (Not Found)} if the attachment is not found,
     * or with status {@code 500 (Internal Server Error)} if the attachment couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Attachment> partialUpdateAttachment(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Attachment attachment
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Attachment partially : {}, {}", id, attachment);
        if (attachment.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, attachment.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!attachmentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Attachment> result = attachmentRepository
            .findById(attachment.getId())
            .map(existingAttachment -> {
                if (attachment.getFileName() != null) {
                    existingAttachment.setFileName(attachment.getFileName());
                }
                if (attachment.getFileType() != null) {
                    existingAttachment.setFileType(attachment.getFileType());
                }
                if (attachment.getData() != null) {
                    existingAttachment.setData(attachment.getData());
                }
                if (attachment.getDataContentType() != null) {
                    existingAttachment.setDataContentType(attachment.getDataContentType());
                }

                return existingAttachment;
            })
            .map(attachmentRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, attachment.getId().toString())
        );
    }

    /**
     * {@code GET  /attachments} : get all the attachments.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of attachments in body.
     */
    @GetMapping("")
    public List<Attachment> getAllAttachments() {
        LOG.debug("REST request to get all Attachments");
        return attachmentRepository.findAll();
    }

    /**
     * {@code GET  /attachments/:id} : get the "id" attachment.
     *
     * @param id the id of the attachment to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the attachment, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Attachment> getAttachment(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Attachment : {}", id);
        Optional<Attachment> attachment = attachmentRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(attachment);
    }

    /**
     * {@code DELETE  /attachments/:id} : delete the "id" attachment.
     *
     * @param id the id of the attachment to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAttachment(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Attachment : {}", id);
        attachmentRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
