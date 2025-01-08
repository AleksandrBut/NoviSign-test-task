package com.test.task.novisign.model.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public final class SlideshowDto {

    private Long id;
    private String name;
    @NotEmpty(message = "Slideshow images must be specified")
    private List<ImageDto> images;
}
