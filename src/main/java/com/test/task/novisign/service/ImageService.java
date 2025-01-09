package com.test.task.novisign.service;

import com.test.task.novisign.model.dto.ImageDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ImageService {

    Mono<ImageDto> addImage(ImageDto image);

    Mono<Void> deleteImageById(Long id);

    Flux<ImageDto> addImagesToSlideshow(List<ImageDto> images, Long id);

    Flux<ImageDto> findAllOrderByAdditionDateTime(List<Long> ids);
}
