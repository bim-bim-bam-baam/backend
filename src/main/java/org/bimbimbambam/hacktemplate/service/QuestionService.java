package org.bimbimbambam.hacktemplate.service;

import org.bimbimbambam.hacktemplate.entity.Question;

import java.util.List;

public interface QuestionService {
    Question addQuestion(String questionContent, String answerLeft, String answerRight, Long categoryId);
    void deleteQuestion(Long questionId);
    List<Question> all();
    List<Question> all(Long categoryId);
    List<Question> remainder(Long userId, Long categoryId);
}
