package org.bimbimbambam.hacktemplate.controller;

import lombok.RequiredArgsConstructor;
import org.bimbimbambam.hacktemplate.controller.request.user.UserLoginReq;
import org.bimbimbambam.hacktemplate.controller.request.user.UserRegisterReq;
import org.bimbimbambam.hacktemplate.controller.request.user.UserUpdateAvatarReq;
import org.bimbimbambam.hacktemplate.entity.User;
import org.bimbimbambam.hacktemplate.service.impl.UserServiceImpl;
import org.bimbimbambam.hacktemplate.utils.Jwt;
import org.bimbimbambam.hacktemplate.utils.JwtUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {
    private final UserServiceImpl userService;
    private final JwtUtils jwtUtils;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserRegisterReq userRegisterReq) {
        try {
            userService.registerUser(userRegisterReq);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody UserLoginReq userLoginReq) {
        Optional<Jwt> jwt = userService.loginUser(userLoginReq);
        if (jwt.isPresent()) {
            return ResponseEntity.ok(jwt.get());
        }
        return ResponseEntity.status(401).body("Invalid username or password");
    }

    @PostMapping("/updateAvatar")
    public ResponseEntity<?> updateAvatar(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @RequestBody UserUpdateAvatarReq userUpdateAvatarReq) {

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing or invalid Authorization header");
        }

        Jwt token = new Jwt(authorizationHeader.substring(7));

        if (!jwtUtils.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token");
        }

        Long userId = jwtUtils.extractId(token);

        userService.updateAvatar(userId, userUpdateAvatarReq);

        return ResponseEntity.status(HttpStatus.OK).body("Avatar updated successfully");
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getUserProfile(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing or invalid Authorization header");
        }

        
        Jwt token = new Jwt(authorizationHeader.substring(7));

        
        if (!jwtUtils.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token");
        }

        
        Long userId = jwtUtils.extractId(token);

        
        Optional<User> userProfile = userService.getUser(userId);

        
        if (userProfile.isPresent()) {
            return ResponseEntity.ok(userProfile.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User profile not found");
        }
    }


}

