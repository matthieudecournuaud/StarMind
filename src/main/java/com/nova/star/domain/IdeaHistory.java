package com.nova.star.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.nova.star.domain.enumeration.RewardType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A IdeaHistory.
 */
@Entity
@Table(name = "idea_history")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class IdeaHistory implements Serializable {

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

    @Lob
    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "reward_type")
    private RewardType rewardType;

    @Column(name = "likes")
    private String likes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "comments", "author", "ideaCategory", "assignedReward", "category", "reward" }, allowSetters = true)
    private Idea idea;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public IdeaHistory id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAction() {
        return this.action;
    }

    public IdeaHistory action(String action) {
        this.setAction(action);
        return this;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public ZonedDateTime getActionDate() {
        return this.actionDate;
    }

    public IdeaHistory actionDate(ZonedDateTime actionDate) {
        this.setActionDate(actionDate);
        return this;
    }

    public void setActionDate(ZonedDateTime actionDate) {
        this.actionDate = actionDate;
    }

    public String getDescription() {
        return this.description;
    }

    public IdeaHistory description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public RewardType getRewardType() {
        return this.rewardType;
    }

    public IdeaHistory rewardType(RewardType rewardType) {
        this.setRewardType(rewardType);
        return this;
    }

    public void setRewardType(RewardType rewardType) {
        this.rewardType = rewardType;
    }

    public String getLikes() {
        return this.likes;
    }

    public IdeaHistory likes(String likes) {
        this.setLikes(likes);
        return this;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }

    public Idea getIdea() {
        return this.idea;
    }

    public void setIdea(Idea idea) {
        this.idea = idea;
    }

    public IdeaHistory idea(Idea idea) {
        this.setIdea(idea);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof IdeaHistory)) {
            return false;
        }
        return getId() != null && getId().equals(((IdeaHistory) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "IdeaHistory{" +
            "id=" + getId() +
            ", action='" + getAction() + "'" +
            ", actionDate='" + getActionDate() + "'" +
            ", description='" + getDescription() + "'" +
            ", rewardType='" + getRewardType() + "'" +
            ", likes='" + getLikes() + "'" +
            "}";
    }
}
