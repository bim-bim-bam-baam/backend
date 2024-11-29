package org.bimbimbambam.hacktemplate.controller.response;

import org.bimbimbambam.hacktemplate.entity.User;

/**
 * DTO for {@link User}
 */
public record UserProfileDto(Long id, String username, String avatar) {
}
