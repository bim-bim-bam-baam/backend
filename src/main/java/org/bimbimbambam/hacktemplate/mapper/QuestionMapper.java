package org.bimbimbambam.hacktemplate.mapper;

import org.bimbimbambam.hacktemplate.controller.response.QuestionDto;
import org.bimbimbambam.hacktemplate.entity.Category;
import org.bimbimbambam.hacktemplate.entity.Question;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface QuestionMapper {
    @Mapping(source = "categoryId", target = "category.id")
    Question toEntity(QuestionDto questionDto);

    @AfterMapping
    default void linkAnswers(@MappingTarget Question question) {
        question.getAnswers().forEach(answer -> answer.setQuestion(question));
    }

    @Mapping(source = "category.id", target = "categoryId")
    QuestionDto toDto(Question question);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "categoryId", target = "category")
    Question partialUpdate(QuestionDto questionDto, @MappingTarget Question question);

    default Category createCategory(Long categoryId) {
        if (categoryId == null) {
            return null;
        }
        Category category = new Category();
        category.setId(categoryId);
        return category;
    }
}