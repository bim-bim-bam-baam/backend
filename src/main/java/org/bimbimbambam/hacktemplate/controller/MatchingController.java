package org.bimbimbambam.hacktemplate.controller;

import lombok.RequiredArgsConstructor;
import org.bimbimbambam.hacktemplate.controller.response.UserMatchingDto;
import org.bimbimbambam.hacktemplate.service.MatchingService;
import org.bimbimbambam.hacktemplate.utils.Jwt;
import org.bimbimbambam.hacktemplate.utils.JwtUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/matching")
public class MatchingController {
    private final MatchingService matchingService;
    private final JwtUtils jwtUtils;

    @GetMapping("/{categoryId}")
    public List<UserMatchingDto> getClosest(@PathVariable Long categoryId) {
        Jwt token = jwtUtils.getJwtToken();
        Long userId = jwtUtils.extractId(token);
        return matchingService.getClosest(userId, categoryId);
    }
}
