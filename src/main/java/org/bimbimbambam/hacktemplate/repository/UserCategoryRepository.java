package org.bimbimbambam.hacktemplate.repository;

import org.bimbimbambam.hacktemplate.entity.User;
import org.bimbimbambam.hacktemplate.entity.UserCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserCategoryRepository extends JpaRepository<UserCategory, Long> {
    Optional<UserCategory> findByUserIdAndCategoryId(Long userId, Long categoryId);
    Optional<UserCategory> findByUserIdAndQuestionId(Long userId, Long questionId);
}
