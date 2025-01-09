package com.test.task.novisign.service;

import com.test.task.novisign.model.dto.SlideshowDto;
import reactor.core.publisher.Mono;

public interface SlideshowService {

    Mono<SlideshowDto> addSlideshow(SlideshowDto slideshowDto);

    Mono<Void> deleteSlideshow(Long id);

    Mono<SlideshowDto> findById(Long id);
}
