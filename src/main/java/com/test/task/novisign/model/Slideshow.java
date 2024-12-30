package com.test.task.novisign.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@NoArgsConstructor
@Table
public class Slideshow {

    @Id
    private Long id;
    private String name;
}
