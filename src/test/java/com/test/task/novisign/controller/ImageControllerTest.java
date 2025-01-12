package com.test.task.novisign.controller;

import com.test.task.novisign.model.dto.ImageDto;
import com.test.task.novisign.model.dto.ImageWithSlideshowsDto;
import com.test.task.novisign.model.dto.SlideshowDto;
import com.test.task.novisign.service.ImageService;
import com.test.task.novisign.service.SlideshowImageService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@ExtendWith(SpringExtension.class)
@WebFluxTest(ImageController.class)
class ImageControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private ImageService imageService;

    @MockitoBean
    private SlideshowImageService slideshowImageService;

    private static ImageDto IMAGE_DTO;
    private static ImageDto IMAGE_DTO_INVALID_URL;
    private static ImageWithSlideshowsDto IMAGE_WITH_SLIDESHOWS;

    @BeforeAll
    static void init() {
        IMAGE_DTO = new ImageDto();

        IMAGE_DTO.setId(1L);
        IMAGE_DTO.setName("image 1");
        IMAGE_DTO.setUrl("https://i.natgeofe.com/n/548467d8-c5f1-4551-9f58-6817a8d2c45e/NationalGeographic_2572187_2x3.jpg");
        IMAGE_DTO.setPlayDuration(Duration.ZERO);
        IMAGE_DTO.setAdditionDateTime(LocalDateTime.now());

        IMAGE_DTO_INVALID_URL = new ImageDto();

        IMAGE_DTO_INVALID_URL.setId(2L);
        IMAGE_DTO_INVALID_URL.setName("image 2");
        IMAGE_DTO_INVALID_URL.setUrl("invalid");
        IMAGE_DTO_INVALID_URL.setPlayDuration(Duration.ZERO);
        IMAGE_DTO_INVALID_URL.setAdditionDateTime(LocalDateTime.now());

        IMAGE_WITH_SLIDESHOWS = new ImageWithSlideshowsDto();

        IMAGE_WITH_SLIDESHOWS.setId(3L);
        IMAGE_WITH_SLIDESHOWS.setName("image 3");
        IMAGE_WITH_SLIDESHOWS.setUrl("url");
        IMAGE_WITH_SLIDESHOWS.setPlayDuration(Duration.ZERO);
        IMAGE_WITH_SLIDESHOWS.setAdditionDateTime(LocalDateTime.now());
        IMAGE_WITH_SLIDESHOWS.setSlideshows(List.of(new SlideshowDto()));
    }

    @Test
    void addImage() {
        Mockito.when(imageService.addImage(IMAGE_DTO))
                .thenReturn(Mono.just(IMAGE_DTO));

        StepVerifier.create(webTestClient.post()
                        .uri("/api/addImage")
                        .bodyValue(IMAGE_DTO)
                        .exchange()
                        .expectStatus()
                        .isCreated()
                        .returnResult(ImageDto.class)
                        .getResponseBody())
                .expectSubscription()
                .expectNext(IMAGE_DTO)
                .verifyComplete();
    }

    @Test
    void addImageInvalidUrl() {
        webTestClient.post()
                .uri("/api/addImage")
                .bodyValue(IMAGE_DTO_INVALID_URL)
                .exchange()
                .expectStatus()
                .isBadRequest();
    }

    @Test
    void searchImagesWithSlideshows() {
        Mockito.when(slideshowImageService.searchImagesWithSlideshows(null, Duration.ZERO))
                .thenReturn(Flux.just(IMAGE_WITH_SLIDESHOWS));

        StepVerifier.create(webTestClient.get()
                        .uri(uriBuilder ->
                                uriBuilder.path("/api/images/search")
                                        .queryParam("playDuration", "PT0S")
                                        .build())
                        .exchange()
                        .expectStatus()
                        .isOk()
                        .returnResult(ImageWithSlideshowsDto.class)
                        .getResponseBody())
                .expectSubscription()
                .expectNext(IMAGE_WITH_SLIDESHOWS)
                .verifyComplete();
    }

    @Test
    void deleteImageById() {
        Mockito.when(imageService.deleteImageById(1L))
                .thenReturn(Mono.empty());

        StepVerifier.create(webTestClient.delete()
                        .uri("/api/deleteImage/1")
                        .exchange()
                        .expectStatus()
                        .isOk()
                        .returnResult(Void.class)
                        .getResponseBody())
                .expectSubscription()
                .verifyComplete();
    }
}