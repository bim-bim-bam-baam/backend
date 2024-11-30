package org.bimbimbambam.hacktemplate.controller.request;

public record AddQuestionDto(String questionContent, String answerLeft, String answerRight, Long categoryId) {
}
