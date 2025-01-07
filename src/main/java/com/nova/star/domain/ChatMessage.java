package com.nova.star.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ChatMessage.
 */
@Entity
@Table(name = "chat_message")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ChatMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Lob
    @Column(name = "message", nullable = false)
    private String message;

    @NotNull
    @Column(name = "created_date", nullable = false)
    private ZonedDateTime createdDate;

    @Column(name = "like_count")
    private Integer likeCount;

    @ManyToOne(fetch = FetchType.LAZY)
    private User author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "messages", "users" }, allowSetters = true)
    private GlobalChat globalChat;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "messages", "users" }, allowSetters = true)
    private IdeaChat ideaChat;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ChatMessage id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return this.message;
    }

    public ChatMessage message(String message) {
        this.setMessage(message);
        return this;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ZonedDateTime getCreatedDate() {
        return this.createdDate;
    }

    public ChatMessage createdDate(ZonedDateTime createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public Integer getLikeCount() {
        return this.likeCount;
    }

    public ChatMessage likeCount(Integer likeCount) {
        this.setLikeCount(likeCount);
        return this;
    }

    public void setLikeCount(Integer likeCount) {
        this.likeCount = likeCount;
    }

    public User getAuthor() {
        return this.author;
    }

    public void setAuthor(User user) {
        this.author = user;
    }

    public ChatMessage author(User user) {
        this.setAuthor(user);
        return this;
    }

    public GlobalChat getGlobalChat() {
        return this.globalChat;
    }

    public void setGlobalChat(GlobalChat globalChat) {
        this.globalChat = globalChat;
    }

    public ChatMessage globalChat(GlobalChat globalChat) {
        this.setGlobalChat(globalChat);
        return this;
    }

    public IdeaChat getIdeaChat() {
        return this.ideaChat;
    }

    public void setIdeaChat(IdeaChat ideaChat) {
        this.ideaChat = ideaChat;
    }

    public ChatMessage ideaChat(IdeaChat ideaChat) {
        this.setIdeaChat(ideaChat);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ChatMessage)) {
            return false;
        }
        return getId() != null && getId().equals(((ChatMessage) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ChatMessage{" +
            "id=" + getId() +
            ", message='" + getMessage() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", likeCount=" + getLikeCount() +
            "}";
    }
}
