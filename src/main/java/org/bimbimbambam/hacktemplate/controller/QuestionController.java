package org.bimbimbambam.hacktemplate.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.bimbimbambam.hacktemplate.controller.response.QuestionDto;
import org.bimbimbambam.hacktemplate.entity.Question;
import org.bimbimbambam.hacktemplate.mapper.QuestionMapper;
import org.bimbimbambam.hacktemplate.service.QuestionService;
import org.bimbimbambam.hacktemplate.service.UserService;
import org.bimbimbambam.hacktemplate.utils.Jwt;
import org.bimbimbambam.hacktemplate.utils.JwtUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/question")
public class QuestionController {
    private final JwtUtils jwtUtils;
    private final UserService userService;
    private final QuestionService questionService;

    private final QuestionMapper questionMapper;


    @GetMapping("/getNextQuestion")
    @SecurityRequirement(name = "bearerAuth")
    public Question getNextQuestionForUser(Long categoryId) {
        Jwt token = jwtUtils.getJwtToken();
        Long userId = jwtUtils.extractId(token);
        return userService.getNextQuestion(userId, categoryId);
    }

    @GetMapping("/setAnswer")
    @SecurityRequirement(name = "bearerAuth")
    public void setQuestionForUser(Long questionId, Long result) {
        Jwt token = jwtUtils.getJwtToken();
        Long userId = jwtUtils.extractId(token);
        userService.answerQuestion(userId, questionId, result);
    }


    @PostMapping(value = "/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @SecurityRequirement(name = "bearerAuth")
    public QuestionDto addQuestion(String content, MultipartFile file, Long categoryId) {
        Jwt token = jwtUtils.getJwtToken();
        Long userId = jwtUtils.extractId(token);
        jwtUtils.forceAdminRole(token);
        return questionMapper.toDto(questionService.addQuestion(
                content,
                file,
                categoryId
        ));
    }

    @DeleteMapping("/delete/{id}")
    @SecurityRequirement(name = "bearerAuth")
    public void deleteQuestion(@PathVariable Long id) {
        Jwt token = jwtUtils.getJwtToken();
        Long userId = jwtUtils.extractId(token);
        jwtUtils.forceAdminRole(token);
        questionService.deleteQuestion(id);
    }

    @GetMapping("/allByCategory")
    public List<QuestionDto> all(Long categoryId) {
        return questionService.all(categoryId).stream().map(questionMapper::toDto).toList();
    }

    @GetMapping("/all")
    public List<QuestionDto> all() {
        return questionService.all().stream().map(questionMapper::toDto).toList();
    }
}