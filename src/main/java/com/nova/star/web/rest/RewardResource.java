package com.nova.star.web.rest;

import com.nova.star.domain.Reward;
import com.nova.star.repository.RewardRepository;
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
 * REST controller for managing {@link com.nova.star.domain.Reward}.
 */
@RestController
@RequestMapping("/api/rewards")
@Transactional
public class RewardResource {

    private static final Logger LOG = LoggerFactory.getLogger(RewardResource.class);

    private static final String ENTITY_NAME = "reward";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RewardRepository rewardRepository;

    public RewardResource(RewardRepository rewardRepository) {
        this.rewardRepository = rewardRepository;
    }

    /**
     * {@code POST  /rewards} : Create a new reward.
     *
     * @param reward the reward to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new reward, or with status {@code 400 (Bad Request)} if the reward has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Reward> createReward(@Valid @RequestBody Reward reward) throws URISyntaxException {
        LOG.debug("REST request to save Reward : {}", reward);
        if (reward.getId() != null) {
            throw new BadRequestAlertException("A new reward cannot already have an ID", ENTITY_NAME, "idexists");
        }
        reward = rewardRepository.save(reward);
        return ResponseEntity.created(new URI("/api/rewards/" + reward.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, reward.getId().toString()))
            .body(reward);
    }

    /**
     * {@code PUT  /rewards/:id} : Updates an existing reward.
     *
     * @param id the id of the reward to save.
     * @param reward the reward to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reward,
     * or with status {@code 400 (Bad Request)} if the reward is not valid,
     * or with status {@code 500 (Internal Server Error)} if the reward couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Reward> updateReward(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Reward reward
    ) throws URISyntaxException {
        LOG.debug("REST request to update Reward : {}, {}", id, reward);
        if (reward.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, reward.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!rewardRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        reward = rewardRepository.save(reward);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, reward.getId().toString()))
            .body(reward);
    }

    /**
     * {@code PATCH  /rewards/:id} : Partial updates given fields of an existing reward, field will ignore if it is null
     *
     * @param id the id of the reward to save.
     * @param reward the reward to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reward,
     * or with status {@code 400 (Bad Request)} if the reward is not valid,
     * or with status {@code 404 (Not Found)} if the reward is not found,
     * or with status {@code 500 (Internal Server Error)} if the reward couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Reward> partialUpdateReward(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Reward reward
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Reward partially : {}, {}", id, reward);
        if (reward.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, reward.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!rewardRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Reward> result = rewardRepository
            .findById(reward.getId())
            .map(existingReward -> {
                if (reward.getName() != null) {
                    existingReward.setName(reward.getName());
                }
                if (reward.getDescription() != null) {
                    existingReward.setDescription(reward.getDescription());
                }

                return existingReward;
            })
            .map(rewardRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, reward.getId().toString())
        );
    }

    /**
     * {@code GET  /rewards} : get all the rewards.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of rewards in body.
     */
    @GetMapping("")
    public ResponseEntity<List<Reward>> getAllRewards(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of Rewards");
        Page<Reward> page = rewardRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /rewards/:id} : get the "id" reward.
     *
     * @param id the id of the reward to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the reward, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Reward> getReward(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Reward : {}", id);
        Optional<Reward> reward = rewardRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(reward);
    }

    /**
     * {@code DELETE  /rewards/:id} : delete the "id" reward.
     *
     * @param id the id of the reward to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReward(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Reward : {}", id);
        rewardRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
