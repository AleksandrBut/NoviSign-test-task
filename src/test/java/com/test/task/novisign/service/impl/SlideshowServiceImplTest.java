package com.test.task.novisign.service.impl;


import com.test.task.novisign.kafka.producer.impl.SlideshowProducer;
import com.test.task.novisign.model.Slideshow;
import com.test.task.novisign.model.SlideshowImage;
import com.test.task.novisign.model.dto.ImageDto;
import com.test.task.novisign.model.dto.SlideshowDto;
import com.test.task.novisign.model.mapper.SlideshowMapper;
import com.test.task.novisign.repository.SlideshowImageRepository;
import com.test.task.novisign.repository.SlideshowRepository;
import com.test.task.novisign.service.ImageService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;
import static org.mockito.Mockito.when;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
@TestInstance(PER_CLASS)
class SlideshowServiceImplTest {

    private SlideshowServiceImpl slideshowService;

    @MockitoBean
    private SlideshowRepository slideshowRepository;
    @MockitoBean
    private SlideshowImageRepository slideshowImageRepository;
    @MockitoBean
    private SlideshowMapper slideshowMapper;
    @MockitoBean
    private ImageService imageService;
    @MockitoBean
    private SlideshowProducer slideshowProducer;

    private ImageDto imageDto;
    private SlideshowDto slideshowDto;
    private Slideshow slideshow;
    private SlideshowImage slideshowImage;

    @BeforeAll
    public void init() {
        slideshowService = new SlideshowServiceImpl(slideshowRepository,
                slideshowImageRepository,
                slideshowMapper,
                imageService,
                slideshowProducer);

        imageDto = new ImageDto();
        imageDto.setId(1L);
        imageDto.setName("image 1");
        imageDto.setUrl("https://i.natgeofe.com/n/548467d8-c5f1-4551-9f58-6817a8d2c45e/NationalGeographic_2572187_2x3.jpg");
        imageDto.setPlayDuration(Duration.ZERO);
        imageDto.setAdditionDateTime(LocalDateTime.now());

        slideshowImage = new SlideshowImage(1L, 1L, 1L);
        slideshowDto = new SlideshowDto();
        slideshowDto.setId(1L);
        slideshowDto.setName("slideshow 1");
        slideshowDto.setImages(List.of(imageDto));

        slideshow = new Slideshow(1L, "slideshow");
    }

    @Test
    void addSlideshow() {
        when(slideshowRepository.save(slideshow))
                .thenReturn(Mono.just(slideshow));

        when(slideshowMapper.toEntity(slideshowDto))
                .thenReturn(slideshow);

        when(slideshowMapper.toDto(slideshow))
                .thenReturn(slideshowDto);

        when(imageService.addImagesToSlideshow(List.of(imageDto), 1L))
                .thenReturn(Flux.just(imageDto));

        StepVerifier.create(slideshowService.addSlideshow(slideshowDto))
                .expectSubscription()
                .expectNext(slideshowDto)
                .verifyComplete();
    }

    @Test
    void deleteSlideshow() {
        when(slideshowRepository.existsById(1L))
                .thenReturn(Mono.just(true));

        when(slideshowRepository.deleteById(1L))
                .thenReturn(Mono.empty());

        StepVerifier.create(slideshowService.deleteSlideshow(1L))
                .expectSubscription()
                .verifyComplete();
    }

    @Test
    void findById() {
        when(slideshowRepository.findById(1L))
                .thenReturn(Mono.just(slideshow));

        when(slideshowMapper.toDto(slideshow))
                .thenReturn(slideshowDto);

        StepVerifier.create(slideshowService.findById(1L))
                .expectSubscription()
                .expectNext(slideshowDto)
                .verifyComplete();
    }

    @Test
    void findSlideshowWithOrderedImages() {
        when(slideshowRepository.findById(1L))
                .thenReturn(Mono.just(slideshow));

        when(slideshowMapper.toDto(slideshow))
                .thenReturn(slideshowDto);

        when(slideshowImageRepository.findAllBySlideshowId(1L))
                .thenReturn(Flux.just(slideshowImage));

        when(imageService.findAllOrderByAdditionDateTime(List.of(1L)))
                .thenReturn(Flux.just(imageDto));

        StepVerifier.create(slideshowService.findSlideshowWithOrderedImages(1L))
                .expectSubscription()
                .expectNext(slideshowDto)
                .verifyComplete();
    }

    @Test
    void recordEventReplacedImage() {
        StepVerifier.create(slideshowService.recordEventReplacedImage(1L, 2L))
                .expectSubscription()
                .verifyComplete();
    }
}