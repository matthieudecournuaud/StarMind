package com.nova.star.repository;

import com.nova.star.domain.GlobalChat;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class GlobalChatRepositoryWithBagRelationshipsImpl implements GlobalChatRepositoryWithBagRelationships {

    private static final String ID_PARAMETER = "id";
    private static final String GLOBALCHATS_PARAMETER = "globalChats";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<GlobalChat> fetchBagRelationships(Optional<GlobalChat> globalChat) {
        return globalChat.map(this::fetchUsers);
    }

    @Override
    public Page<GlobalChat> fetchBagRelationships(Page<GlobalChat> globalChats) {
        return new PageImpl<>(fetchBagRelationships(globalChats.getContent()), globalChats.getPageable(), globalChats.getTotalElements());
    }

    @Override
    public List<GlobalChat> fetchBagRelationships(List<GlobalChat> globalChats) {
        return Optional.of(globalChats).map(this::fetchUsers).orElse(Collections.emptyList());
    }

    GlobalChat fetchUsers(GlobalChat result) {
        return entityManager
            .createQuery(
                "select globalChat from GlobalChat globalChat left join fetch globalChat.users where globalChat.id = :id",
                GlobalChat.class
            )
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<GlobalChat> fetchUsers(List<GlobalChat> globalChats) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, globalChats.size()).forEach(index -> order.put(globalChats.get(index).getId(), index));
        List<GlobalChat> result = entityManager
            .createQuery(
                "select globalChat from GlobalChat globalChat left join fetch globalChat.users where globalChat in :globalChats",
                GlobalChat.class
            )
            .setParameter(GLOBALCHATS_PARAMETER, globalChats)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
