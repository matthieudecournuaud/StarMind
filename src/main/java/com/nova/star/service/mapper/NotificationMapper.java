package com.nova.star.service.mapper;

import com.nova.star.domain.Notification;
import com.nova.star.domain.User;
import com.nova.star.service.dto.NotificationDTO;
import com.nova.star.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Notification} and its DTO {@link NotificationDTO}.
 */
@Mapper(componentModel = "spring")
public interface NotificationMapper extends EntityMapper<NotificationDTO, Notification> {
    @Mapping(target = "user", source = "user", qualifiedByName = "userId")
    NotificationDTO toDto(Notification s);

    @Named("userId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserDTO toDtoUserId(User user);
}
