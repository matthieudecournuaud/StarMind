package com.nova.star.repository;

import com.nova.star.domain.IdeaChat;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface IdeaChatRepositoryWithBagRelationships {
    Optional<IdeaChat> fetchBagRelationships(Optional<IdeaChat> ideaChat);

    List<IdeaChat> fetchBagRelationships(List<IdeaChat> ideaChats);

    Page<IdeaChat> fetchBagRelationships(Page<IdeaChat> ideaChats);
}
