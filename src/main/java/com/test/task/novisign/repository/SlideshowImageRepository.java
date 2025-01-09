package com.test.task.novisign.repository;

import com.test.task.novisign.model.SlideshowImage;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface SlideshowImageRepository extends ReactiveCrudRepository<SlideshowImage, Long> {

    Flux<SlideshowImage> findAllByImageId(Long imageId);

    Flux<SlideshowImage> findAllBySlideshowId(Long slideshowId);
}
