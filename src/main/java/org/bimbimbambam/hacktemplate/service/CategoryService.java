package org.bimbimbambam.hacktemplate.service;

import org.bimbimbambam.hacktemplate.controller.CategoryController;
import org.bimbimbambam.hacktemplate.controller.request.CategoryCreateReq;
import org.bimbimbambam.hacktemplate.controller.response.CategoryRes;
import org.bimbimbambam.hacktemplate.entity.Category;

public interface CategoryService {
    Category createCategory(CategoryCreateReq categoryCreateRes);

    Category info(Long id);

    void deleteCategory(Long id);
}
