package org.bimbimbambam.hacktemplate.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.bimbimbambam.hacktemplate.controller.request.user.UserLoginReq;
import org.bimbimbambam.hacktemplate.controller.request.user.UserRegisterReq;
import org.bimbimbambam.hacktemplate.controller.request.user.UserUpdateAvatarReq;
import org.bimbimbambam.hacktemplate.entity.Question;
import org.bimbimbambam.hacktemplate.entity.User;
import org.bimbimbambam.hacktemplate.service.impl.UserServiceImpl;
import org.bimbimbambam.hacktemplate.utils.Jwt;
import org.bimbimbambam.hacktemplate.utils.JwtUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {
    private final UserServiceImpl userService;
    private final JwtUtils jwtUtils;

    @PostMapping("/register")
    public void registerUser(@RequestBody UserRegisterReq userRegisterReq) {
        userService.registerUser(userRegisterReq);
    }

    @PostMapping("/login")
    public Jwt loginUser(@RequestBody UserLoginReq userLoginReq) {
        return userService.loginUser(userLoginReq);
    }

    @PostMapping(value = "/updateAvatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @SecurityRequirement(name = "bearerAuth")
    public void updateAvatar(
            @ModelAttribute UserUpdateAvatarReq userUpdateAvatarReq) {
        Jwt token = jwtUtils.getJwtToken();
        Long userId = jwtUtils.extractId(token);
        userService.updateAvatar(userId, userUpdateAvatarReq);
    }

    @GetMapping("/profile")
    @SecurityRequirement(name = "bearerAuth")
    public User getUserProfile() {
        Jwt token = jwtUtils.getJwtToken();
        Long userId = jwtUtils.extractId(token);
        return userService.getUser(userId);
    }

    @GetMapping("/getNextQuestion")
    @SecurityRequirement(name = "bearerAuth")
    public Question getNextQuestionForUser(Long categoryId) {
        Jwt token = jwtUtils.getJwtToken();
        Long userId = jwtUtils.extractId(token);
        return userService.getNextQuestion(userId, categoryId);
    }

    @GetMapping("/setQuestion")
    @SecurityRequirement(name = "bearerAuth")
    public void setQuestionForUser(Long questionId, Long result) {
        Jwt token = jwtUtils.getJwtToken();
        Long userId = jwtUtils.extractId(token);
        userService.answerQuestion(userId, questionId, result);
    }
}

