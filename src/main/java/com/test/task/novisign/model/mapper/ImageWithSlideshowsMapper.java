package com.test.task.novisign.model.mapper;

import com.test.task.novisign.model.Image;
import com.test.task.novisign.model.dto.ImageWithSlideshowsDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface ImageWithSlideshowsMapper extends BaseMapper<Image, ImageWithSlideshowsDto> {

    @Override
    @Mapping(target = "slideshows", ignore = true)
    ImageWithSlideshowsDto toDto(Image entity);
}
