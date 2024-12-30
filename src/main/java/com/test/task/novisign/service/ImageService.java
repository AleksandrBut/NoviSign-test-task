package com.test.task.novisign.service;

import com.test.task.novisign.model.Image;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ImageService {

    Mono<Image> addImage(Image image);

    Flux<Image> searchImages();

    Mono<Void> deleteImageById(Long id);
}
