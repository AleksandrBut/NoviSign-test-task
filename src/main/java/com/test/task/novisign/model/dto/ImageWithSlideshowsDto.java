package com.test.task.novisign.model.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class ImageWithSlideshowsDto extends ImageDto {

    private List<SlideshowDto> slideshows;
}
