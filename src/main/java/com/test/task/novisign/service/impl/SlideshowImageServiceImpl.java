package com.test.task.novisign.service.impl;

import com.test.task.novisign.model.Image;
import com.test.task.novisign.model.dto.ImageWithSlideshowsDto;
import com.test.task.novisign.model.mapper.ImageWithSlideshowsMapper;
import com.test.task.novisign.repository.ImageRepository;
import com.test.task.novisign.repository.SlideshowImageRepository;
import com.test.task.novisign.service.SlideshowImageService;
import com.test.task.novisign.service.SlideshowService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import reactor.core.publisher.Flux;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class SlideshowImageServiceImpl implements SlideshowImageService {

    private final SlideshowService slideshowService;
    private final SlideshowImageRepository slideshowImageRepository;
    private final ImageRepository imageRepository;
    private final ImageWithSlideshowsMapper imageWithSlideshowsMapper;

    @Override
    public Flux<ImageWithSlideshowsDto> searchImagesWithSlideshows(String urlKeyword, Duration playDuration) {
        Flux<Image> imageFlux = ObjectUtils.isEmpty(urlKeyword) ?
                imageRepository.findByPlayDurationEquals(playDuration) :
                imageRepository.findByUrlContaining(urlKeyword);

        return imageFlux.map(imageWithSlideshowsMapper::toDto)
                .delayUntil(imageWithSlideshowsDto ->
                        slideshowImageRepository.findAllByImageId(imageWithSlideshowsDto.getId())
                                .flatMap(slideshowImage -> slideshowService.findById(slideshowImage.slideshowId()))
                                .collectList()
                                .map(slideshowDtoList -> {
                                    imageWithSlideshowsDto.setSlideshows(slideshowDtoList);

                                    return imageWithSlideshowsDto;
                                }));
    }
}
