package com.nova.star.service.mapper;

import com.nova.star.domain.ProspectBoard;
import com.nova.star.domain.ProspectEntry;
import com.nova.star.domain.User;
import com.nova.star.service.dto.ProspectBoardDTO;
import com.nova.star.service.dto.ProspectEntryDTO;
import com.nova.star.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ProspectEntry} and its DTO {@link ProspectEntryDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProspectEntryMapper extends EntityMapper<ProspectEntryDTO, ProspectEntry> {
    @Mapping(target = "prospect", source = "prospect", qualifiedByName = "userId")
    @Mapping(target = "prospectBoard", source = "prospectBoard", qualifiedByName = "prospectBoardId")
    ProspectEntryDTO toDto(ProspectEntry s);

    @Named("userId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserDTO toDtoUserId(User user);

    @Named("prospectBoardId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ProspectBoardDTO toDtoProspectBoardId(ProspectBoard prospectBoard);
}
