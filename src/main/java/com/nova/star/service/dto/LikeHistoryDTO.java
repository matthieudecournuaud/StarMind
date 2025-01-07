package com.nova.star.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the {@link com.nova.star.domain.LikeHistory} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class LikeHistoryDTO implements Serializable {

    private Long id;

    @NotNull
    private String action;

    @NotNull
    private ZonedDateTime actionDate;

    private Integer oldLikes;

    private Integer newLikes;

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

    public Integer getOldLikes() {
        return oldLikes;
    }

    public void setOldLikes(Integer oldLikes) {
        this.oldLikes = oldLikes;
    }

    public Integer getNewLikes() {
        return newLikes;
    }

    public void setNewLikes(Integer newLikes) {
        this.newLikes = newLikes;
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
        if (!(o instanceof LikeHistoryDTO)) {
            return false;
        }

        LikeHistoryDTO likeHistoryDTO = (LikeHistoryDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, likeHistoryDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LikeHistoryDTO{" +
            "id=" + getId() +
            ", action='" + getAction() + "'" +
            ", actionDate='" + getActionDate() + "'" +
            ", oldLikes=" + getOldLikes() +
            ", newLikes=" + getNewLikes() +
            ", idea=" + getIdea() +
            "}";
    }
}
