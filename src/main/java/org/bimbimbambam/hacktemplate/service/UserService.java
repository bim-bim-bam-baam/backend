package org.bimbimbambam.hacktemplate.service;

import org.bimbimbambam.hacktemplate.controller.request.UserLoginReq;
import org.bimbimbambam.hacktemplate.controller.request.UserRegisterReq;
import org.bimbimbambam.hacktemplate.controller.request.UserUpdateAvatarReq;
import org.bimbimbambam.hacktemplate.entity.Question;
import org.bimbimbambam.hacktemplate.entity.User;
import org.bimbimbambam.hacktemplate.utils.Jwt;

import java.util.List;

public interface UserService {
    void registerUser(UserRegisterReq userRegisterReq);

    Jwt loginUser(UserLoginReq userLoginReq);

    void updateAvatar(Long id, UserUpdateAvatarReq updateAvatarReq);

    User getUser(Long id);

    Question getNextQuestion(Long userId, Long cateroryId);

    void answerQuestion(Long userId, Long questionId, Long result);
    
    List<User> all();
}
