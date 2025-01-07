package com.nova.star.service.mapper;

import com.nova.star.domain.IdeaChat;
import com.nova.star.domain.User;
import com.nova.star.service.dto.IdeaChatDTO;
import com.nova.star.service.dto.UserDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link IdeaChat} and its DTO {@link IdeaChatDTO}.
 */
@Mapper(componentModel = "spring")
public interface IdeaChatMapper extends EntityMapper<IdeaChatDTO, IdeaChat> {
    @Mapping(target = "users", source = "users", qualifiedByName = "userIdSet")
    IdeaChatDTO toDto(IdeaChat s);

    @Mapping(target = "removeUsers", ignore = true)
    IdeaChat toEntity(IdeaChatDTO ideaChatDTO);

    @Named("userId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserDTO toDtoUserId(User user);

    @Named("userIdSet")
    default Set<UserDTO> toDtoUserIdSet(Set<User> user) {
        return user.stream().map(this::toDtoUserId).collect(Collectors.toSet());
    }
}
