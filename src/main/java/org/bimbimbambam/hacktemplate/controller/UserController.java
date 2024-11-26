package org.bimbimbambam.hacktemplate.controller;

import lombok.RequiredArgsConstructor;
import org.bimbimbambam.hacktemplate.controller.request.UserLoginReq;
import org.bimbimbambam.hacktemplate.controller.request.UserRegisterReq;
import org.bimbimbambam.hacktemplate.service.UserService;
import org.bimbimbambam.hacktemplate.utils.Jwt;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

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
}

