package org.bimbimbambam.hacktemplate.service;

import org.bimbimbambam.hacktemplate.entity.Question;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface QuestionService {
    Question addQuestion(String content, MultipartFile imageFile, Long categoryId);
    void deleteQuestion(Long questionId);
    List<Question> all();
    List<Question> all(Long categoryId);
    void insertQuestions(Map<String, String> questionDescriptions, String categoryName);
    List<Question> remainder(Long userId, Long categoryId);
}
