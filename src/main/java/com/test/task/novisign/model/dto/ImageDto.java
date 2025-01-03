package com.test.task.novisign.model.dto;

import com.test.task.novisign.validation.annotation.ImageUrl;
import jakarta.validation.constraints.NotBlank;

public record ImageDto(
        Long id,
        @NotBlank(message = "Name must not be blank")
        String name,
        @ImageUrl(message = "Provided image URL is blank or does not contain an image: ${validatedValue}")
        String url
) {
}
