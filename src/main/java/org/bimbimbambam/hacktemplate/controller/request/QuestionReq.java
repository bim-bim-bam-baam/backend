package org.bimbimbambam.hacktemplate.controller.request;

import org.springframework.web.multipart.MultipartFile;

public record QuestionReq(String content, MultipartFile file) {

}
