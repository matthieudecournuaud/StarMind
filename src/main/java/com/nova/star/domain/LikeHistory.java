package com.nova.star.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A LikeHistory.
 */
@Entity
@Table(name = "like_history")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class LikeHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "action", nullable = false)
    private String action;

    @NotNull
    @Column(name = "action_date", nullable = false)
    private ZonedDateTime actionDate;

    @Column(name = "old_likes")
    private String oldLikes;

    @Column(name = "new_likes")
    private String newLikes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "comments", "author", "ideaCategory", "assignedReward", "category", "reward" }, allowSetters = true)
    private Idea idea;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public LikeHistory id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAction() {
        return this.action;
    }

    public LikeHistory action(String action) {
        this.setAction(action);
        return this;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public ZonedDateTime getActionDate() {
        return this.actionDate;
    }

    public LikeHistory actionDate(ZonedDateTime actionDate) {
        this.setActionDate(actionDate);
        return this;
    }

    public void setActionDate(ZonedDateTime actionDate) {
        this.actionDate = actionDate;
    }

    public String getOldLikes() {
        return this.oldLikes;
    }

    public LikeHistory oldLikes(String oldLikes) {
        this.setOldLikes(oldLikes);
        return this;
    }

    public void setOldLikes(String oldLikes) {
        this.oldLikes = oldLikes;
    }

    public String getNewLikes() {
        return this.newLikes;
    }

    public LikeHistory newLikes(String newLikes) {
        this.setNewLikes(newLikes);
        return this;
    }

    public void setNewLikes(String newLikes) {
        this.newLikes = newLikes;
    }

    public Idea getIdea() {
        return this.idea;
    }

    public void setIdea(Idea idea) {
        this.idea = idea;
    }

    public LikeHistory idea(Idea idea) {
        this.setIdea(idea);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LikeHistory)) {
            return false;
        }
        return getId() != null && getId().equals(((LikeHistory) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LikeHistory{" +
            "id=" + getId() +
            ", action='" + getAction() + "'" +
            ", actionDate='" + getActionDate() + "'" +
            ", oldLikes='" + getOldLikes() + "'" +
            ", newLikes='" + getNewLikes() + "'" +
            "}";
    }
}
