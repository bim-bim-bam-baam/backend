package org.bimbimbambam.hacktemplate.service;

import org.bimbimbambam.hacktemplate.entity.Question;
import org.bimbimbambam.hacktemplate.entity.QuestionInQueue;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface QuestionService {
    QuestionInQueue addQuestionInQueue(String questionContent, String answerLeft, String answerRight, MultipartFile imageFile, Long categoryId);
    Question addQuestion(String questionContent, String answerLeft, String answerRight, MultipartFile imageFile, Long categoryId);
    void deleteQuestion(Long questionId);
    List<Question> all();
    List<Question> all(Long categoryId);
    List<Question> remainder(Long userId, Long categoryId);
}
