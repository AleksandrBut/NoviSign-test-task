package com.test.task.novisign.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table
public record Slideshow(
        @Id
        Long id,
        String name) {
}
