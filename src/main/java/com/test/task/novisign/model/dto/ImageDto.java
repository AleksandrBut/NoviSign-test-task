package com.test.task.novisign.model.dto;

import com.test.task.novisign.validation.annotation.ImageUrl;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.Duration;
import java.time.LocalDateTime;

@Data
public class ImageDto {

    private Long id;
    @NotBlank(message = "Name must not be blank")
    private String name;
    @ImageUrl(message = "Provided image URL is blank or does not contain an image: ${validatedValue}")
    private String url;
    @NotNull(message = "Play duration must not be null")
    private Duration playDuration;
    private LocalDateTime additionDateTime;
}
