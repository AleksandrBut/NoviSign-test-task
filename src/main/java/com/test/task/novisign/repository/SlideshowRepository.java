package com.test.task.novisign.repository;

import com.test.task.novisign.model.Slideshow;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SlideshowRepository extends ReactiveCrudRepository<Slideshow, Long> {

}
