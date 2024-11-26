package org.bimbimbambam.hacktemplate.service;

import lombok.RequiredArgsConstructor;
import org.bimbimbambam.hacktemplate.controller.request.UserLoginReq;
import org.bimbimbambam.hacktemplate.controller.request.UserRegisterReq;
import org.bimbimbambam.hacktemplate.entity.User;
import org.bimbimbambam.hacktemplate.repository.UserRepository;
import org.bimbimbambam.hacktemplate.utils.Jwt;
import org.bimbimbambam.hacktemplate.utils.JwtUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    public void registerUser(UserRegisterReq userRegisterReq) {
        if (userRepository.findByUsername(userRegisterReq.username()).isPresent()) {
            throw new IllegalArgumentException("Username already exists");
        }

        User newUser = new User();

        newUser.setUsername(userRegisterReq.username());
        newUser.setPassword(passwordEncoder.encode(userRegisterReq.password()));

        userRepository.save(newUser);
    }

    public Optional<Jwt> loginUser(UserLoginReq userLoginReq) {
        Optional<User> user = userRepository.findByUsername(userLoginReq.username());
        if (user.isPresent() && passwordEncoder.matches(userLoginReq.password(), user.get().getPassword())) {
            Jwt token = jwtUtils.generateToken(user.get().getUsername());
            return Optional.of(token);
        }
        return Optional.empty();
    }
}
