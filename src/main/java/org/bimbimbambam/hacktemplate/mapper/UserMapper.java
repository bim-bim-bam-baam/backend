package org.bimbimbambam.hacktemplate.mapper;

import org.bimbimbambam.hacktemplate.entity.User;
import org.bimbimbambam.hacktemplate.controller.response.UserProfileDto;
import org.mapstruct.Mapper;

@Mapper(unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE, componentModel = "spring")
public interface UserMapper {
    User toEntity(UserProfileDto userProfileDto);

    UserProfileDto toDto(User user);

    @org.mapstruct.BeanMapping(nullValuePropertyMappingStrategy = org.mapstruct.NullValuePropertyMappingStrategy.IGNORE)
    User partialUpdate(UserProfileDto userProfileDto, @org.mapstruct.MappingTarget User user);
}
