package com.nova.star.repository;

import com.nova.star.domain.IdeaChat;
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
public class IdeaChatRepositoryWithBagRelationshipsImpl implements IdeaChatRepositoryWithBagRelationships {

    private static final String ID_PARAMETER = "id";
    private static final String IDEACHATS_PARAMETER = "ideaChats";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<IdeaChat> fetchBagRelationships(Optional<IdeaChat> ideaChat) {
        return ideaChat.map(this::fetchUsers);
    }

    @Override
    public Page<IdeaChat> fetchBagRelationships(Page<IdeaChat> ideaChats) {
        return new PageImpl<>(fetchBagRelationships(ideaChats.getContent()), ideaChats.getPageable(), ideaChats.getTotalElements());
    }

    @Override
    public List<IdeaChat> fetchBagRelationships(List<IdeaChat> ideaChats) {
        return Optional.of(ideaChats).map(this::fetchUsers).orElse(Collections.emptyList());
    }

    IdeaChat fetchUsers(IdeaChat result) {
        return entityManager
            .createQuery("select ideaChat from IdeaChat ideaChat left join fetch ideaChat.users where ideaChat.id = :id", IdeaChat.class)
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<IdeaChat> fetchUsers(List<IdeaChat> ideaChats) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, ideaChats.size()).forEach(index -> order.put(ideaChats.get(index).getId(), index));
        List<IdeaChat> result = entityManager
            .createQuery(
                "select ideaChat from IdeaChat ideaChat left join fetch ideaChat.users where ideaChat in :ideaChats",
                IdeaChat.class
            )
            .setParameter(IDEACHATS_PARAMETER, ideaChats)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
