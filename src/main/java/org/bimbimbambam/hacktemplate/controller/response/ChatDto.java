package org.bimbimbambam.hacktemplate.controller.response;

import org.bimbimbambam.hacktemplate.entity.Chat;

/**
 * DTO for {@link Chat}
 */
public record ChatDto(Long id, Long fromUserId, Long toUserId, boolean toUserConfirmed) {
}
