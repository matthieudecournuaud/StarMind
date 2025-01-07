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
 * A GlobalChat.
 */
@Entity
@Table(name = "global_chat")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class GlobalChat implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "globalChat")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "author", "globalChat", "ideaChat" }, allowSetters = true)
    private Set<ChatMessage> messages = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_global_chat__users",
        joinColumns = @JoinColumn(name = "global_chat_id"),
        inverseJoinColumns = @JoinColumn(name = "users_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<User> users = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public GlobalChat id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public GlobalChat name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<ChatMessage> getMessages() {
        return this.messages;
    }

    public void setMessages(Set<ChatMessage> chatMessages) {
        if (this.messages != null) {
            this.messages.forEach(i -> i.setGlobalChat(null));
        }
        if (chatMessages != null) {
            chatMessages.forEach(i -> i.setGlobalChat(this));
        }
        this.messages = chatMessages;
    }

    public GlobalChat messages(Set<ChatMessage> chatMessages) {
        this.setMessages(chatMessages);
        return this;
    }

    public GlobalChat addMessages(ChatMessage chatMessage) {
        this.messages.add(chatMessage);
        chatMessage.setGlobalChat(this);
        return this;
    }

    public GlobalChat removeMessages(ChatMessage chatMessage) {
        this.messages.remove(chatMessage);
        chatMessage.setGlobalChat(null);
        return this;
    }

    public Set<User> getUsers() {
        return this.users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public GlobalChat users(Set<User> users) {
        this.setUsers(users);
        return this;
    }

    public GlobalChat addUsers(User user) {
        this.users.add(user);
        return this;
    }

    public GlobalChat removeUsers(User user) {
        this.users.remove(user);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GlobalChat)) {
            return false;
        }
        return getId() != null && getId().equals(((GlobalChat) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GlobalChat{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
