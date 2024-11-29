package org.bimbimbambam.hacktemplate.controller.response;

/**
 * DTO for {@link org.bimbimbambam.hacktemplate.entity.Category}
 */
public record CategoryRes(Long id, String name, String avatar, Long questionCount) {
}
