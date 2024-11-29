package org.bimbimbambam.hacktemplate.mapper;

import org.bimbimbambam.hacktemplate.controller.response.MessageDto;
import org.bimbimbambam.hacktemplate.entity.Chat;
import org.bimbimbambam.hacktemplate.entity.Message;
import org.bimbimbambam.hacktemplate.entity.User;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface MessageMapper {
    @Mapping(source = "authorId", target = "author.id")
    @Mapping(source = "chatId", target = "chat.id")
    Message toEntity(MessageDto messageDto);

    @InheritInverseConfiguration(name = "toEntity")
    MessageDto toDto(Message message);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "authorId", target = "author")
    @Mapping(source = "chatId", target = "chat")
    Message partialUpdate(MessageDto messageDto, @MappingTarget Message message);

    default Chat createChat(Long chatId) {
        if (chatId == null) {
            return null;
        }
        Chat chat = new Chat();
        chat.setId(chatId);
        return chat;
    }

    default User createUser(Long authorId) {
        if (authorId == null) {
            return null;
        }
        User user = new User();
        user.setId(authorId);
        return user;
    }
}
