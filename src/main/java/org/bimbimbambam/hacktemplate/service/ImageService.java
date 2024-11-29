package org.bimbimbambam.hacktemplate.service;

import lombok.SneakyThrows;
import org.bimbimbambam.hacktemplate.controller.request.ImageRequest;

public interface ImageService {
    @SneakyThrows
    String upload(ImageRequest imageRequest);

    @SneakyThrows
    String getImage(String filename);
}
