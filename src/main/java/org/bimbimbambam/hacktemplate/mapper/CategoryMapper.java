package org.bimbimbambam.hacktemplate.mapper;

import org.bimbimbambam.hacktemplate.controller.response.CategoryRes;
import org.bimbimbambam.hacktemplate.entity.Category;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface CategoryMapper {
    Category toEntity(CategoryRes categoryRes);

    CategoryRes toDto(Category category);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Category partialUpdate(CategoryRes categoryRes, @MappingTarget Category category);
}
