package com.test.task.novisign.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Duration;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Table
public class SlideshowImage {

    @Id
    private Long id;
    private Image image;
    private Slideshow slideshow;
    private Duration playDuration;
    private LocalDateTime additionDate;
}
