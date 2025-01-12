package com.test.task.novisign.controller;

import com.test.task.novisign.model.dto.SlideshowDto;
import com.test.task.novisign.service.SlideshowService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import static org.springframework.http.HttpStatus.CREATED;

@RequiredArgsConstructor
@RestController
@RequestMapping("api")
public class SlideshowController {

    private final SlideshowService slideshowService;

    @PostMapping("/addSlideshow")
    @ResponseStatus(CREATED)
    public Mono<SlideshowDto> addSlideshow(@Valid @RequestBody SlideshowDto slideshowDto) {
        return slideshowService.addSlideshow(slideshowDto);
    }

    @DeleteMapping("/deleteSlideshow/{id}")
    public Mono<Void> deleteSlideshow(@PathVariable Long id) {
        return slideshowService.deleteSlideshow(id);
    }

    @GetMapping("/slideshow/{id}/slideshowOrder")
    public Mono<SlideshowDto> getSlideshowWithOrderedImages(@PathVariable Long id) {
        return slideshowService.findSlideshowWithOrderedImages(id);
    }

    @PostMapping("/slideshow/{slideshowId}/proof-of-play/{imageId}")
    public Mono<Void> recordEventReplacedImage(@PathVariable Long slideshowId,
                                               @PathVariable Long imageId) {
        return slideshowService.recordEventReplacedImage(slideshowId, imageId);
    }
}
