package org.bimbimbambam.hacktemplate.mapper;

import org.bimbimbambam.hacktemplate.controller.response.ChatDto;
import org.bimbimbambam.hacktemplate.entity.Chat;
import org.bimbimbambam.hacktemplate.entity.User;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface ChatMapper {
    @Mapping(source = "toUserId", target = "toUser.id")
    @Mapping(source = "fromUserId", target = "fromUser.id")
    Chat toEntity(ChatDto chatDto);

    @InheritInverseConfiguration(name = "toEntity")
    ChatDto toDto(Chat chat);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "toUserId", target = "toUser")
    @Mapping(source = "fromUserId", target = "fromUser")
    Chat partialUpdate(ChatDto chatDto, @MappingTarget Chat chat);

    default User createUser(Long fromUserId) {
        if (fromUserId == null) {
            return null;
        }
        User user = new User();
        user.setId(fromUserId);
        return user;
    }
}
