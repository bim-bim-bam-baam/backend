package org.bimbimbambam.hacktemplate.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.bimbimbambam.hacktemplate.controller.request.QuestionReq;
import org.bimbimbambam.hacktemplate.controller.response.QuestionDto;
import org.bimbimbambam.hacktemplate.entity.Question;
import org.bimbimbambam.hacktemplate.entity.User;
import org.bimbimbambam.hacktemplate.mapper.QuestionMapper;
import org.bimbimbambam.hacktemplate.service.QuestionService;
import org.bimbimbambam.hacktemplate.service.UserService;
import org.bimbimbambam.hacktemplate.service.impl.UserServiceImpl;
import org.bimbimbambam.hacktemplate.utils.Jwt;
import org.bimbimbambam.hacktemplate.utils.JwtUtils;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public QuestionDto addQuestion(
            @RequestPart("questionReq") QuestionReq questionReq,
            @RequestParam("categoryId") Long categoryId) {
        Jwt token = jwtUtils.getJwtToken();
        Long userId = jwtUtils.extractId(token);
        jwtUtils.forceAdminRole(token);
        return questionMapper.toDto(questionService.addQuestion(
                questionReq.content(),
                questionReq.file(),
                categoryId
        ));
    }

    @DeleteMapping("/delete/{id}")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Void> deleteQuestion(@PathVariable Long id) {
        Jwt token = jwtUtils.getJwtToken();
        Long userId = jwtUtils.extractId(token);
        jwtUtils.forceAdminRole(token);
        questionService.deleteQuestion(id);
        return ResponseEntity.noContent().build();
    }
}
