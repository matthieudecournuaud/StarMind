package com.nova.star.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link com.nova.star.domain.IdeaChat} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class IdeaChatDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    private Set<UserDTO> users = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<UserDTO> getUsers() {
        return users;
    }

    public void setUsers(Set<UserDTO> users) {
        this.users = users;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof IdeaChatDTO)) {
            return false;
        }

        IdeaChatDTO ideaChatDTO = (IdeaChatDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, ideaChatDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "IdeaChatDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", users=" + getUsers() +
            "}";
    }
}
