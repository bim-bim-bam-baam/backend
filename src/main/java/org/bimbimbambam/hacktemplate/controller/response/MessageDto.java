package org.bimbimbambam.hacktemplate.controller.response;

import org.bimbimbambam.hacktemplate.entity.Message;

import java.time.LocalDateTime;

/**
 * DTO for {@link Message}
 */
public record MessageDto(Long id, Long chatId, Long authorId, String content, LocalDateTime sentAt) {
}
