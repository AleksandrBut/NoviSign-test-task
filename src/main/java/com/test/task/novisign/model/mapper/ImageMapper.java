package com.test.task.novisign.model.mapper;

import com.test.task.novisign.model.Image;
import com.test.task.novisign.model.SlideshowImage;
import com.test.task.novisign.model.dto.ImageDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface ImageMapper extends BaseMapper<Image, ImageDto> {

    @Override
    @Mapping(target = "additionDateTime", expression = "java(LocalDateTime.now())")
    Image toEntity(ImageDto dto);

    @Override
    ImageDto toDto(Image entity);

    default List<SlideshowImage> mapToSlideshowImages(List<Long> imageIds, Long slideshowId) {
        return imageIds.stream()
                .map(imageId -> new SlideshowImage(null, imageId, slideshowId))
                .toList();
    }
}
