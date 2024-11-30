package org.bimbimbambam.hacktemplate.mapper;

import org.bimbimbambam.hacktemplate.controller.response.UserDto;
import org.bimbimbambam.hacktemplate.entity.User;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
    User toEntity(UserDto userDto);

    UserDto toDto(User user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    User partialUpdate(UserDto userDto, @MappingTarget User user);
}