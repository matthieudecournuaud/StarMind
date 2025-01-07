package com.nova.star.service.mapper;

import com.nova.star.domain.Category;
import com.nova.star.domain.Idea;
import com.nova.star.domain.Reward;
import com.nova.star.domain.User;
import com.nova.star.service.dto.CategoryDTO;
import com.nova.star.service.dto.IdeaDTO;
import com.nova.star.service.dto.RewardDTO;
import com.nova.star.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Idea} and its DTO {@link IdeaDTO}.
 */
@Mapper(componentModel = "spring")
public interface IdeaMapper extends EntityMapper<IdeaDTO, Idea> {
    @Mapping(target = "author", source = "author", qualifiedByName = "userId")
    @Mapping(target = "assignedReward", source = "assignedReward", qualifiedByName = "rewardId")
    @Mapping(target = "manager", source = "manager", qualifiedByName = "userId")
    @Mapping(target = "category", source = "category", qualifiedByName = "categoryId")
    IdeaDTO toDto(Idea s);

    @Named("userId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserDTO toDtoUserId(User user);

    @Named("rewardId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    RewardDTO toDtoRewardId(Reward reward);

    @Named("categoryId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CategoryDTO toDtoCategoryId(Category category);
}
