package com.test.task.novisign.model.mapper;

import com.test.task.novisign.model.Slideshow;
import com.test.task.novisign.model.dto.SlideshowDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import static org.mapstruct.InjectionStrategy.CONSTRUCTOR;
import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING,
        uses = ImageMapper.class,
        injectionStrategy = CONSTRUCTOR)
public interface SlideshowMapper extends BaseMapper<Slideshow, SlideshowDto> {

    @Override
    Slideshow toEntity(SlideshowDto dto);

    @Override
    @Mapping(target = "images", ignore = true)
    SlideshowDto toDto(Slideshow entity);
}
