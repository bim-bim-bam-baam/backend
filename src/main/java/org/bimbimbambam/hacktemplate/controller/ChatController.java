package org.bimbimbambam.hacktemplate.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.bimbimbambam.hacktemplate.controller.response.ChatDto;
import org.bimbimbambam.hacktemplate.controller.response.MessageDto;
import org.bimbimbambam.hacktemplate.mapper.ChatMapper;
import org.bimbimbambam.hacktemplate.mapper.MessageMapper;
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

    private final ChatMapper chatMapper;

    private final MessageMapper messageMapper;

    @GetMapping("/sent-requests")
    @SecurityRequirement(name = "bearerAuth")
    public List<ChatDto> getSentRequests() {
        Long userId = jwtUtils.extractId(jwtUtils.getJwtToken());
        return charService.getSentRequests(userId).stream().map(chatMapper::toDto).toList();
    }

    @GetMapping("/pending-requests")
    @SecurityRequirement(name = "bearerAuth")
    public List<ChatDto> getPendingRequests() {
        Long userId = jwtUtils.extractId(jwtUtils.getJwtToken());
        return charService.getPendingRequests(userId).stream().map(chatMapper::toDto).toList();
    }

    @PostMapping("/{chatId}/accept")
    @SecurityRequirement(name = "bearerAuth")
    public void acceptChatRequest(@PathVariable Long chatId) {
        Long userId = jwtUtils.extractId(jwtUtils.getJwtToken());
        charService.acceptChatRequest(chatId, userId);
    }

    @GetMapping("/active")
    @SecurityRequirement(name = "bearerAuth")
    public List<ChatDto> getActiveChats() {
        Long userId = jwtUtils.extractId(jwtUtils.getJwtToken());
        return charService.getActiveChats(userId).stream().map(chatMapper::toDto).toList();
    }

    @PostMapping("/{chatId}/messages")
    @SecurityRequirement(name = "bearerAuth")
    public MessageDto sendMessage(@PathVariable Long chatId, @RequestBody String content) {
        Long userId = jwtUtils.extractId(jwtUtils.getJwtToken());
        return messageMapper.toDto(charService.sendMessage(chatId, userId, content));
    }

    @GetMapping("/{chatId}/messages")
    @SecurityRequirement(name = "bearerAuth")
    public List<MessageDto> getMessages(@PathVariable Long chatId) {
        return charService.getMessages(chatId).stream().map(messageMapper::toDto).toList();
    }

    @PostMapping("/invite/{toUserId}")
    @SecurityRequirement(name = "bearerAuth")
    public ChatDto inviteToChat(@PathVariable Long toUserId) {
        Long fromUserId = jwtUtils.extractId(jwtUtils.getJwtToken());
        return chatMapper.toDto(charService.createChatRequest(fromUserId, toUserId));
    }
}

