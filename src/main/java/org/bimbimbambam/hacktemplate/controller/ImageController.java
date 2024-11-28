package org.bimbimbambam.hacktemplate.controller;

import lombok.RequiredArgsConstructor;
import org.bimbimbambam.hacktemplate.controller.request.image.ImageRequest;
import org.bimbimbambam.hacktemplate.service.ImageService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/image")
@RequiredArgsConstructor
public class ImageController {
    private final ImageService imageService;

    @PostMapping(path = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadImage(@ModelAttribute ImageRequest imageRequest) {
        return ResponseEntity.ok(imageService.upload(imageRequest));
    }

    @GetMapping("/download")
    public String downloadImage(@RequestParam("filename") String filename) {
        return imageService.getImage(filename);
    }
}

