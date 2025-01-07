package com.nova.star.repository;

import com.nova.star.domain.ChatMessage;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ChatMessage entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    @Query("select chatMessage from ChatMessage chatMessage where chatMessage.author.login = ?#{authentication.name}")
    List<ChatMessage> findByAuthorIsCurrentUser();
}
