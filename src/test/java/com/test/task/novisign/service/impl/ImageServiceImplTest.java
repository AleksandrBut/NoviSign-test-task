package com.test.task.novisign.service.impl;

import com.test.task.novisign.kafka.producer.KafkaProducer;
import com.test.task.novisign.model.Image;
import com.test.task.novisign.model.SlideshowImage;
import com.test.task.novisign.model.dto.ImageDto;
import com.test.task.novisign.model.mapper.ImageMapper;
import com.test.task.novisign.repository.ImageRepository;
import com.test.task.novisign.repository.SlideshowImageRepository;
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
class ImageServiceImplTest {

    private ImageDto imageDto;
    private Image image;
    private SlideshowImage slideshowImage;

    private ImageServiceImpl imageService;

    @MockitoBean
    private ImageRepository imageRepository;
    @MockitoBean
    private SlideshowImageRepository slideshowImageRepository;
    @MockitoBean
    private ImageMapper imageMapper;
    @MockitoBean
    private KafkaProducer imageProducer;

    @BeforeAll
    public void init() {
        imageService = new ImageServiceImpl(imageRepository,
                slideshowImageRepository,
                imageMapper,
                imageProducer);

        imageDto = new ImageDto();

        imageDto.setId(1L);
        imageDto.setName("image 1");
        imageDto.setUrl("https://i.natgeofe.com/n/548467d8-c5f1-4551-9f58-6817a8d2c45e/NationalGeographic_2572187_2x3.jpg");
        imageDto.setPlayDuration(Duration.ZERO);
        imageDto.setAdditionDateTime(LocalDateTime.now());

        image = new Image(1L,
                "image 1",
                "https://i.natgeofe.com/n/548467d8-c5f1-4551-9f58-6817a8d2c45e/NationalGeographic_2572187_2x3.jpg",
                Duration.ZERO,
                LocalDateTime.now());

        slideshowImage = new SlideshowImage(1L, 1L, 1L);
    }

    @Test
    void addImage() {
        when(imageRepository.save(image))
                .thenReturn(Mono.just(image));

        when(imageMapper.toEntity(imageDto))
                .thenReturn(image);

        when(imageMapper.toDto(image))
                .thenReturn(imageDto);

        StepVerifier.create(imageService.addImage(imageDto))
                .expectSubscription()
                .expectNext(imageDto)
                .verifyComplete();
    }

    @Test
    void deleteImageById() {
        when(imageRepository.existsById(1L))
                .thenReturn(Mono.just(true));

        when(imageRepository.deleteById(1L))
                .thenReturn(Mono.empty());

        StepVerifier.create(imageService.deleteImageById(1L))
                .expectSubscription()
                .verifyComplete();
    }

    @Test
    void addImagesToSlideshow() {
        when(imageMapper.toSlideshowImages(List.of(1L), 1L))
                .thenReturn(List.of(slideshowImage));

        when(imageMapper.toDto(image))
                .thenReturn(imageDto);

        when(slideshowImageRepository.saveAll(List.of(slideshowImage)))
                .thenReturn(Flux.fromIterable(List.of(slideshowImage)));

        when(imageRepository.findAllById(List.of(1L)))
                .thenReturn(Flux.just(image));

        when(imageRepository.updatePlayDurationByImageId(1L, Duration.ZERO))
                .thenReturn(Mono.empty());

        StepVerifier.create(imageService.addImagesToSlideshow(List.of(imageDto), 1L))
                .expectSubscription()
                .expectNext(imageDto)
                .verifyComplete();
    }

    @Test
    void findAllOrderByAdditionDateTime() {
        when(imageRepository.findAllByIdInOrderByAdditionDateTime(List.of(1L)))
                .thenReturn(Flux.just(image));

        when(imageMapper.toDto(image))
                .thenReturn(imageDto);

        imageService.findAllOrderByAdditionDateTime(List.of(1L));
    }
}