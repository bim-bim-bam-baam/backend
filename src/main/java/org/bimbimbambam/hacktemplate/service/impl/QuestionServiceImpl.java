package org.bimbimbambam.hacktemplate.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.bimbimbambam.hacktemplate.config.MinioConfig;
import org.bimbimbambam.hacktemplate.controller.request.ImageRequest;
import org.bimbimbambam.hacktemplate.entity.Category;
import org.bimbimbambam.hacktemplate.entity.Question;
import org.bimbimbambam.hacktemplate.repository.CategoryRepository;
import org.bimbimbambam.hacktemplate.repository.QuestionRepository;
import org.bimbimbambam.hacktemplate.service.ImageService;
import org.bimbimbambam.hacktemplate.service.QuestionService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {

    private final QuestionRepository questionRepository;
    private final CategoryRepository categoryRepository; // Assuming this exists.
    private final ImageService imageService;
    private final MinioConfig minioConfig;

    public Question addQuestion(String content, MultipartFile imageFile, Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException("Category with ID " + categoryId + " not found"));

        String imagePath = null;
        if (imageFile != null && !imageFile.isEmpty()) {
            imagePath = imageService.upload(new ImageRequest(imageFile));
        }

        Question question = new Question();
        question.setContent(content);
        question.setImage(imagePath);
        question.setCategory(category);

        return questionRepository.save(question);
    }

    public void deleteQuestion(Long questionId) {
        if (!questionRepository.existsById(questionId)) {
            throw new EntityNotFoundException("Question with ID " + questionId + " not found");
        }
        questionRepository.deleteById(questionId);
    }

    @Override
    public List<Question> all() {
        return questionRepository.findAll().stream()
                .peek(question -> {
                    if (question.getImage() != null) {
                        question.setImage(minioConfig.getUrl() + "/" + minioConfig.getBucket() + "/" + question.getImage());
                    }
                }).toList();
    }

    @Override
    public List<Question> all(Long categoryId) {
        return questionRepository.findAllByCategoryId(categoryId).stream()
                .peek(question -> {
                    if(question.getImage() != null) {
                        question.setImage(minioConfig.getUrl()+"/"+minioConfig.getBucket()+"/"+question.getImage());
                    }
                }).toList();
    }
}
