package com.nova.star.service.dto;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the {@link com.nova.star.domain.ChatMessage} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ChatMessageDTO implements Serializable {

    private Long id;

    @Lob
    private String message;

    @NotNull
    private ZonedDateTime createdDate;

    private Integer likeCount;

    private UserDTO author;

    private GlobalChatDTO globalChat;

    private IdeaChatDTO ideaChat;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ZonedDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public Integer getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(Integer likeCount) {
        this.likeCount = likeCount;
    }

    public UserDTO getAuthor() {
        return author;
    }

    public void setAuthor(UserDTO author) {
        this.author = author;
    }

    public GlobalChatDTO getGlobalChat() {
        return globalChat;
    }

    public void setGlobalChat(GlobalChatDTO globalChat) {
        this.globalChat = globalChat;
    }

    public IdeaChatDTO getIdeaChat() {
        return ideaChat;
    }

    public void setIdeaChat(IdeaChatDTO ideaChat) {
        this.ideaChat = ideaChat;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ChatMessageDTO)) {
            return false;
        }

        ChatMessageDTO chatMessageDTO = (ChatMessageDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, chatMessageDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ChatMessageDTO{" +
            "id=" + getId() +
            ", message='" + getMessage() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", likeCount=" + getLikeCount() +
            ", author=" + getAuthor() +
            ", globalChat=" + getGlobalChat() +
            ", ideaChat=" + getIdeaChat() +
            "}";
    }
}
