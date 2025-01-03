package com.test.task.novisign.repository;

import com.test.task.novisign.model.Image;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends ReactiveCrudRepository<Image, Long> {
}
