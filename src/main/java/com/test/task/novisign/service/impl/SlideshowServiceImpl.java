package com.test.task.novisign.service.impl;

import com.test.task.novisign.exception.NotFoundException;
import com.test.task.novisign.kafka.producer.impl.SlideshowProducer;
import com.test.task.novisign.model.SlideshowImage;
import com.test.task.novisign.model.dto.SlideshowDto;
import com.test.task.novisign.model.mapper.SlideshowMapper;
import com.test.task.novisign.repository.SlideshowImageRepository;
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

    private static final String SLIDESHOW_NOT_FOUND_MESSAGE = "Slideshow with id %s is not found";

    private final SlideshowRepository slideshowRepository;
    private final SlideshowImageRepository slideshowImageRepository;
    private final SlideshowMapper slideshowMapper;
    private final ImageService imageService;
    private final SlideshowProducer slideshowProducer;

    @Override
    @Transactional
    public Mono<SlideshowDto> addSlideshow(SlideshowDto slideshowDto) {
        return slideshowRepository.save(slideshowMapper.toEntity(slideshowDto))
                .map(slideshowMapper::toDto)
                .zipWhen(slideshow -> imageService.addImagesToSlideshow(slideshowDto.getImages(), slideshow.getId()).collectList(),
                        (slideshow, images) -> {
                            slideshow.setImages(images);
                            return slideshow;
                        })
                .doOnSuccess(slideshow -> slideshowProducer.sendMessage("Slideshow added: " + slideshow));
    }

    @Override
    public Mono<Void> deleteSlideshow(Long id) {
        return slideshowRepository.existsById(id)
                .filter(exists -> exists)
                .switchIfEmpty(Mono.error(new NotFoundException(String.format(SLIDESHOW_NOT_FOUND_MESSAGE, id))))
                .then(slideshowRepository.deleteById(id))
                .doOnSuccess(unused -> slideshowProducer.sendMessage("Slideshow with id " + id + " deleted"));
    }

    @Override
    public Mono<SlideshowDto> findById(Long id) {
        return slideshowRepository.findById(id)
                .map(slideshowMapper::toDto);
    }

    @Override
    public Mono<SlideshowDto> findSlideshowWithOrderedImages(Long id) {
        return findById(id)
                .switchIfEmpty(Mono.error(new NotFoundException(String.format(SLIDESHOW_NOT_FOUND_MESSAGE, id))))
                .zipWhen(slideshowDto -> slideshowImageRepository.findAllBySlideshowId(id)
                                .map(SlideshowImage::imageId)
                                .collectList()
                                .flatMapMany(imageService::findAllOrderByAdditionDateTime)
                                .collectList(),
                        (slideshowDto, imageDtoList) -> {
                            slideshowDto.setImages(imageDtoList);

                            return slideshowDto;
                        });
    }

    @Override
    public Mono<Void> recordEventReplacedImage(Long slideshowId, Long imageId) {
        return Mono.create(monoSink -> {
            slideshowProducer.sendMessage("Image with id " + imageId + " in slideshow with id " + slideshowId + " is replaced by the next one");
            monoSink.success();
        });
    }
}
