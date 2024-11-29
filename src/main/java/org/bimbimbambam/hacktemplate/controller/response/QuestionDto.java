package org.bimbimbambam.hacktemplate.controller.response;

import lombok.Value;
import org.bimbimbambam.hacktemplate.entity.Answer;
import org.bimbimbambam.hacktemplate.entity.Question;

import java.util.List;

/**
 * DTO for {@link Question}
 */
@Value
public class QuestionDto {
    Long id;
    String content;
    String image;
    List<AnswerDto> answers;
    Long categoryId;

    /**
     * DTO for {@link Answer}
     */
    @Value
    public static class AnswerDto {
        Long id;
        Long answer;
    }
}