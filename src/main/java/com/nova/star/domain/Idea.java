package com.nova.star.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.nova.star.domain.enumeration.IdeaStatus;
import com.nova.star.domain.enumeration.RewardType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Idea.
 */
@Entity
@Table(name = "idea")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Idea implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @Lob
    @Column(name = "description", nullable = false)
    private String description;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private IdeaStatus status;

    @Column(name = "validation")
    private Boolean validation;

    @Enumerated(EnumType.STRING)
    @Column(name = "reward_type")
    private RewardType rewardType;

    @Column(name = "likes")
    private String likes;

    @Column(name = "created_date")
    private ZonedDateTime createdDate;

    @Column(name = "modified_date")
    private ZonedDateTime modifiedDate;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "idea")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "relatedIdea", "author", "idea" }, allowSetters = true)
    private Set<Comment> comments = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    private User author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "ideas", "subcategories", "parentCategory", "superCategory" }, allowSetters = true)
    private Category ideaCategory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "ideas" }, allowSetters = true)
    private Reward assignedReward;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "ideas", "subcategories", "parentCategory", "superCategory" }, allowSetters = true)
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "ideas" }, allowSetters = true)
    private Reward reward;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Idea id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public Idea title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return this.description;
    }

    public Idea description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public IdeaStatus getStatus() {
        return this.status;
    }

    public Idea status(IdeaStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(IdeaStatus status) {
        this.status = status;
    }

    public Boolean getValidation() {
        return this.validation;
    }

    public Idea validation(Boolean validation) {
        this.setValidation(validation);
        return this;
    }

    public void setValidation(Boolean validation) {
        this.validation = validation;
    }

    public RewardType getRewardType() {
        return this.rewardType;
    }

    public Idea rewardType(RewardType rewardType) {
        this.setRewardType(rewardType);
        return this;
    }

    public void setRewardType(RewardType rewardType) {
        this.rewardType = rewardType;
    }

    public String getLikes() {
        return this.likes;
    }

    public Idea likes(String likes) {
        this.setLikes(likes);
        return this;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }

    public ZonedDateTime getCreatedDate() {
        return this.createdDate;
    }

    public Idea createdDate(ZonedDateTime createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public ZonedDateTime getModifiedDate() {
        return this.modifiedDate;
    }

    public Idea modifiedDate(ZonedDateTime modifiedDate) {
        this.setModifiedDate(modifiedDate);
        return this;
    }

    public void setModifiedDate(ZonedDateTime modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public Set<Comment> getComments() {
        return this.comments;
    }

    public void setComments(Set<Comment> comments) {
        if (this.comments != null) {
            this.comments.forEach(i -> i.setIdea(null));
        }
        if (comments != null) {
            comments.forEach(i -> i.setIdea(this));
        }
        this.comments = comments;
    }

    public Idea comments(Set<Comment> comments) {
        this.setComments(comments);
        return this;
    }

    public Idea addComments(Comment comment) {
        this.comments.add(comment);
        comment.setIdea(this);
        return this;
    }

    public Idea removeComments(Comment comment) {
        this.comments.remove(comment);
        comment.setIdea(null);
        return this;
    }

    public User getAuthor() {
        return this.author;
    }

    public void setAuthor(User user) {
        this.author = user;
    }

    public Idea author(User user) {
        this.setAuthor(user);
        return this;
    }

    public Category getIdeaCategory() {
        return this.ideaCategory;
    }

    public void setIdeaCategory(Category category) {
        this.ideaCategory = category;
    }

    public Idea ideaCategory(Category category) {
        this.setIdeaCategory(category);
        return this;
    }

    public Reward getAssignedReward() {
        return this.assignedReward;
    }

    public void setAssignedReward(Reward reward) {
        this.assignedReward = reward;
    }

    public Idea assignedReward(Reward reward) {
        this.setAssignedReward(reward);
        return this;
    }

    public Category getCategory() {
        return this.category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Idea category(Category category) {
        this.setCategory(category);
        return this;
    }

    public Reward getReward() {
        return this.reward;
    }

    public void setReward(Reward reward) {
        this.reward = reward;
    }

    public Idea reward(Reward reward) {
        this.setReward(reward);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Idea)) {
            return false;
        }
        return getId() != null && getId().equals(((Idea) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Idea{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", status='" + getStatus() + "'" +
            ", validation='" + getValidation() + "'" +
            ", rewardType='" + getRewardType() + "'" +
            ", likes='" + getLikes() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", modifiedDate='" + getModifiedDate() + "'" +
            "}";
    }
}
