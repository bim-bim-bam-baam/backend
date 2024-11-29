package org.bimbimbambam.hacktemplate.mapper;

import org.bimbimbambam.hacktemplate.controller.request.CategoryCreateReq;
import org.bimbimbambam.hacktemplate.entity.Category;
import org.bimbimbambam.hacktemplate.controller.response.CategoryRes;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface CategoryMapper {
    Category toEntity(CategoryRes categoryCreateRes);

    CategoryRes toDto(Category category);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Category partialUpdate(CategoryRes categoryCreateRes, @MappingTarget Category category);

    Category toEntity(CategoryCreateReq categoryDto);

    CategoryCreateReq toDto1(Category category);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Category partialUpdate(CategoryCreateReq categoryDto, @MappingTarget Category category);
}