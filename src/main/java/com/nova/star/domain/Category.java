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
 * A Category.
 */
@Entity
@Table(name = "category")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Category implements Serializable {

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
    @Column(name = "level", nullable = false)
    private String level;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "category")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "comments", "author", "ideaCategory", "assignedReward", "category", "reward" }, allowSetters = true)
    private Set<Idea> ideas = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "superCategory")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "ideas", "subcategories", "parentCategory", "superCategory" }, allowSetters = true)
    private Set<Category> subcategories = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "ideas", "subcategories", "parentCategory", "superCategory" }, allowSetters = true)
    private Category parentCategory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "ideas", "subcategories", "parentCategory", "superCategory" }, allowSetters = true)
    private Category superCategory;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Category id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Category name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public Category description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLevel() {
        return this.level;
    }

    public Category level(String level) {
        this.setLevel(level);
        return this;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public Set<Idea> getIdeas() {
        return this.ideas;
    }

    public void setIdeas(Set<Idea> ideas) {
        if (this.ideas != null) {
            this.ideas.forEach(i -> i.setCategory(null));
        }
        if (ideas != null) {
            ideas.forEach(i -> i.setCategory(this));
        }
        this.ideas = ideas;
    }

    public Category ideas(Set<Idea> ideas) {
        this.setIdeas(ideas);
        return this;
    }

    public Category addIdeas(Idea idea) {
        this.ideas.add(idea);
        idea.setCategory(this);
        return this;
    }

    public Category removeIdeas(Idea idea) {
        this.ideas.remove(idea);
        idea.setCategory(null);
        return this;
    }

    public Set<Category> getSubcategories() {
        return this.subcategories;
    }

    public void setSubcategories(Set<Category> categories) {
        if (this.subcategories != null) {
            this.subcategories.forEach(i -> i.setSuperCategory(null));
        }
        if (categories != null) {
            categories.forEach(i -> i.setSuperCategory(this));
        }
        this.subcategories = categories;
    }

    public Category subcategories(Set<Category> categories) {
        this.setSubcategories(categories);
        return this;
    }

    public Category addSubcategories(Category category) {
        this.subcategories.add(category);
        category.setSuperCategory(this);
        return this;
    }

    public Category removeSubcategories(Category category) {
        this.subcategories.remove(category);
        category.setSuperCategory(null);
        return this;
    }

    public Category getParentCategory() {
        return this.parentCategory;
    }

    public void setParentCategory(Category category) {
        this.parentCategory = category;
    }

    public Category parentCategory(Category category) {
        this.setParentCategory(category);
        return this;
    }

    public Category getSuperCategory() {
        return this.superCategory;
    }

    public void setSuperCategory(Category category) {
        this.superCategory = category;
    }

    public Category superCategory(Category category) {
        this.setSuperCategory(category);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Category)) {
            return false;
        }
        return getId() != null && getId().equals(((Category) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Category{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", level='" + getLevel() + "'" +
            "}";
    }
}
