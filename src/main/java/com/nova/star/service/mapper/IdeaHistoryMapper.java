package com.nova.star.service.mapper;

import com.nova.star.domain.Idea;
import com.nova.star.domain.IdeaHistory;
import com.nova.star.service.dto.IdeaDTO;
import com.nova.star.service.dto.IdeaHistoryDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link IdeaHistory} and its DTO {@link IdeaHistoryDTO}.
 */
@Mapper(componentModel = "spring")
public interface IdeaHistoryMapper extends EntityMapper<IdeaHistoryDTO, IdeaHistory> {
    @Mapping(target = "idea", source = "idea", qualifiedByName = "ideaId")
    IdeaHistoryDTO toDto(IdeaHistory s);

    @Named("ideaId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    IdeaDTO toDtoIdeaId(Idea idea);
}
