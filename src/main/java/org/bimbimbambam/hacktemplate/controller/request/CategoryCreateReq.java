package org.bimbimbambam.hacktemplate.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * DTO for {@link Category}
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record CategoryDto(String name, String avatar) {
}