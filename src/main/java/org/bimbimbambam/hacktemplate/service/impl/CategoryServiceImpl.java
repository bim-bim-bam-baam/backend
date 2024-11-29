package org.bimbimbambam.hacktemplate.service.impl;

import org.bimbimbambam.hacktemplate.controller.request.CategoryCreateReq;
import org.bimbimbambam.hacktemplate.entity.Category;
import org.bimbimbambam.hacktemplate.exception.NotFoundException;
import org.bimbimbambam.hacktemplate.mapper.CategoryCreateMapper;
import org.bimbimbambam.hacktemplate.repository.CategoryRepository;
import org.bimbimbambam.hacktemplate.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryCreateMapper categoryCreateMapper;

    @Override
    public Category createCategory(CategoryCreateReq categoryCreateReq) {

        Category category = categoryCreateMapper.toEntity(categoryCreateReq);
        return categoryRepository.save(category);
    }

    @Override
    public Category info(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Catefory does not exist"));
    }

    @Override
    public void deleteCategory(Long id) {
        if (categoryRepository.existsById(id)) {
            categoryRepository.deleteById(id);
        } else {
            throw new NotFoundException("Category does not exist.");
        }
    }
}
