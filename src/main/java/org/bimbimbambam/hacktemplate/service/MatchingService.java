package org.bimbimbambam.hacktemplate.service;

import org.bimbimbambam.hacktemplate.controller.request.UserLoginReq;
import org.bimbimbambam.hacktemplate.entity.User;

import java.util.List;

public interface MatchingService {
    List<User> getClosest(Long userId);
}
