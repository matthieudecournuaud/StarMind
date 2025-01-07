package com.nova.star.service.dto;

import com.nova.star.domain.enumeration.IdeaStatus;
import com.nova.star.domain.enumeration.RewardType;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the {@link com.nova.star.domain.Idea} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class IdeaDTO implements Serializable {

    private Long id;

    @NotNull
    private String title;

    @Lob
    private String description;

    @NotNull
    private IdeaStatus status;

    @NotNull
    private Boolean isConfidential;

    private Boolean validation;

    private RewardType rewardType;

    private Integer likes;

    private ZonedDateTime createdDate;

    private ZonedDateTime modifiedDate;

    private Boolean isPublic;

    private String impact;

    private UserDTO author;

    private RewardDTO assignedReward;

    private UserDTO manager;

    private CategoryDTO category;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public IdeaStatus getStatus() {
        return status;
    }

    public void setStatus(IdeaStatus status) {
        this.status = status;
    }

    public Boolean getIsConfidential() {
        return isConfidential;
    }

    public void setIsConfidential(Boolean isConfidential) {
        this.isConfidential = isConfidential;
    }

    public Boolean getValidation() {
        return validation;
    }

    public void setValidation(Boolean validation) {
        this.validation = validation;
    }

    public RewardType getRewardType() {
        return rewardType;
    }

    public void setRewardType(RewardType rewardType) {
        this.rewardType = rewardType;
    }

    public Integer getLikes() {
        return likes;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
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

    public Boolean getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(Boolean isPublic) {
        this.isPublic = isPublic;
    }

    public String getImpact() {
        return impact;
    }

    public void setImpact(String impact) {
        this.impact = impact;
    }

    public UserDTO getAuthor() {
        return author;
    }

    public void setAuthor(UserDTO author) {
        this.author = author;
    }

    public RewardDTO getAssignedReward() {
        return assignedReward;
    }

    public void setAssignedReward(RewardDTO assignedReward) {
        this.assignedReward = assignedReward;
    }

    public UserDTO getManager() {
        return manager;
    }

    public void setManager(UserDTO manager) {
        this.manager = manager;
    }

    public CategoryDTO getCategory() {
        return category;
    }

    public void setCategory(CategoryDTO category) {
        this.category = category;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof IdeaDTO)) {
            return false;
        }

        IdeaDTO ideaDTO = (IdeaDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, ideaDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "IdeaDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", status='" + getStatus() + "'" +
            ", isConfidential='" + getIsConfidential() + "'" +
            ", validation='" + getValidation() + "'" +
            ", rewardType='" + getRewardType() + "'" +
            ", likes=" + getLikes() +
            ", createdDate='" + getCreatedDate() + "'" +
            ", modifiedDate='" + getModifiedDate() + "'" +
            ", isPublic='" + getIsPublic() + "'" +
            ", impact='" + getImpact() + "'" +
            ", author=" + getAuthor() +
            ", assignedReward=" + getAssignedReward() +
            ", manager=" + getManager() +
            ", category=" + getCategory() +
            "}";
    }
}
