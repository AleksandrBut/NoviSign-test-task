package com.test.task.novisign.service.impl;

import com.test.task.novisign.exception.NotFoundException;
import com.test.task.novisign.model.Image;
import com.test.task.novisign.model.dto.ImageDto;
import com.test.task.novisign.model.mapper.ImageMapper;
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
    private final ImageMapper imageMapper;

    @Override
    public Mono<ImageDto> addImage(ImageDto imageDto) {
        return imageRepository.save(imageMapper.toEntity(imageDto))
                .map(imageMapper::toDto);
    }

    @Override
    public Flux<ImageDto> searchImages() {
        return imageRepository.findAll()
                .map(imageMapper::toDto);
    }

    @Override
    public Mono<Void> deleteImageById(Long id) {
        return imageRepository.existsById(id)
                .filter(exists -> exists)
                .switchIfEmpty(Mono.error(new NotFoundException("Image with id " + id + " is not found")))
                .then(imageRepository.deleteById(id));
    }
}
