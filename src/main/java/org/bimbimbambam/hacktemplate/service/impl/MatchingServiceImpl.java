package org.bimbimbambam.hacktemplate.service.impl;

import lombok.RequiredArgsConstructor;
import org.bimbimbambam.hacktemplate.config.MinioConfig;
import org.bimbimbambam.hacktemplate.controller.response.UserMatchingDto;
import org.bimbimbambam.hacktemplate.entity.Answer;
import org.bimbimbambam.hacktemplate.entity.Category;
import org.bimbimbambam.hacktemplate.entity.Question;
import org.bimbimbambam.hacktemplate.entity.User;
import org.bimbimbambam.hacktemplate.exception.NotFoundException;
import org.bimbimbambam.hacktemplate.repository.*;
import org.bimbimbambam.hacktemplate.service.MatchingService;
import org.bimbimbambam.hacktemplate.utils.SvdUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    private final MinioConfig minioConfig;

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

        List<User> allUsers = userRepository.findAll().stream()
                .filter(user -> !connectedUserIds.contains(user))
                //.filter(user -> !user.equals(currentUser))
                .toList();

        Map<Long, Map<Long, Long>> userAnswers = buildUserAnswersMap(allUsers, questions);
        Map<Long, Long> targetUserAnswers = userAnswers.getOrDefault(userId, new HashMap<>());

        return allUsers.stream()
                .filter(user -> !user.getId().equals(userId))
                .map(user -> {
                    Map<Long, Long> otherUserAnswers = userAnswers.getOrDefault(user.getId(), new HashMap<>());
                    double similarity = calculateCosineSimilarity(targetUserAnswers, otherUserAnswers, questions);
                    return toUserMatchingDto(user, similarity);
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


    @Override
    public List<UserMatchingDto> getClosestSvd(Long userId, Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("Category not found"));

        List<Question> questions = questionRepository.findAllByCategory(category);

        User currentUser = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        List<User> connectedUserIds = chatRepository.findAllByFromUserOrToUser(currentUser, currentUser).stream()
                .flatMap(chat -> Stream.of(chat.getFromUser(), chat.getToUser()))
                .distinct().toList();


        List<User> allUsers = userRepository.findAll().stream()
                .filter(user -> !connectedUserIds.contains(user)).toList();

        double[][] userQuestionMatrix = buildUserQuestionMatrix(allUsers, questions);

        SvdUtils.SVDResult svdResult = SvdUtils.computeSVD(userQuestionMatrix);

        double[][] reducedU = reduceMatrix(svdResult.U, 2);

        int currentUserIndex = allUsers.indexOf(currentUser);
        double[] currentUserVector = reducedU[currentUserIndex];

        return findSimilarUsers(currentUserIndex, currentUserVector, reducedU, allUsers);
    }

    private List<UserMatchingDto> findSimilarUsers(int currentUserIndex, double[] currentUserVector, double[][] reducedU, List<User> allUsers) {
        List<UserMatchingDto> result = new ArrayList<>();

        for (int i = 0; i < reducedU.length; i++) {
            if (i == currentUserIndex) continue;

            double[] otherUserVector = reducedU[i];
            double similarity = calculateCosineSimilarity(currentUserVector, otherUserVector);

            User user = allUsers.get(i);
            result.add(toUserMatchingDto(user, similarity));
        }

        return result.stream()
                .sorted((a, b) -> Long.compare(b.similarity(), a.similarity()))
                .collect(Collectors.toList());
    }


    private double[][] buildUserQuestionMatrix(List<User> users, List<Question> questions) {
        double[][] matrix = new double[users.size()][questions.size()];

        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            for (int j = 0; j < questions.size(); j++) {
                Question question = questions.get(j);
                Answer answer = answerRepository.findByUserAndQuestion(user, question).orElse(null);
                matrix[i][j] = (answer != null) ? answer.getAnswer() : 0;
            }
        }

        return matrix;
    }

    private double[][] reduceMatrix(double[][] matrix, int rank) {
        double[][] reduced = new double[matrix.length][rank];
        for (int i = 0; i < matrix.length; i++) {
            System.arraycopy(matrix[i], 0, reduced[i], 0, rank);
        }
        return reduced;
    }

    private double calculateCosineSimilarity(double[] vec1, double[] vec2) {
        double dotProduct = 0.0;
        double normVec1 = 0.0;
        double normVec2 = 0.0;

        for (int i = 0; i < vec1.length; i++) {
            dotProduct += vec1[i] * vec2[i];
            normVec1 += vec1[i] * vec1[i];
            normVec2 += vec2[i] * vec2[i];
        }

        return (normVec1 == 0 || normVec2 == 0) ? 0.0 : dotProduct / (Math.sqrt(normVec1) * Math.sqrt(normVec2));
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

    private UserMatchingDto toUserMatchingDto(User user, double similarity) {
        if (user.getAvatar() == null)
            user.setAvatar("unknown.png");
        user.setAvatar(minioConfig.getUrl() + "/" + minioConfig.getBucket() + "/" + user.getAvatar());
        return new UserMatchingDto(
                user.getId(),
                user.getUsername(),
                user.getAvatar(),
                Math.round(similarity * 100)
        );
    }
}
