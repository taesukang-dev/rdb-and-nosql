package com.example.benchmark.controller;

import com.example.benchmark.domain.TestLog;
import com.example.benchmark.service.LogMongoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/mongo")
@RestController
public class LogMongoController {

    private final LogMongoService logMongoService;

    @GetMapping("/paging")
    public Flux<TestLog> paging(int page, int size) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        return logMongoService.paging(page, size)
                .doOnComplete(() -> {
                    stopWatch.stop();
                    log.info("Data fetch duration: {} s", stopWatch.getTotalTimeSeconds());
                });
    }

    @GetMapping("/find_time")
    public Flux<TestLog> findTime() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        return logMongoService.findByTime()
                .doOnComplete(() -> {
                    stopWatch.stop();
                    log.info("Data fetch duration: {} s", stopWatch.getTotalTimeSeconds());
                });
    }

    @GetMapping("/find_id/{id}")
    public Mono<TestLog> findById(@PathVariable("id") String id) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        return logMongoService.findById(id)
                .doOnSuccess(data -> {
                    stopWatch.stop();
                    log.info("Data fetch duration: {} s", stopWatch.getTotalTimeSeconds());
                });
    }

    @GetMapping("/find_title/{title}")
    public Flux<TestLog> findByTitle(@PathVariable("title") String title) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        return logMongoService.findByTitle(title)
                .doOnComplete(() -> {
                    stopWatch.stop();
                    log.info("Data fetch duration: {} s", stopWatch.getTotalTimeSeconds());
                });
    }


    @GetMapping("/find_content/{content}")
    public Flux<TestLog> findByContent(
            @PathVariable("content") String content
    ) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        return logMongoService.findByContent(content)
                .doOnComplete(() -> {
                    stopWatch.stop();
                    log.info("Data fetch duration: {} s", stopWatch.getTotalTimeSeconds());
                });
    }


    @GetMapping("/setup/{times}")
    public Flux<TestLog> setup(@PathVariable("times") int times) {
        return logMongoService.mongo_setup(times);
    }

    @GetMapping("/findall")
    public Flux<TestLog> findAllMongo() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        return logMongoService.findAll()
                .doOnComplete(() -> {
                    stopWatch.stop();
                    log.info("Data fetch duration: {} s", stopWatch.getTotalTimeSeconds());
                });
    }
}
