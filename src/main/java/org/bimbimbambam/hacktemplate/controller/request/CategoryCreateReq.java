package org.bimbimbambam.hacktemplate.controller.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.bimbimbambam.hacktemplate.entity.Category;

/**
 * DTO for {@link Category}
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record CategoryCreateReq(String name) {
}