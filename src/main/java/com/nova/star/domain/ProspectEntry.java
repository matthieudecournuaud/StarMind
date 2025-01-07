package com.nova.star.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.nova.star.domain.enumeration.ProspectStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ProspectEntry.
 */
@Entity
@Table(name = "prospect_entry")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProspectEntry implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ProspectStatus status;

    @NotNull
    @Column(name = "created_date", nullable = false)
    private ZonedDateTime createdDate;

    @NotNull
    @Column(name = "modified_date", nullable = false)
    private ZonedDateTime modifiedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    private User prospect;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "entries", "owner" }, allowSetters = true)
    private ProspectBoard prospectBoard;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ProspectEntry id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ProspectStatus getStatus() {
        return this.status;
    }

    public ProspectEntry status(ProspectStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(ProspectStatus status) {
        this.status = status;
    }

    public ZonedDateTime getCreatedDate() {
        return this.createdDate;
    }

    public ProspectEntry createdDate(ZonedDateTime createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public ZonedDateTime getModifiedDate() {
        return this.modifiedDate;
    }

    public ProspectEntry modifiedDate(ZonedDateTime modifiedDate) {
        this.setModifiedDate(modifiedDate);
        return this;
    }

    public void setModifiedDate(ZonedDateTime modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public User getProspect() {
        return this.prospect;
    }

    public void setProspect(User user) {
        this.prospect = user;
    }

    public ProspectEntry prospect(User user) {
        this.setProspect(user);
        return this;
    }

    public ProspectBoard getProspectBoard() {
        return this.prospectBoard;
    }

    public void setProspectBoard(ProspectBoard prospectBoard) {
        this.prospectBoard = prospectBoard;
    }

    public ProspectEntry prospectBoard(ProspectBoard prospectBoard) {
        this.setProspectBoard(prospectBoard);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProspectEntry)) {
            return false;
        }
        return getId() != null && getId().equals(((ProspectEntry) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProspectEntry{" +
            "id=" + getId() +
            ", status='" + getStatus() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", modifiedDate='" + getModifiedDate() + "'" +
            "}";
    }
}
