package org.bimbimbambam.hacktemplate.service;

import org.bimbimbambam.hacktemplate.entity.Question;
import org.springframework.web.multipart.MultipartFile;

public interface QuestionService {
    public Question addQuestion(String content, MultipartFile imageFile, Long categoryId);
    public void deleteQuestion(Long questionId);

}
