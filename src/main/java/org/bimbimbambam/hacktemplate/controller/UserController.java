package org.bimbimbambam.hacktemplate.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.bimbimbambam.hacktemplate.controller.request.UserLoginReq;
import org.bimbimbambam.hacktemplate.controller.request.UserRegisterReq;
import org.bimbimbambam.hacktemplate.controller.request.UpdateImageReq;
import org.bimbimbambam.hacktemplate.controller.response.UserDto;
import org.bimbimbambam.hacktemplate.mapper.UserMapper;
import org.bimbimbambam.hacktemplate.service.impl.UserServiceImpl;
import org.bimbimbambam.hacktemplate.utils.Jwt;
import org.bimbimbambam.hacktemplate.utils.JwtUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {
    private final UserServiceImpl userService;
    private final JwtUtils jwtUtils;

    private final UserMapper userMapper;

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
            @ModelAttribute UpdateImageReq updateImageReq) {
        Jwt token = jwtUtils.getJwtToken();
        Long userId = jwtUtils.extractId(token);
        userService.updateAvatar(userId, updateImageReq);
    }

    @GetMapping("/profile")
    @SecurityRequirement(name = "bearerAuth")
    public UserDto getUserProfile() {
        Jwt token = jwtUtils.getJwtToken();
        Long userId = jwtUtils.extractId(token);
        return userMapper.toDto(userService.getUser(userId));
    }

    @GetMapping("/{userId}")
    public UserDto getUser(@PathVariable Long userId) {
        return userMapper.toDto(userService.getUser(userId));
    }
    
    @GetMapping("/all")
    public List<UserDto> all() {
        return userService.all().stream().map(userMapper::toDto).toList();
    }

    @PostMapping("/setDescrioptin")
    public void setDescription(String description) {
        Jwt token = jwtUtils.getJwtToken();
        Long userId = jwtUtils.extractId(token);
        userService.setDescription(userId, description);
    }
}

