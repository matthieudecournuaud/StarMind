package com.nova.star.service.mapper;

import com.nova.star.domain.Comment;
import com.nova.star.domain.Idea;
import com.nova.star.domain.User;
import com.nova.star.service.dto.CommentDTO;
import com.nova.star.service.dto.IdeaDTO;
import com.nova.star.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Comment} and its DTO {@link CommentDTO}.
 */
@Mapper(componentModel = "spring")
public interface CommentMapper extends EntityMapper<CommentDTO, Comment> {
    @Mapping(target = "author", source = "author", qualifiedByName = "userId")
    @Mapping(target = "idea", source = "idea", qualifiedByName = "ideaId")
    CommentDTO toDto(Comment s);

    @Named("userId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserDTO toDtoUserId(User user);

    @Named("ideaId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    IdeaDTO toDtoIdeaId(Idea idea);
}
