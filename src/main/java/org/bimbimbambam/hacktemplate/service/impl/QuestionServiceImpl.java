package org.bimbimbambam.hacktemplate.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.bimbimbambam.hacktemplate.controller.request.ImageRequest;
import org.bimbimbambam.hacktemplate.entity.Category;
import org.bimbimbambam.hacktemplate.entity.Question;
import org.bimbimbambam.hacktemplate.repository.CategoryRepository;
import org.bimbimbambam.hacktemplate.repository.QuestionRepository;
import org.bimbimbambam.hacktemplate.service.ImageService;
import org.bimbimbambam.hacktemplate.service.QuestionService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {

    private final QuestionRepository questionRepository;
    private final CategoryRepository categoryRepository; // Assuming this exists.
    private final ImageService imageService;

    /**
     * Adds a new question to the database.
     *
     * @param content The content of the question.
     * @param imageFile The image file (if any) associated with the question.
     * @param categoryId The category ID to which the question belongs.
     * @return The saved question.
     */
    public Question addQuestion(String content, MultipartFile imageFile, Long categoryId) {
        // Fetch the category by ID
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException("Category with ID " + categoryId + " not found"));

        // Upload the image if provided and get the file path
        String imagePath = null;
        if (imageFile != null && !imageFile.isEmpty()) {
            imagePath = imageService.upload(new ImageRequest(imageFile));
        }

        // Create and save the question
        Question question = new Question();
        question.setContent(content);
        question.setImage(imagePath);
        question.setCategory(category);

        return questionRepository.save(question);
    }

    /**
     * Deletes a question by its ID.
     *
     * @param questionId The ID of the question to delete.
     */
    public void deleteQuestion(Long questionId) {
        if (!questionRepository.existsById(questionId)) {
            throw new EntityNotFoundException("Question with ID " + questionId + " not found");
        }
        questionRepository.deleteById(questionId);
    }
}
