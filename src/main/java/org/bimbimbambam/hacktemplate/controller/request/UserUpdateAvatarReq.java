package org.bimbimbambam.hacktemplate.controller.request;

import org.springframework.web.multipart.MultipartFile;

public record UserUpdateAvatarReq(MultipartFile image) {
}
