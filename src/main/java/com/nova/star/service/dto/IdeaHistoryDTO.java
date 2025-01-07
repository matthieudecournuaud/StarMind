package com.nova.star.service.dto;

import com.nova.star.domain.enumeration.RewardType;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the {@link com.nova.star.domain.IdeaHistory} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class IdeaHistoryDTO implements Serializable {

    private Long id;

    @NotNull
    private String action;

    @NotNull
    private ZonedDateTime actionDate;

    @Lob
    private String description;

    private RewardType rewardType;

    private Integer likes;

    private IdeaDTO idea;

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

    public RewardType getRewardType() {
        return rewardType;
    }

    public void setRewardType(RewardType rewardType) {
        this.rewardType = rewardType;
    }

    public Integer getLikes() {
        return likes;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    public IdeaDTO getIdea() {
        return idea;
    }

    public void setIdea(IdeaDTO idea) {
        this.idea = idea;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof IdeaHistoryDTO)) {
            return false;
        }

        IdeaHistoryDTO ideaHistoryDTO = (IdeaHistoryDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, ideaHistoryDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "IdeaHistoryDTO{" +
            "id=" + getId() +
            ", action='" + getAction() + "'" +
            ", actionDate='" + getActionDate() + "'" +
            ", description='" + getDescription() + "'" +
            ", rewardType='" + getRewardType() + "'" +
            ", likes=" + getLikes() +
            ", idea=" + getIdea() +
            "}";
    }
}
