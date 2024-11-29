package org.bimbimbambam.hacktemplate.service;

import org.bimbimbambam.hacktemplate.controller.request.CategoryCreateReq;
import org.bimbimbambam.hacktemplate.entity.Category;

import java.util.List;

public interface CategoryService {
    Category createCategory(CategoryCreateReq categoryCreateRes);

    Category info(Long id);
    List<Category> all();


    void deleteCategory(Long id);
}
