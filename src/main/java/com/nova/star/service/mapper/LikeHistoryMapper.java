package com.nova.star.service.mapper;

import com.nova.star.domain.Idea;
import com.nova.star.domain.LikeHistory;
import com.nova.star.service.dto.IdeaDTO;
import com.nova.star.service.dto.LikeHistoryDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link LikeHistory} and its DTO {@link LikeHistoryDTO}.
 */
@Mapper(componentModel = "spring")
public interface LikeHistoryMapper extends EntityMapper<LikeHistoryDTO, LikeHistory> {
    @Mapping(target = "idea", source = "idea", qualifiedByName = "ideaId")
    LikeHistoryDTO toDto(LikeHistory s);

    @Named("ideaId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    IdeaDTO toDtoIdeaId(Idea idea);
}
