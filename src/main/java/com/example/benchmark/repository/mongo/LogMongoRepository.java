package com.example.benchmark.repository.mongo;

import com.example.benchmark.domain.TestLog;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface LogMongoRepository extends ReactiveMongoRepository<TestLog, String> {
    Flux<TestLog> findAllBy();

    Flux<TestLog> findByContent(String content);

    Flux<TestLog> findByTitle(String title);

    @Query("{createdAt: {$gte : ?0, $lte : ?1}}")
    Flux<TestLog> findByCreatedAtBetween(String start, String end);
}
