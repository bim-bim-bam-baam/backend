package org.bimbimbambam.hacktemplate.repository;

import org.bimbimbambam.hacktemplate.entity.Answer;
import org.bimbimbambam.hacktemplate.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
    List<Answer> findAllByUser(User user);
}
