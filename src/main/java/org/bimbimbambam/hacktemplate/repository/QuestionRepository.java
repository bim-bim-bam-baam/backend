package org.bimbimbambam.hacktemplate.repository;

import org.bimbimbambam.hacktemplate.entity.Category;
import org.bimbimbambam.hacktemplate.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findAllByCategory(Category category);
    List<Question> findAllByCategoryId(Long categoryId);

}