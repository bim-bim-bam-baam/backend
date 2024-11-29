package org.bimbimbambam.hacktemplate.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.bimbimbambam.hacktemplate.controller.request.CategoryCreateReq;
import org.bimbimbambam.hacktemplate.controller.request.CategoryIdReq;
import org.bimbimbambam.hacktemplate.controller.response.CategoryRes;
import org.bimbimbambam.hacktemplate.mapper.CategoryMapper;
import org.bimbimbambam.hacktemplate.service.CategoryService;
import org.bimbimbambam.hacktemplate.utils.Jwt;
import org.bimbimbambam.hacktemplate.utils.JwtUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/category")
@RequiredArgsConstructor
public class CategoryController {
    private final JwtUtils jwtUtils;
    private final CategoryService categoryService;

    private final CategoryMapper categoryMapper;

    @PostMapping("/create")
    @SecurityRequirement(name = "bearerAuth")
    public CategoryRes createCategory(@RequestBody CategoryCreateReq categoryCreateRes) {
        Jwt token = jwtUtils.getJwtToken();
        Long userId = jwtUtils.extractId(token);
        jwtUtils.forceAdminRole(token);
        return categoryMapper.toDto(categoryService.createCategory(categoryCreateRes));
    }

    @DeleteMapping("/delete")
    @SecurityRequirement(name = "bearerAuth")
    public void deleteCategory(@RequestBody CategoryIdReq categoryIdReq) {
        Jwt token = jwtUtils.getJwtToken();
        Long userId = jwtUtils.extractId(token);
        jwtUtils.forceAdminRole(token);
        categoryService.deleteCategory(categoryIdReq.id());
    }

    @GetMapping("/{categoryId}")
    public CategoryRes getCategory(@PathVariable Long categoryId) {
        return categoryMapper.toDto(categoryService.info(categoryId));
    }

    @GetMapping("/all")
    public List<CategoryRes> getCategories() {
        return categoryService.all().stream().map(categoryMapper::toDto).toList();
    }
}
