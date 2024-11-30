package org.bimbimbambam.hacktemplate.service;

import jakarta.persistence.EntityNotFoundException;
import org.bimbimbambam.hacktemplate.controller.request.ImageRequest;
import org.bimbimbambam.hacktemplate.entity.Category;
import org.bimbimbambam.hacktemplate.entity.Question;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface QuestionService {
    public Question addQuestion(String content, MultipartFile imageFile, Long categoryId);
    public void deleteQuestion(Long questionId);
    public List<Question> all();
    public List<Question> all(Long categoryId);
    public void insertQuestions(Map<String, String> questionDescriptions, String categoryName);

}
