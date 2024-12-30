package com.test.task.novisign.controller;

import com.test.task.novisign.model.Image;
import com.test.task.novisign.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/")
public class ImageController {

    private final ImageService imageService;

    @PostMapping("addImage")
    public Mono<Image> addImage(@RequestBody Image image) {
        return imageService.addImage(image);
    }

    @GetMapping("images/search")
    public Flux<Image> searchImages() {
        return imageService.searchImages();
    }

    @DeleteMapping("deleteImage/{id}")
    public Mono<Void> deleteImageById(@PathVariable Long id) {
        return imageService.deleteImageById(id);
    }
}
