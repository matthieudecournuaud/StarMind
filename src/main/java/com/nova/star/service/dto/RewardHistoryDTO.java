package com.nova.star.service.dto;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the {@link com.nova.star.domain.RewardHistory} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RewardHistoryDTO implements Serializable {

    private Long id;

    @NotNull
    private String action;

    @NotNull
    private ZonedDateTime actionDate;

    @Lob
    private String description;

    private IdeaDTO idea;

    private RewardDTO reward;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public ZonedDateTime getActionDate() {
        return actionDate;
    }

    public void setActionDate(ZonedDateTime actionDate) {
        this.actionDate = actionDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public IdeaDTO getIdea() {
        return idea;
    }

    public void setIdea(IdeaDTO idea) {
        this.idea = idea;
    }

    public RewardDTO getReward() {
        return reward;
    }

    public void setReward(RewardDTO reward) {
        this.reward = reward;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RewardHistoryDTO)) {
            return false;
        }

        RewardHistoryDTO rewardHistoryDTO = (RewardHistoryDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, rewardHistoryDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RewardHistoryDTO{" +
            "id=" + getId() +
            ", action='" + getAction() + "'" +
            ", actionDate='" + getActionDate() + "'" +
            ", description='" + getDescription() + "'" +
            ", idea=" + getIdea() +
            ", reward=" + getReward() +
            "}";
    }
}
