package com.nova.star.service.mapper;

import com.nova.star.domain.Reward;
import com.nova.star.service.dto.RewardDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Reward} and its DTO {@link RewardDTO}.
 */
@Mapper(componentModel = "spring")
public interface RewardMapper extends EntityMapper<RewardDTO, Reward> {}
