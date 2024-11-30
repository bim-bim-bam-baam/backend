package org.bimbimbambam.hacktemplate.service.impl;

import lombok.RequiredArgsConstructor;
import org.bimbimbambam.hacktemplate.controller.response.UserMatchingDto;
import org.bimbimbambam.hacktemplate.entity.*;
import org.bimbimbambam.hacktemplate.exception.NotFoundException;
import org.bimbimbambam.hacktemplate.repository.*;
import org.bimbimbambam.hacktemplate.service.MatchingService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class MatchingServiceImpl implements MatchingService {

    private final UserRepository userRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final CategoryRepository categoryRepository;
    private final ChatRepository chatRepository;
    private final UserCategoryRepository userCategoryRepository;

    @Override
    public List<UserMatchingDto> getClosest(Long userId, Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("Category not found"));

        List<Question> questions = questionRepository.findAllByCategory(category);

        User currentUser = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        List<User> connectedUserIds = chatRepository.findAllByFromUserOrToUser(currentUser, currentUser).stream()
                .flatMap(chat -> Stream.of(chat.getFromUser(), chat.getToUser()))
                .distinct().toList();

        List<User> availableUsers = userCategoryRepository.findAllByCategory(category).stream()
                .map(UserCategory::getUser).toList();

        List<User> allUsers = userRepository.findAll().stream()
                .filter(availableUsers::contains)
                .filter(user -> !connectedUserIds.contains(user))
                .filter(user -> !user.equals(currentUser)).toList();

        Map<Long, Map<Long, Long>> userAnswers = buildUserAnswersMap(allUsers, questions);
        Map<Long, Long> targetUserAnswers = userAnswers.getOrDefault(userId, new HashMap<>());

        return allUsers.stream()
                .filter(user -> !user.getId().equals(userId))
                .map(user -> {
                    Map<Long, Long> otherUserAnswers = userAnswers.getOrDefault(user.getId(), new HashMap<>());
                    double similarity = calculateCosineSimilarity(targetUserAnswers, otherUserAnswers, questions);
                    return new UserMatchingDto(user.getId(), user.getUsername(), user.getAvatar(), Math.round(similarity * 100));
                })
                .sorted((a, b) -> Long.compare(b.similarity(), a.similarity()))
                .collect(Collectors.toList());
    }

    private Map<Long, Map<Long, Long>> buildUserAnswersMap(List<User> users, List<Question> questions) {
        Map<Long, Map<Long, Long>> userAnswersMap = new HashMap<>();
        for (User user : users) {
            List<Answer> answers = answerRepository.findAllByUser(user);
            Map<Long, Long> answersMap = answers.stream()
                    .filter(answer -> questions.stream().anyMatch(q -> q.getId().equals(answer.getQuestion().getId())))
                    .collect(Collectors.toMap(answer -> answer.getQuestion().getId(), Answer::getAnswer));
            userAnswersMap.put(user.getId(), answersMap);
        }
        return userAnswersMap;
    }

    private double calculateCosineSimilarity(Map<Long, Long> user1Answers, Map<Long, Long> user2Answers, List<Question> questions) {
        double dotProduct = 0.0;
        double normUser1 = 0.0;
        double normUser2 = 0.0;

        for (Question question : questions) {
            Long answer1 = user1Answers.getOrDefault(question.getId(), 0L);
            Long answer2 = user2Answers.getOrDefault(question.getId(), 0L);
            dotProduct += answer1 * answer2;
            normUser1 += Math.pow(answer1, 2);
            normUser2 += Math.pow(answer2, 2);
        }

        return normUser1 == 0 || normUser2 == 0 ? 0.0 : dotProduct / (Math.sqrt(normUser1) * Math.sqrt(normUser2));
    }
}
