package org.bimbimbambam.hacktemplate.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.bimbimbambam.hacktemplate.controller.request.CategoryCreateReq;
import org.bimbimbambam.hacktemplate.controller.response.CategoryRes;
import org.bimbimbambam.hacktemplate.mapper.CategoryMapper;
import org.bimbimbambam.hacktemplate.service.CategoryService;
import org.bimbimbambam.hacktemplate.utils.Jwt;
import org.bimbimbambam.hacktemplate.utils.JwtUtils;
import org.springframework.web.bind.annotation.*;

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
    public void deleteCategory(@RequestBody Long id) {
        Jwt token = jwtUtils.getJwtToken();
        Long userId = jwtUtils.extractId(token);
        jwtUtils.forceAdminRole(token);
        categoryService.deleteCategory(id);
    }

    @GetMapping("/info")
    public CategoryRes info(@RequestBody Long id) {
        return categoryMapper.toDto(categoryService.info(id));
    }
}