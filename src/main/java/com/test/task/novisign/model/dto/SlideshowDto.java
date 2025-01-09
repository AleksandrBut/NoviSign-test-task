package com.test.task.novisign.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;

@Data
public class SlideshowDto {

    private Long id;
    private String name;
    @NotEmpty(message = "Slideshow images must be specified")
    @JsonInclude(NON_EMPTY)
    private List<ImageDto> images;
}
