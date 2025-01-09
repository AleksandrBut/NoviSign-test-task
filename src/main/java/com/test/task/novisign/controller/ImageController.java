package com.test.task.novisign.controller;

import com.test.task.novisign.model.dto.ImageDto;
import com.test.task.novisign.model.dto.ImageWithSlideshowsDto;
import com.test.task.novisign.service.ImageService;
import com.test.task.novisign.service.SlideshowImageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

import static org.springframework.http.HttpStatus.CREATED;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/")
public class ImageController {

    private final ImageService imageService;
    private final SlideshowImageService slideshowImageService;

    @PostMapping("addImage")
    @ResponseStatus(CREATED)
    public Mono<ImageDto> addImage(@Valid @RequestBody ImageDto imageDto) {
        return imageService.addImage(imageDto);
    }

    @GetMapping("images/search")
    public Flux<ImageWithSlideshowsDto> searchImagesWithSlideshows(@RequestParam(required = false) String urlKeyword,
                                                                   @RequestParam(required = false) Duration playDuration) {
        return slideshowImageService.searchImagesWithSlideshows(urlKeyword, playDuration);
    }

    @DeleteMapping("deleteImage/{id}")
    public Mono<Void> deleteImageById(@PathVariable Long id) {
        return imageService.deleteImageById(id);
    }
}
