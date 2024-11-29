package org.bimbimbambam.hacktemplate.service.impl;

import lombok.RequiredArgsConstructor;
import org.bimbimbambam.hacktemplate.config.MinioConfig;
import org.bimbimbambam.hacktemplate.controller.request.image.ImageRequest;
import org.bimbimbambam.hacktemplate.controller.request.user.UserLoginReq;
import org.bimbimbambam.hacktemplate.controller.request.user.UserRegisterReq;
import org.bimbimbambam.hacktemplate.controller.request.user.UserUpdateAvatarReq;
import org.bimbimbambam.hacktemplate.entity.User;
import org.bimbimbambam.hacktemplate.exception.InvalidCredentialsException;
import org.bimbimbambam.hacktemplate.exception.UserExistException;
import org.bimbimbambam.hacktemplate.exception.UserNotFoundException;
import org.bimbimbambam.hacktemplate.repository.UserRepository;
import org.bimbimbambam.hacktemplate.service.ImageService;
import org.bimbimbambam.hacktemplate.service.UserService;
import org.bimbimbambam.hacktemplate.utils.Jwt;
import org.bimbimbambam.hacktemplate.utils.JwtUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ImageService imageService;
    private final JwtUtils jwtUtils;
    private final MinioConfig minioConfig;

    @Override
    public void registerUser(UserRegisterReq userRegisterReq) {
        if (userRepository.findByUsername(userRegisterReq.username()).isPresent()) {
            throw new UserExistException("Username already exists");
        }

        User newUser = new User();

        newUser.setUsername(userRegisterReq.username());
        newUser.setPassword(passwordEncoder.encode(userRegisterReq.password()));

        userRepository.save(newUser);
    }

    @Override
    public Jwt loginUser(UserLoginReq userLoginReq) {
        Optional<User> user = userRepository.findByUsername(userLoginReq.username());
        if (user.isEmpty()) {
            throw new InvalidCredentialsException("Username not found");
        }

        if (!passwordEncoder.matches(userLoginReq.password(), user.get().getPassword())) {
            throw new InvalidCredentialsException("Wrong password");
        }

        return jwtUtils.generateToken(user.get().getUsername(), user.get().getId(), user.get().getRoles());
    }

    @Override
    public void updateAvatar(Long id, UserUpdateAvatarReq updateAvatarReq) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new UserNotFoundException("User not found");
        }

        String filename = imageService.upload(new ImageRequest(updateAvatarReq.image()));
        user.get().setAvatar(filename);
        userRepository.save(user.get());
    }

    @Override
    public User getUser(Long id) {
        Optional<User> user = userRepository.findById(id);

        if (user.isEmpty()) {
            throw new UserNotFoundException("User not found");
        }

        user.get().setPassword(minioConfig.getUrl() + "/" + minioConfig.getBucket() + "/" + user.get().getAvatar());
        return user.get();
    }
}
