package org.bimbimbambam.hacktemplate.controller.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.bimbimbambam.hacktemplate.entity.User;

/**
 * DTO for {@link User}
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record UserDto(Long id, String username, String description, String avatar) {
}