package com.nova.star.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ProspectBoard.
 */
@Entity
@Table(name = "prospect_board")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProspectBoard implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Lob
    @Column(name = "description")
    private String description;

    @NotNull
    @Column(name = "created_date", nullable = false)
    private ZonedDateTime createdDate;

    @NotNull
    @Column(name = "modified_date", nullable = false)
    private ZonedDateTime modifiedDate;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "prospectBoard")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "prospect", "prospectBoard" }, allowSetters = true)
    private Set<ProspectEntry> entries = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    private User owner;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ProspectBoard id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public ProspectBoard name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public ProspectBoard description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ZonedDateTime getCreatedDate() {
        return this.createdDate;
    }

    public ProspectBoard createdDate(ZonedDateTime createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public ZonedDateTime getModifiedDate() {
        return this.modifiedDate;
    }

    public ProspectBoard modifiedDate(ZonedDateTime modifiedDate) {
        this.setModifiedDate(modifiedDate);
        return this;
    }

    public void setModifiedDate(ZonedDateTime modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public Set<ProspectEntry> getEntries() {
        return this.entries;
    }

    public void setEntries(Set<ProspectEntry> prospectEntries) {
        if (this.entries != null) {
            this.entries.forEach(i -> i.setProspectBoard(null));
        }
        if (prospectEntries != null) {
            prospectEntries.forEach(i -> i.setProspectBoard(this));
        }
        this.entries = prospectEntries;
    }

    public ProspectBoard entries(Set<ProspectEntry> prospectEntries) {
        this.setEntries(prospectEntries);
        return this;
    }

    public ProspectBoard addEntries(ProspectEntry prospectEntry) {
        this.entries.add(prospectEntry);
        prospectEntry.setProspectBoard(this);
        return this;
    }

    public ProspectBoard removeEntries(ProspectEntry prospectEntry) {
        this.entries.remove(prospectEntry);
        prospectEntry.setProspectBoard(null);
        return this;
    }

    public User getOwner() {
        return this.owner;
    }

    public void setOwner(User user) {
        this.owner = user;
    }

    public ProspectBoard owner(User user) {
        this.setOwner(user);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProspectBoard)) {
            return false;
        }
        return getId() != null && getId().equals(((ProspectBoard) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProspectBoard{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", modifiedDate='" + getModifiedDate() + "'" +
            "}";
    }
}
