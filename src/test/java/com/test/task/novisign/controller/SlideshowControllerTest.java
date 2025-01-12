package com.test.task.novisign.controller;

import com.test.task.novisign.model.dto.ImageDto;
import com.test.task.novisign.model.dto.SlideshowDto;
import com.test.task.novisign.service.SlideshowService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

@ExtendWith(SpringExtension.class)
@WebFluxTest(SlideshowController.class)
class SlideshowControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private SlideshowService slideshowService;

    private static SlideshowDto SLIDESHOW_DTO;

    @BeforeAll
    static void init() {
        SLIDESHOW_DTO = new SlideshowDto();

        SLIDESHOW_DTO.setId(1L);
        SLIDESHOW_DTO.setName("slideshow 1");
        SLIDESHOW_DTO.setImages(List.of(new ImageDto()));
    }

    @Test
    void addSlideshow() {
        Mockito.when(slideshowService.addSlideshow(SLIDESHOW_DTO))
                .thenReturn(Mono.just(SLIDESHOW_DTO));

        StepVerifier.create(webTestClient.post()
                        .uri("/api/addSlideshow")
                        .bodyValue(SLIDESHOW_DTO)
                        .exchange()
                        .expectStatus()
                        .isCreated()
                        .returnResult(SlideshowDto.class)
                        .getResponseBody())
                .expectSubscription()
                .expectNext(SLIDESHOW_DTO)
                .verifyComplete();
    }

    @Test
    void deleteSlideshow() {
        Mockito.when(slideshowService.deleteSlideshow(1L))
                .thenReturn(Mono.empty());

        StepVerifier.create(webTestClient.delete()
                        .uri("/api/deleteSlideshow/1")
                        .exchange()
                        .expectStatus()
                        .isOk()
                        .returnResult(Void.class)
                        .getResponseBody())
                .expectSubscription()
                .verifyComplete();
    }

    @Test
    void getSlideshowWithOrderedImages() {
        Mockito.when(slideshowService.findSlideshowWithOrderedImages(1L))
                .thenReturn(Mono.just(SLIDESHOW_DTO));

        StepVerifier.create(webTestClient.get()
                        .uri("/api/slideshow/1/slideshowOrder")
                        .exchange()
                        .expectStatus()
                        .isOk()
                        .returnResult(SlideshowDto.class)
                        .getResponseBody())
                .expectSubscription()
                .expectNext(SLIDESHOW_DTO)
                .verifyComplete();
    }

    @Test
    void recordEventReplacedImage() {
        Mockito.when(slideshowService.recordEventReplacedImage(1L, 1L))
                .thenReturn(Mono.empty());

        StepVerifier.create(webTestClient.post()
                        .uri("/api/slideshow/1/proof-of-play/1")
                        .exchange()
                        .expectStatus()
                        .isOk()
                        .returnResult(Void.class)
                        .getResponseBody())
                .expectSubscription()
                .verifyComplete();
    }
}