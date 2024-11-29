package org.bimbimbambam.hacktemplate.service;

import org.bimbimbambam.hacktemplate.controller.request.user.UserLoginReq;
import org.bimbimbambam.hacktemplate.controller.request.user.UserRegisterReq;
import org.bimbimbambam.hacktemplate.controller.request.user.UserUpdateAvatarReq;
import org.bimbimbambam.hacktemplate.entity.Question;
import org.bimbimbambam.hacktemplate.entity.User;
import org.bimbimbambam.hacktemplate.utils.Jwt;

public interface UserService {
    void registerUser(UserRegisterReq userRegisterReq);

    Jwt loginUser(UserLoginReq userLoginReq);

    void updateAvatar(Long id, UserUpdateAvatarReq updateAvatarReq);

    User getUser(Long id);

    Question getNextQuestion(Long cateroryId);
}
