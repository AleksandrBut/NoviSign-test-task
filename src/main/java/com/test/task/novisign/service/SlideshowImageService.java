package com.test.task.novisign.service;

import com.test.task.novisign.model.dto.ImageWithSlideshowsDto;
import reactor.core.publisher.Flux;

import java.time.Duration;

public interface SlideshowImageService {

    Flux<ImageWithSlideshowsDto> searchImagesWithSlideshows(String urlKeyword, Duration playDuration);
}
