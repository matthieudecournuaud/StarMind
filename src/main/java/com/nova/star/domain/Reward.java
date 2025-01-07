package com.nova.star.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Reward.
 */
@Entity
@Table(name = "reward")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Reward implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "reward")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "idea", "reward" }, allowSetters = true)
    private Set<RewardHistory> rewardHistories = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Reward id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Reward name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public Reward description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<RewardHistory> getRewardHistories() {
        return this.rewardHistories;
    }

    public void setRewardHistories(Set<RewardHistory> rewardHistories) {
        if (this.rewardHistories != null) {
            this.rewardHistories.forEach(i -> i.setReward(null));
        }
        if (rewardHistories != null) {
            rewardHistories.forEach(i -> i.setReward(this));
        }
        this.rewardHistories = rewardHistories;
    }

    public Reward rewardHistories(Set<RewardHistory> rewardHistories) {
        this.setRewardHistories(rewardHistories);
        return this;
    }

    public Reward addRewardHistories(RewardHistory rewardHistory) {
        this.rewardHistories.add(rewardHistory);
        rewardHistory.setReward(this);
        return this;
    }

    public Reward removeRewardHistories(RewardHistory rewardHistory) {
        this.rewardHistories.remove(rewardHistory);
        rewardHistory.setReward(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Reward)) {
            return false;
        }
        return getId() != null && getId().equals(((Reward) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Reward{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
