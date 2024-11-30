package org.bimbimbambam.hacktemplate.service.impl;

import lombok.RequiredArgsConstructor;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.SingularValueDecomposition;
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

        List<User> availableUsers = userCategoryRepository.findAllByCategory(category).stream()
                .map(UserCategory::getUser).toList();

        List<User> allUsers = userRepository.findAll().stream()
                .filter(availableUsers::contains)
                .filter(user -> !connectedUserIds.contains(user))
                .filter(user -> !user.equals(currentUser)).toList();

        
        RealMatrix userQuestionMatrix = buildUserQuestionMatrix(allUsers, questions);

        
        RealMatrix reducedUserMatrix = performSVD(userQuestionMatrix, 2); 

        
        return getSimilarUsers(reducedUserMatrix, currentUser, allUsers);
    }

    
    private RealMatrix buildUserQuestionMatrix(List<User> users, List<Question> questions) {
        double[][] matrix = new double[users.size()][questions.size()];

        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            for (int j = 0; j < questions.size(); j++) {
                Question question = questions.get(j);
                Answer answer = answerRepository.findByUserAndQuestion(user, question).orElse(null);
                matrix[i][j] = (answer != null) ? answer.getAnswer() : 0; 
            }
        }

        return MatrixUtils.createRealMatrix(matrix);
    }

    
    private RealMatrix performSVD(RealMatrix matrix, int rank) {
        SingularValueDecomposition svd = new SingularValueDecomposition(matrix);

        RealMatrix U = svd.getU();
        
        return U.getSubMatrix(0, U.getRowDimension() - 1, 0, rank - 1);
    }

    
    private List<UserMatchingDto> getSimilarUsers(RealMatrix reducedUserMatrix, User currentUser, List<User> allUsers) {
        RealVector currentUserVector = reducedUserMatrix.getRowVector(currentUser.getId().intValue());

        
        Map<Long, Double> similarityScores = new HashMap<>();
        for (int i = 0; i < reducedUserMatrix.getRowDimension(); i++) {
            if (i != currentUser.getId().intValue()) {
                RealVector otherUserVector = reducedUserMatrix.getRowVector(i);
                double similarity = calculateCosineSimilarity(currentUserVector, otherUserVector);
                similarityScores.put(allUsers.get(i).getId(), similarity);
            }
        }

        
        return similarityScores.entrySet().stream()
                .sorted((entry1, entry2) -> Double.compare(entry2.getValue(), entry1.getValue()))
                .limit(10) 
                .map(entry -> {
                    User user = allUsers.stream().filter(u -> u.getId().equals(entry.getKey())).findFirst().orElse(null);
                    return new UserMatchingDto(user.getId(), user.getUsername(), user.getAvatar(), Math.round(entry.getValue() * 100));
                })
                .collect(Collectors.toList());
    }

    
    private double calculateCosineSimilarity(RealVector vec1, RealVector vec2) {
        double dotProduct = vec1.dotProduct(vec2);
        double normVec1 = vec1.getNorm();
        double normVec2 = vec2.getNorm();

        return (normVec1 == 0 || normVec2 == 0) ? 0.0 : dotProduct / (normVec1 * normVec2);
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
