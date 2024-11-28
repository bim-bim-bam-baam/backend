package org.bimbimbambam.hacktemplate.controller.request.user;

import org.springframework.web.multipart.MultipartFile;

public record UserUpdateAvatarReq(MultipartFile image) {
}
