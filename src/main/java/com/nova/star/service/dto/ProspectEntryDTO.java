package com.nova.star.service.dto;

import com.nova.star.domain.enumeration.ProspectStatus;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the {@link com.nova.star.domain.ProspectEntry} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProspectEntryDTO implements Serializable {

    private Long id;

    @NotNull
    private ProspectStatus status;

    @NotNull
    private ZonedDateTime createdDate;

    @NotNull
    private ZonedDateTime modifiedDate;

    private UserDTO prospect;

    private ProspectBoardDTO prospectBoard;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ProspectStatus getStatus() {
        return status;
    }

    public void setStatus(ProspectStatus status) {
        this.status = status;
    }

    public ZonedDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public ZonedDateTime getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(ZonedDateTime modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public UserDTO getProspect() {
        return prospect;
    }

    public void setProspect(UserDTO prospect) {
        this.prospect = prospect;
    }

    public ProspectBoardDTO getProspectBoard() {
        return prospectBoard;
    }

    public void setProspectBoard(ProspectBoardDTO prospectBoard) {
        this.prospectBoard = prospectBoard;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProspectEntryDTO)) {
            return false;
        }

        ProspectEntryDTO prospectEntryDTO = (ProspectEntryDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, prospectEntryDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProspectEntryDTO{" +
            "id=" + getId() +
            ", status='" + getStatus() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", modifiedDate='" + getModifiedDate() + "'" +
            ", prospect=" + getProspect() +
            ", prospectBoard=" + getProspectBoard() +
            "}";
    }
}
