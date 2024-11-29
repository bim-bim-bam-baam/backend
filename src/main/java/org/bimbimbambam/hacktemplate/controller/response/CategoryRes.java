package org.bimbimbambam.hacktemplate.controller.response;

import org.bimbimbambam.hacktemplate.entity.Category;

/**
 * DTO for {@link Category}
 */
public record CategoryRes(Long id, String name, String avatar) {
}