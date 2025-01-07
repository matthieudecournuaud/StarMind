package com.nova.star.service.mapper;

import com.nova.star.domain.Idea;
import com.nova.star.domain.Reward;
import com.nova.star.domain.RewardHistory;
import com.nova.star.service.dto.IdeaDTO;
import com.nova.star.service.dto.RewardDTO;
import com.nova.star.service.dto.RewardHistoryDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link RewardHistory} and its DTO {@link RewardHistoryDTO}.
 */
@Mapper(componentModel = "spring")
public interface RewardHistoryMapper extends EntityMapper<RewardHistoryDTO, RewardHistory> {
    @Mapping(target = "idea", source = "idea", qualifiedByName = "ideaId")
    @Mapping(target = "reward", source = "reward", qualifiedByName = "rewardId")
    RewardHistoryDTO toDto(RewardHistory s);

    @Named("ideaId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    IdeaDTO toDtoIdeaId(Idea idea);

    @Named("rewardId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    RewardDTO toDtoRewardId(Reward reward);
}
