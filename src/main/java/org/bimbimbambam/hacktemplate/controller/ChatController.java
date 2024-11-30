package org.bimbimbambam.hacktemplate.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.bimbimbambam.hacktemplate.controller.response.ChatDto;
import org.bimbimbambam.hacktemplate.controller.response.MessageDto;
import org.bimbimbambam.hacktemplate.entity.Chat;
import org.bimbimbambam.hacktemplate.entity.Message;
import org.bimbimbambam.hacktemplate.entity.User;
import org.bimbimbambam.hacktemplate.repository.UserRepository;
import org.bimbimbambam.hacktemplate.service.ChatService;
import org.bimbimbambam.hacktemplate.utils.JwtUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/chat")
public class ChatController {
    private final ChatService charService;
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;

    @GetMapping("/sent-requests")
    @SecurityRequirement(name = "bearerAuth")
    public List<ChatDto> getSentRequests() {
        Long userId = jwtUtils.extractId(jwtUtils.getJwtToken());
        return charService.getSentRequests(userId).stream().map(chat -> toDto(chat, userId)).toList();
    }

    @GetMapping("/pending-requests")
    @SecurityRequirement(name = "bearerAuth")
    public List<ChatDto> getPendingRequests() {
        Long userId = jwtUtils.extractId(jwtUtils.getJwtToken());
        return charService.getPendingRequests(userId).stream().map(chat -> toDto(chat, userId)).toList();
    }

    @GetMapping("/active")
    @SecurityRequirement(name = "bearerAuth")
    public List<ChatDto> getActiveChats() {
        Long userId = jwtUtils.extractId(jwtUtils.getJwtToken());
        return charService.getActiveChats(userId).stream().map(chat -> toDto(chat, userId)).toList();
    }

    @PostMapping("/{chatId}/messages")
    @SecurityRequirement(name = "bearerAuth")
    public MessageDto sendMessage(@PathVariable Long chatId, @RequestBody String content) {
        Long userId = jwtUtils.extractId(jwtUtils.getJwtToken());
        return toDto(charService.sendMessage(chatId, userId, content), userId);
    }

    @GetMapping("/{chatId}/messages")
    @SecurityRequirement(name = "bearerAuth")
    public List<MessageDto> getMessages(@PathVariable Long chatId) {
        Long userId = jwtUtils.extractId(jwtUtils.getJwtToken());
        return charService.getMessages(chatId).stream().map(message -> toDto(message, userId)).toList();
    }

    @PostMapping("/invite/{toUserId}")
    @SecurityRequirement(name = "bearerAuth")
    public ChatDto inviteToChat(@PathVariable Long toUserId) {
        Long fromUserId = jwtUtils.extractId(jwtUtils.getJwtToken());
        return toDto(charService.createChatRequest(fromUserId, toUserId), fromUserId);
    }

    @PostMapping("/{chatId}/accept")
    @SecurityRequirement(name = "bearerAuth")
    public void acceptChatRequest(@PathVariable Long chatId) {
        Long userId = jwtUtils.extractId(jwtUtils.getJwtToken());
        charService.acceptChatRequest(chatId, userId);
    }

    @PostMapping("/{chatId}/decline")
    @SecurityRequirement(name = "bearerAuth")
    public void declineChatRequest(@PathVariable Long chatId) {
        Long userId = jwtUtils.extractId(jwtUtils.getJwtToken());
        charService.declineChatRequest(chatId, userId);
    }

    private ChatDto toDto(Chat chat, Long userId) {
        Long toUserId = chat.getFromUser().getId() + chat.getToUser().getId() - userId;
        User user = userRepository.findById(toUserId).orElseThrow();

        return new ChatDto(
                chat.getId(),
                user.getId(),
                user.getUsername()
        );
    }

    private MessageDto toDto(Message message, Long userId) {
        return new MessageDto(
                message.getId(),
                message.getChat().getId(),
                message.getAuthor().getId(),
                message.getAuthor().getId().equals(userId),
                message.getContent()
        );
    }
}

