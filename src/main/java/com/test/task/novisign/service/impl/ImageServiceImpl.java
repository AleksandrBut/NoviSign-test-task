package com.test.task.novisign.service.impl;

import com.test.task.novisign.model.Image;
import com.test.task.novisign.repository.ImageRepository;
import com.test.task.novisign.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;

    @Override
    public Mono<Image> addImage(Image image) {
        return imageRepository.save(image);
    }

    @Override
    public Flux<Image> searchImages() {
        return imageRepository.findAll();
    }

    @Override
    public Mono<Void> deleteImageById(Long id) {
        return imageRepository.deleteById(id);
    }
}
