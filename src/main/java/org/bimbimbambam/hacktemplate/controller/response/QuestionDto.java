package org.bimbimbambam.hacktemplate.controller.response;

/**
 * DTO for {@link org.bimbimbambam.hacktemplate.entity.Question}
 */
public record QuestionDto(Long id, String content, String answerLeft, String answerRight) {
}
