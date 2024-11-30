package org.bimbimbambam.hacktemplate.service.impl;

import lombok.RequiredArgsConstructor;
import org.bimbimbambam.hacktemplate.controller.request.UpdateImageReq;
import org.bimbimbambam.hacktemplate.entity.Chat;
import org.bimbimbambam.hacktemplate.entity.Message;
import org.bimbimbambam.hacktemplate.entity.User;
import org.bimbimbambam.hacktemplate.exception.ForbiddenException;
import org.bimbimbambam.hacktemplate.exception.NotFoundException;
import org.bimbimbambam.hacktemplate.repository.ChatRepository;
import org.bimbimbambam.hacktemplate.repository.MessageRepository;
import org.bimbimbambam.hacktemplate.repository.UserRepository;
import org.bimbimbambam.hacktemplate.service.ChatService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ChatServiceImpl implements ChatService {
    private final ChatRepository chatRepository;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    public List<Chat> getSentRequests(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));
        return chatRepository.findAllByFromUserAndToUserConfirmedFalse(user)
                .stream().filter(chat -> !chat.isCanceled()).toList();

    }

    public List<Chat> getPendingRequests(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));
        return chatRepository.findAllByToUserAndToUserConfirmedFalse(user)
                .stream().filter(chat -> !chat.isCanceled()).toList();
    }

    @Transactional
    public void acceptChatRequest(Long chatId, Long userId) {
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new NotFoundException("Chat request not found"));

        if (!chat.getToUser().getId().equals(userId)) {
            throw new ForbiddenException("Access denied: You are not allowed to accept this chat request");
        }

        chat.setToUserConfirmed(true);
        chatRepository.save(chat);
    }

    @Transactional
    public void declineChatRequest(Long chatId, Long userId) {
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new NotFoundException("Chat request not found"));

        if (!chat.getToUser().getId().equals(userId) && !chat.getFromUser().getId().equals(userId)) {
            throw new ForbiddenException("Access denied: You are not allowed to accept this chat request");
        }
        chat.setCanceled(true);
        chatRepository.save(chat);
    }

    @Override
    public Message uploadMessage(Long userId, Long chatId, UpdateImageReq updateImageReq) {
        return null;
    }

    public List<Chat> getActiveChats(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        return chatRepository.findAllByFromUserAndToUserConfirmedTrueOrToUserAndToUserConfirmedTrue(user, user)
                .stream().filter(chat -> !chat.isCanceled()).toList();
    }

    @Transactional
    public Message sendMessage(Long chatId, Long authorId, String content) {
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new NotFoundException("Chat not found"));

        if (!chat.isToUserConfirmed()) {
            throw new ForbiddenException("Chat is not confirmed");
        }

        User author = userRepository.findById(authorId)
                .orElseThrow(() -> new NotFoundException("Author not found"));

        Message message = new Message();
        message.setChat(chat);
        message.setAuthor(author);
        message.setContent(content);
        return messageRepository.save(message);
    }

    public List<Message> getMessages(Long chatId) {
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new NotFoundException("Chat not found"));

        return messageRepository.findAllByChatOrderBySentAt(chat);
    }

    @Transactional
    public Chat createChatRequest(Long fromUserId, Long toUserId) {
        if (fromUserId.equals(toUserId)) {
            throw new ForbiddenException("You cannot invite yourself to a chat");
        }

        User fromUser = userRepository.findById(fromUserId)
                .orElseThrow(() -> new NotFoundException("Sender not found"));

        User toUser = userRepository.findById(toUserId)
                .orElseThrow(() -> new NotFoundException("Recipient not found"));

        boolean chatExists = chatRepository.existsByFromUserAndToUser(fromUser, toUser) ||
                             chatRepository.existsByFromUserAndToUser(toUser, fromUser);

        if (chatExists) {
            throw new ForbiddenException("Chat request already exists");
        }

        Chat chat = new Chat();
        chat.setFromUser(fromUser);
        chat.setToUser(toUser);
        chat.setToUserConfirmed(false);

        return chatRepository.save(chat);
    }


}
