package org.bimbimbambam.hacktemplate.service;

import org.bimbimbambam.hacktemplate.controller.request.user.UserLoginReq;
import org.bimbimbambam.hacktemplate.controller.request.user.UserRegisterReq;
import org.bimbimbambam.hacktemplate.controller.request.user.UserUpdateAvatarReq;
import org.bimbimbambam.hacktemplate.entity.User;
import org.bimbimbambam.hacktemplate.utils.Jwt;

import java.util.Optional;

public interface UserService {
    void registerUser(UserRegisterReq userRegisterReq);

    Optional<Jwt> loginUser(UserLoginReq userLoginReq);

    void updateAvatar(Long id, UserUpdateAvatarReq updateAvatarReq);

    Optional<User> getUser(Long id);
}
