package com.nova.star.service.mapper;

import com.nova.star.domain.ChatMessage;
import com.nova.star.domain.GlobalChat;
import com.nova.star.domain.IdeaChat;
import com.nova.star.domain.User;
import com.nova.star.service.dto.ChatMessageDTO;
import com.nova.star.service.dto.GlobalChatDTO;
import com.nova.star.service.dto.IdeaChatDTO;
import com.nova.star.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ChatMessage} and its DTO {@link ChatMessageDTO}.
 */
@Mapper(componentModel = "spring")
public interface ChatMessageMapper extends EntityMapper<ChatMessageDTO, ChatMessage> {
    @Mapping(target = "author", source = "author", qualifiedByName = "userId")
    @Mapping(target = "globalChat", source = "globalChat", qualifiedByName = "globalChatId")
    @Mapping(target = "ideaChat", source = "ideaChat", qualifiedByName = "ideaChatId")
    ChatMessageDTO toDto(ChatMessage s);

    @Named("userId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserDTO toDtoUserId(User user);

    @Named("globalChatId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    GlobalChatDTO toDtoGlobalChatId(GlobalChat globalChat);

    @Named("ideaChatId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    IdeaChatDTO toDtoIdeaChatId(IdeaChat ideaChat);
}
