package com.test.task.novisign.service.impl;

import com.test.task.novisign.exception.NotFoundException;
import com.test.task.novisign.model.dto.SlideshowDto;
import com.test.task.novisign.model.mapper.SlideshowMapper;
import com.test.task.novisign.repository.SlideshowRepository;
import com.test.task.novisign.service.ImageService;
import com.test.task.novisign.service.SlideshowService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class SlideshowServiceImpl implements SlideshowService {

    private final SlideshowRepository slideshowRepository;
    private final SlideshowMapper slideshowMapper;
    private final ImageService imageService;

    @Override
    @Transactional
    public Mono<SlideshowDto> addSlideshow(SlideshowDto slideshowDto) {
        return slideshowRepository.save(slideshowMapper.toEntity(slideshowDto))
                .map(slideshowMapper::toDto)
                .zipWhen(slideshow -> imageService.addImagesToSlideshow(slideshowDto.getImages(), slideshow.getId()).collectList(),
                        (slideshow, images) -> {
                            slideshow.setImages(images);
                            return slideshow;
                        });
    }
}
