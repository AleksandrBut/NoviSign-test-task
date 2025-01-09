package com.test.task.novisign.repository;

import com.test.task.novisign.model.Image;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;

@Repository
public interface ImageRepository extends ReactiveCrudRepository<Image, Long> {

    @Query("update image set play_duration = :playDuration where id = :imageId")
    Mono<Void> updatePlayDurationByImageId(Long imageId, Duration playDuration);

    Flux<Image> findByUrlContaining(String urlKeyword);

    Flux<Image> findByPlayDurationEquals(Duration playDuration);

    Flux<Image> findAllByIdInOrderByAdditionDateTime(List<Long> ids);
}
