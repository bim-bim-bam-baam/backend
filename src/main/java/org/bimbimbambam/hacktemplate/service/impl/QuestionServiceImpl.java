package org.bimbimbambam.hacktemplate.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.bimbimbambam.hacktemplate.config.MinioConfig;
import org.bimbimbambam.hacktemplate.entity.Category;
import org.bimbimbambam.hacktemplate.entity.Question;
import org.bimbimbambam.hacktemplate.repository.CategoryRepository;
import org.bimbimbambam.hacktemplate.repository.QuestionRepository;
import org.bimbimbambam.hacktemplate.service.ImageService;
import org.bimbimbambam.hacktemplate.service.QuestionService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {
    private final QuestionRepository questionRepository;
    private final CategoryRepository categoryRepository; // Assuming this exists.
    private final ImageService imageService;
    private final MinioConfig minioConfig;

    public Question addQuestion(String questionContent, String answerLeft, String answerRight, Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException("Category with ID " + categoryId + " not found"));


        Question question = new Question();
        question.setContent(questionContent);
        question.setAnswerLeft(answerLeft);
        question.setAnswerRight(answerRight);
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

    @Override
    public List<Question> remainder(Long userId, Long categoryId) {
        return questionRepository.findAllByCategoryId(categoryId).stream()
                .filter(question -> question.getAnswers().stream()
                        .noneMatch(answer -> answer.getUser().getId().equals(userId)))
                .peek(question -> {
                    if (question.getImage() != null) {
                        question.setImage(minioConfig.getUrl() + "/" + minioConfig.getBucket() + "/" + question.getImage());
                    }
                })
                .toList();
    }
}
