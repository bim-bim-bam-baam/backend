package org.bimbimbambam.hacktemplate.controller;

import lombok.RequiredArgsConstructor;
import org.bimbimbambam.hacktemplate.controller.request.UserLoginReq;
import org.bimbimbambam.hacktemplate.controller.response.UserProfileDto;
import org.bimbimbambam.hacktemplate.mapper.UserMapper;
import org.bimbimbambam.hacktemplate.service.MatchingService;
import org.bimbimbambam.hacktemplate.utils.Jwt;
import org.bimbimbambam.hacktemplate.utils.JwtUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/matching")
public class MatchingController {
    private final MatchingService matchingService;
    private final UserMapper userMapper;
    private final JwtUtils jwtUtils;

    @GetMapping("/")
    public List<UserProfileDto> getClosest() {
        Jwt token = jwtUtils.getJwtToken();
        Long userId = jwtUtils.extractId(token);
        return matchingService.getClosest(userId).stream().map(userMapper::toDto).collect(Collectors.toList());
    }
}
