package com.nova.star.repository;

import com.nova.star.domain.GlobalChat;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface GlobalChatRepositoryWithBagRelationships {
    Optional<GlobalChat> fetchBagRelationships(Optional<GlobalChat> globalChat);

    List<GlobalChat> fetchBagRelationships(List<GlobalChat> globalChats);

    Page<GlobalChat> fetchBagRelationships(Page<GlobalChat> globalChats);
}
