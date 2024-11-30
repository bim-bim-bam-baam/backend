package org.bimbimbambam.hacktemplate.controller.response;

public record MessageDto(Long id, Long chatId, Long authorId, boolean isMe, String content) {
}
