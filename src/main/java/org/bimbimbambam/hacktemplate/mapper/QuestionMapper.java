package org.bimbimbambam.hacktemplate.mapper;

import org.bimbimbambam.hacktemplate.controller.response.QuestionDto;
import org.bimbimbambam.hacktemplate.entity.Question;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface QuestionMapper {
    Question toEntity(QuestionDto questionDto);

    QuestionDto toDto(Question question);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Question partialUpdate(QuestionDto questionDto, @MappingTarget Question question);
}
