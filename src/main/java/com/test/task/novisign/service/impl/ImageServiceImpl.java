package com.test.task.novisign.service.impl;

import com.test.task.novisign.exception.NotFoundException;
import com.test.task.novisign.model.dto.ImageDto;
import com.test.task.novisign.model.mapper.ImageMapper;
import com.test.task.novisign.repository.ImageRepository;
import com.test.task.novisign.repository.SlideshowImageRepository;
import com.test.task.novisign.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;
    private final SlideshowImageRepository slideshowImageRepository;
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

    @Override
    public Flux<ImageDto> addImagesToSlideshow(List<ImageDto> imageDtoList, Long slideshowId) {
        var imageIds = imageDtoList.stream()
                .map(ImageDto::getId)
                .toList();

        return slideshowImageRepository.saveAll(imageMapper.mapToSlideshowImages(imageIds, slideshowId))
                .onErrorResume(DataIntegrityViolationException.class,
                        e -> Mono.error(new DataIntegrityViolationException("One or many specified images don't exist")))
                .thenMany(updatePlayDurationIfSpecified(imageDtoList))
                .thenMany(imageRepository.findAllById(imageIds)
                        .map(imageMapper::toDto));
    }

    @Override
    public Flux<ImageDto> findImagesByIds(List<Long> ids) {
        return imageRepository.findAllById(ids)
                .map(imageMapper::toDto);
    }

    private Flux<Void> updatePlayDurationIfSpecified(List<ImageDto> imageDtos) {
        return Flux.fromIterable(imageDtos)
                .filter(imageDto -> imageDto.getPlayDuration() != null)
                .flatMap(imageDto -> imageRepository.updatePlayDurationByImageId(imageDto.getId(), imageDto.getPlayDuration()));
    }
}
