package org.bimbimbambam.hacktemplate.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.bimbimbambam.hacktemplate.config.MinioConfig;
import org.bimbimbambam.hacktemplate.controller.request.ImageRequest;
import org.bimbimbambam.hacktemplate.entity.Category;
import org.bimbimbambam.hacktemplate.entity.Question;
import org.bimbimbambam.hacktemplate.entity.User;
import org.bimbimbambam.hacktemplate.entity.UserCategory;
import org.bimbimbambam.hacktemplate.exception.NotFoundException;
import org.bimbimbambam.hacktemplate.repository.CategoryRepository;
import org.bimbimbambam.hacktemplate.repository.QuestionRepository;
import org.bimbimbambam.hacktemplate.repository.UserCategoryRepository;
import org.bimbimbambam.hacktemplate.repository.UserRepository;
import org.bimbimbambam.hacktemplate.service.ImageService;
import org.bimbimbambam.hacktemplate.service.QuestionService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {

    private final QuestionRepository questionRepository;
    private final CategoryRepository categoryRepository; // Assuming this exists.
    private final ImageService imageService;
    private final MinioConfig minioConfig;
    private final UserCategoryRepository userCategoryRepository;
    private final UserRepository userRepository;

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

        category.setQuestionCount(category.getQuestionCount() + 1);
        category = categoryRepository.save(category);

        return questionRepository.save(question);
    }

    public void deleteQuestion(Long questionId) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new EntityNotFoundException("Question with ID " + questionId + " not found"));
        Category category = question.getCategory();
        category.setQuestionCount(category.getQuestionCount() - 1);
        categoryRepository.save(category);
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
                    if (question.getImage() != null) {
                        question.setImage(minioConfig.getUrl() + "/" + minioConfig.getBucket() + "/" + question.getImage());
                    }
                }).toList();
    }

    public void insertQuestions(Map<String, String> questionDescriptions, String categoryName) {
        Category category = categoryRepository.findByName(categoryName)
                .orElseThrow(() -> new EntityNotFoundException("Category not found"));

        questionDescriptions.forEach((code, description) -> {
            Question question = new Question();
            question.setContent(description);
            question.setCategory(category);
            questionRepository.save(question);
        });
    }

    @Override
    public List<Question> remainder(Long userId, Long categoryId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new NotFoundException("Category not found"));

        UserCategory userCategory = userCategoryRepository.findByUserAndCategory(user, category)
                .orElseThrow(() -> new NotFoundException("UserCategory not found"));

        return questionRepository.findAllByCategoryId(categoryId).stream()
                .skip(userCategory.getNextQuestionPos())
                .peek(question -> {
                    if (question.getImage() != null) {
                        question.setImage(minioConfig.getUrl() + "/" + minioConfig.getBucket() + "/" + question.getImage());
                    }
                }).toList();
    }
}
