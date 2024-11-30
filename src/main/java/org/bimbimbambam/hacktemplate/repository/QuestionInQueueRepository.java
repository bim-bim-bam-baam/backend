package org.bimbimbambam.hacktemplate.repository;

import org.bimbimbambam.hacktemplate.entity.QuestionInQueue;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionInQueueRepository extends JpaRepository<QuestionInQueue, Long> {
}