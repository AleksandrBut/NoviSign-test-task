package com.test.task.novisign.service;

import com.test.task.novisign.model.dto.ImageDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ImageService {

    Mono<ImageDto> addImage(ImageDto image);

    Flux<ImageDto> searchImages();

    Mono<Void> deleteImageById(Long id);
}
