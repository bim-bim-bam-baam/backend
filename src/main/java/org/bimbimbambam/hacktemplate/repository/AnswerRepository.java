package org.bimbimbambam.hacktemplate.repository;

import org.bimbimbambam.hacktemplate.entity.Answer;
import org.bimbimbambam.hacktemplate.entity.Question;
import org.bimbimbambam.hacktemplate.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
    Optional<Answer> findByUserAndQuestion(User user, Question question);
}