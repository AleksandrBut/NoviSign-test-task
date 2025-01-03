package com.test.task.novisign.model.mapper;

import com.test.task.novisign.model.Image;
import com.test.task.novisign.model.dto.ImageDto;
import org.mapstruct.Mapper;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface ImageMapper extends BaseMapper<Image, ImageDto> {

    @Override
    Image toEntity(ImageDto dto);

    @Override
    ImageDto toDto(Image entity);
}
