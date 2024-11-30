package org.bimbimbambam.hacktemplate.service.impl;

import lombok.RequiredArgsConstructor;
import org.bimbimbambam.hacktemplate.config.MinioConfig;
import org.bimbimbambam.hacktemplate.controller.request.ImageRequest;
import org.bimbimbambam.hacktemplate.controller.request.UserLoginReq;
import org.bimbimbambam.hacktemplate.controller.request.UserRegisterReq;
import org.bimbimbambam.hacktemplate.controller.request.UpdateImageReq;
import org.bimbimbambam.hacktemplate.entity.*;
import org.bimbimbambam.hacktemplate.exception.InternalServerErrorException;
import org.bimbimbambam.hacktemplate.exception.UnauthorizedException;
import org.bimbimbambam.hacktemplate.exception.BadRequestException;
import org.bimbimbambam.hacktemplate.exception.NotFoundException;
import org.bimbimbambam.hacktemplate.repository.*;
import org.bimbimbambam.hacktemplate.service.ImageService;
import org.bimbimbambam.hacktemplate.service.UserService;
import org.bimbimbambam.hacktemplate.utils.Jwt;
import org.bimbimbambam.hacktemplate.utils.JwtUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserCategoryRepository userCategoryRepository;
    private final QuestionRepository questionRepository;
    private final CategoryRepository categoryRepository;
    private final AnswerRepository answerRepository;
    private final PasswordEncoder passwordEncoder;
    private final ImageService imageService;
    private final JwtUtils jwtUtils;
    private final MinioConfig minioConfig;



    @Override
    public void registerUser(UserRegisterReq userRegisterReq) {
        if (userRepository.findByUsername(userRegisterReq.username()).isPresent()) {
            throw new BadRequestException("Username already exists");
        }

        User newUser = new User();

        newUser.setUsername(userRegisterReq.username());
        newUser.setPassword(passwordEncoder.encode(userRegisterReq.password()));

        userRepository.save(newUser);
    }

    @Override
    public Jwt loginUser(UserLoginReq userLoginReq) {
        Optional<User> user = userRepository.findByUsername(userLoginReq.username());
        if (user.isEmpty()) {
            throw new UnauthorizedException("Username not found");
        }

        if (!passwordEncoder.matches(userLoginReq.password(), user.get().getPassword())) {
            throw new UnauthorizedException("Wrong password");
        }

        return jwtUtils.generateToken(user.get().getUsername(), user.get().getId(), user.get().getRoles());
    }

    @Override
    public void updateAvatar(Long id, UpdateImageReq updateAvatarReq) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new NotFoundException("User not found");
        }

        String filename = imageService.upload(new ImageRequest(updateAvatarReq.image()));
        user.get().setAvatar(filename);
        userRepository.save(user.get());
    }

    @Override
    public User getUser(Long id) {
        Optional<User> user = userRepository.findById(id);

        if (user.isEmpty()) {
            throw new NotFoundException("User not found");
        }

        String avatar = user.get().getAvatar() == null ? "unknown.png" : user.get().getAvatar();

        user.get().setAvatar(minioConfig.getUrl() + "/" + minioConfig.getBucket() + "/" + avatar);
        return user.get();
    }

    @Override
    public Question getNextQuestion(Long userId, Long categoryId) {
        Category category = categoryRepository.findById(categoryId).orElse(null);

        if (category == null) {
            throw new NotFoundException("Category doesn't exist");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User id is non-existent"));

        UserCategory userCategory = userCategoryRepository.findByUserAndCategory(user, category).orElse(new UserCategory());

        if (userCategory.getNextQuestionPos() >= category.getQuestionCount()) {
            throw new NotFoundException("User answered all possible questions");
        }

        Question question = questionRepository.findById(userCategory.getNextQuestionPos()).orElse(null);
        if (question == null) {
            throw new InternalServerErrorException("WTF, question should've existed, but it is not");
        }
        userCategory.setNextQuestionPos(userCategory.getNextQuestionPos() + 1);
        userCategoryRepository.save(userCategory);
        return question;
    }

    @Override
    @Transactional
    public void answerQuestion(Long userId, Long questionId, Long result) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new NotFoundException("Question id doesn't exist"));
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));
        Answer answer = answerRepository.findByUserAndQuestion(user, question)
                .orElse(new Answer());

        answer.setUser(user);
        answer.setQuestion(question);
        answer.setAnswer(result);

        UserCategory userCategory = userCategoryRepository.findByUserAndCategory(user, question.getCategory())
                .orElse(new UserCategory());

        userCategory.setUser(user);
        userCategory.setCategory(question.getCategory());
        userCategory.setNextQuestionPos(userCategory.getNextQuestionPos() + 1);

        userCategoryRepository.save(userCategory);
        answerRepository.save(answer);
        questionRepository.save(question);
    }

    @Override
    public List<User> all() {
        return userRepository.findAll();
    }

    @Override
    public void setDescription(Long userId, String description) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User doesn't exist"));
        user.setDescription(description);
        userRepository.save(user);
    }
}
