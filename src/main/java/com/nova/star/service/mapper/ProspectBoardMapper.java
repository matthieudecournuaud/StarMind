package com.nova.star.service.mapper;

import com.nova.star.domain.ProspectBoard;
import com.nova.star.domain.User;
import com.nova.star.service.dto.ProspectBoardDTO;
import com.nova.star.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ProspectBoard} and its DTO {@link ProspectBoardDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProspectBoardMapper extends EntityMapper<ProspectBoardDTO, ProspectBoard> {
    @Mapping(target = "owner", source = "owner", qualifiedByName = "userId")
    ProspectBoardDTO toDto(ProspectBoard s);

    @Named("userId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserDTO toDtoUserId(User user);
}
