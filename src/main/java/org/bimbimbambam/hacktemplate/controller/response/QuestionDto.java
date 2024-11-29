package org.bimbimbambam.hacktemplate.controller.response;

import lombok.Value;
import org.bimbimbambam.hacktemplate.entity.Question;

/**
 * DTO for {@link Question}
 */
@Value
public class QuestionDto {
    Long id;
    String content;
    String image;
}