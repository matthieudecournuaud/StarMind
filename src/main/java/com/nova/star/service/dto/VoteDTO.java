package com.nova.star.service.dto;

import com.nova.star.domain.enumeration.VoteOption;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the {@link com.nova.star.domain.Vote} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class VoteDTO implements Serializable {

    private Long id;

    @NotNull
    private VoteOption voteOption;

    @Lob
    private String comment;

    @NotNull
    private ZonedDateTime createdDate;

    private UserDTO voter;

    private IdeaDTO idea;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public VoteOption getVoteOption() {
        return voteOption;
    }

    public void setVoteOption(VoteOption voteOption) {
        this.voteOption = voteOption;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public ZonedDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public UserDTO getVoter() {
        return voter;
    }

    public void setVoter(UserDTO voter) {
        this.voter = voter;
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
        if (!(o instanceof VoteDTO)) {
            return false;
        }

        VoteDTO voteDTO = (VoteDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, voteDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VoteDTO{" +
            "id=" + getId() +
            ", voteOption='" + getVoteOption() + "'" +
            ", comment='" + getComment() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", voter=" + getVoter() +
            ", idea=" + getIdea() +
            "}";
    }
}
