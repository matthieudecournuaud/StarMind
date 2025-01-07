package com.nova.star.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.nova.star.domain.enumeration.VoteOption;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Vote.
 */
@Entity
@Table(name = "vote")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Vote implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "vote_option", nullable = false)
    private VoteOption voteOption;

    @Lob
    @Column(name = "comment")
    private String comment;

    @NotNull
    @Column(name = "created_date", nullable = false)
    private ZonedDateTime createdDate;

    @ManyToOne(fetch = FetchType.LAZY)
    private User voter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = { "comments", "votes", "likeHistories", "author", "assignedReward", "manager", "category" },
        allowSetters = true
    )
    private Idea idea;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Vote id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public VoteOption getVoteOption() {
        return this.voteOption;
    }

    public Vote voteOption(VoteOption voteOption) {
        this.setVoteOption(voteOption);
        return this;
    }

    public void setVoteOption(VoteOption voteOption) {
        this.voteOption = voteOption;
    }

    public String getComment() {
        return this.comment;
    }

    public Vote comment(String comment) {
        this.setComment(comment);
        return this;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public ZonedDateTime getCreatedDate() {
        return this.createdDate;
    }

    public Vote createdDate(ZonedDateTime createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public User getVoter() {
        return this.voter;
    }

    public void setVoter(User user) {
        this.voter = user;
    }

    public Vote voter(User user) {
        this.setVoter(user);
        return this;
    }

    public Idea getIdea() {
        return this.idea;
    }

    public void setIdea(Idea idea) {
        this.idea = idea;
    }

    public Vote idea(Idea idea) {
        this.setIdea(idea);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Vote)) {
            return false;
        }
        return getId() != null && getId().equals(((Vote) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Vote{" +
            "id=" + getId() +
            ", voteOption='" + getVoteOption() + "'" +
            ", comment='" + getComment() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            "}";
    }
}
