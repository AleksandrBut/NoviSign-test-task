package com.test.task.novisign.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Duration;
import java.time.LocalDateTime;

@Table
public record Image(
        @Id
        Long id,
        String name,
        String url,
        Duration playDuration,
        LocalDateTime additionDateTime
) {
}
