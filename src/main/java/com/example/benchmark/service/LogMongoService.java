package com.example.benchmark.service;

import com.example.benchmark.domain.TestLog;
import com.example.benchmark.repository.mongo.LogMongoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class LogMongoService {

    private final LogMongoRepository logMongoRepository;
    private final ReactiveMongoTemplate reactiveMongoTemplate;

    public Flux<TestLog> paging(int page, int size) {
        String start = "2024-04-23T04:15:34.230+00:00";
        String end = "2024-04-23T04:15:35.000+00:00";

        ZonedDateTime startTime = ZonedDateTime.parse(start, DateTimeFormatter.ISO_DATE_TIME);
        ZonedDateTime endTime = ZonedDateTime.parse(end, DateTimeFormatter.ISO_DATE_TIME);

        Criteria criteria = Criteria.where("createdAt").gte(startTime.toInstant()).lte(endTime.toInstant());
//        Query query = new Query(criteria).skip((page - 1) * size).limit(size);
        Query query = new Query().skip((page - 1) * size).limit(size);
        return reactiveMongoTemplate.find(query, TestLog.class);
    }

    public Flux<TestLog> findByTime() {
        String start = "2024-04-23T04:15:34.230+00:00";
        String end = "2024-04-23T04:15:35.000+00:00";

        ZonedDateTime startTime = ZonedDateTime.parse(start, DateTimeFormatter.ISO_DATE_TIME);
        ZonedDateTime endTime = ZonedDateTime.parse(end, DateTimeFormatter.ISO_DATE_TIME);

        Criteria criteria = Criteria.where("createdAt").gte(startTime.toInstant()).lte(endTime.toInstant());
        Query query = Query.query(criteria);

        return reactiveMongoTemplate.find(query, TestLog.class, "test-log");
    }

    public Flux<TestLog> mongo_setup(int times) {
        final int chunkSize = 100000;

        return Flux.range(0, times)
                .buffer(chunkSize)
                .flatMap(chunk -> {
                    List<TestLog> logs = chunk.stream()
                            .map(i -> TestLog.of("title" + new Random().nextInt(10000000), "content" + new Random().nextInt(10000000), "author" + i))
                            .collect(Collectors.toList());
                    return logMongoRepository.saveAll(logs)
                            .then(Mono.fromRunnable(logs::clear));
                });
    }

    public Flux<TestLog> findAll() {
        return logMongoRepository.findAll();
    }

    public Flux<TestLog> findByContent(String content) {
        return logMongoRepository.findByContent(content);
    }

    public Mono<TestLog> findById(String id) {
        return logMongoRepository.findById(id);
    }

    public Flux<TestLog> findByTitle(String title) {
        return logMongoRepository.findByTitle(title);
    }
}
