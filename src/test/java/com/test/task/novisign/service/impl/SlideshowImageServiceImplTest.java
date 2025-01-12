package com.test.task.novisign.service.impl;

import com.test.task.novisign.model.Image;
import com.test.task.novisign.model.SlideshowImage;
import com.test.task.novisign.model.dto.ImageDto;
import com.test.task.novisign.model.dto.ImageWithSlideshowsDto;
import com.test.task.novisign.model.dto.SlideshowDto;
import com.test.task.novisign.model.mapper.ImageWithSlideshowsMapper;
import com.test.task.novisign.repository.ImageRepository;
import com.test.task.novisign.repository.SlideshowImageRepository;
import com.test.task.novisign.service.SlideshowService;
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
class SlideshowImageServiceImplTest {

    private SlideshowImageServiceImpl slideshowImageService;

    @MockitoBean
    private SlideshowService slideshowService;
    @MockitoBean
    private SlideshowImageRepository slideshowImageRepository;
    @MockitoBean
    private ImageRepository imageRepository;
    @MockitoBean
    private ImageWithSlideshowsMapper imageWithSlideshowsMapper;

    private ImageWithSlideshowsDto imageWithSlideshowsDto;
    private Image image;
    private SlideshowDto slideshowDto;
    private SlideshowImage slideshowImage;

    @BeforeAll
    public void init() {
        slideshowImageService = new SlideshowImageServiceImpl(slideshowService,
                slideshowImageRepository,
                imageRepository,
                imageWithSlideshowsMapper
        );

        imageWithSlideshowsDto = new ImageWithSlideshowsDto();
        imageWithSlideshowsDto.setId(1L);
        imageWithSlideshowsDto.setName("image 3");
        imageWithSlideshowsDto.setUrl("url");
        imageWithSlideshowsDto.setPlayDuration(Duration.ZERO);
        imageWithSlideshowsDto.setAdditionDateTime(LocalDateTime.now());
        imageWithSlideshowsDto.setSlideshows(List.of(new SlideshowDto()));

        image = new Image(1L,
                "image 1",
                "https://i.natgeofe.com/n/548467d8-c5f1-4551-9f58-6817a8d2c45e/NationalGeographic_2572187_2x3.jpg",
                Duration.ZERO,
                LocalDateTime.now());

        ImageDto imageDto = new ImageDto();
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
    }

    @Test
    void searchImagesWithSlideshows() {
        when(imageRepository.findByPlayDurationEquals(Duration.ZERO))
                .thenReturn(Flux.just(image));

        when(imageWithSlideshowsMapper.toDto(image))
                .thenReturn(imageWithSlideshowsDto);

        when(slideshowImageRepository.findAllByImageId(1L))
                .thenReturn(Flux.just(slideshowImage));

        when(slideshowService.findById(1L))
                .thenReturn(Mono.just(slideshowDto));

        StepVerifier.create(slideshowImageService.searchImagesWithSlideshows(null, Duration.ZERO))
                .expectSubscription()
                .expectNext(imageWithSlideshowsDto)
                .verifyComplete();
    }
}