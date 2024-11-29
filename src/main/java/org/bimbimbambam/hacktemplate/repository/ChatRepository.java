package org.bimbimbambam.hacktemplate.repository;

import org.bimbimbambam.hacktemplate.entity.Chat;
import org.bimbimbambam.hacktemplate.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRepository extends JpaRepository<Chat, Long> {
    List<Chat> findAllByFromUserAndToUserConfirmedFalse(User fromUser);

    List<Chat> findAllByToUserAndToUserConfirmedFalse(User toUser);

    boolean existsByFromUserAndToUser(User fromUser, User toUser);

    List<Chat> findAllByFromUserAndToUserConfirmedTrueOrToUserAndToUserConfirmedTrue(User fromUser, User toUser);
}

