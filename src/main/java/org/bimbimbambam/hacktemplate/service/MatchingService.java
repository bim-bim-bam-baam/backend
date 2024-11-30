package org.bimbimbambam.hacktemplate.service;

import org.bimbimbambam.hacktemplate.controller.response.UserMatchingDto;

import java.util.List;

public interface MatchingService {
    List<UserMatchingDto> getClosest(Long userId, Long categoryId);

    public List<UserMatchingDto> getClosestSvd(Long userId, Long categoryId);
}
