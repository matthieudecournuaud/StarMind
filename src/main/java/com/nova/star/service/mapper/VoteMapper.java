package com.nova.star.service.mapper;

import com.nova.star.domain.Idea;
import com.nova.star.domain.User;
import com.nova.star.domain.Vote;
import com.nova.star.service.dto.IdeaDTO;
import com.nova.star.service.dto.UserDTO;
import com.nova.star.service.dto.VoteDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Vote} and its DTO {@link VoteDTO}.
 */
@Mapper(componentModel = "spring")
public interface VoteMapper extends EntityMapper<VoteDTO, Vote> {
    @Mapping(target = "voter", source = "voter", qualifiedByName = "userId")
    @Mapping(target = "idea", source = "idea", qualifiedByName = "ideaId")
    VoteDTO toDto(Vote s);

    @Named("userId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserDTO toDtoUserId(User user);

    @Named("ideaId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    IdeaDTO toDtoIdeaId(Idea idea);
}
