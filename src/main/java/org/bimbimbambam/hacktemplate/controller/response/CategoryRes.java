package org.bimbimbambam.hacktemplate.controller.response;

import org.bimbimbambam.hacktemplate.entity.Category;

/**
 * DTO for {@link Category}
 */
public record CategoryCreateRes(Long id, String name, String avatar) {
}