package com.example.benchmark.controller;

import com.example.benchmark.domain.TestLog;
import com.example.benchmark.repository.r2dbc.LogMySQLRepository;
import com.example.benchmark.service.LogMySQLService;
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
@RequestMapping("/mysql")
@RestController
public class LogMySQLController {
    private final LogMySQLService logMySQLService;
    private final LogMySQLRepository logMySQLRepository;

    @GetMapping("/find_time")
    public Flux<TestLog> findByCreatedAt() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        return logMySQLRepository.findByCreatedAtBetween(
                "2024-04-23 18:33:00",
                "2024-04-23 18:33:20")
                .doOnComplete(() -> {
                    stopWatch.stop();
                    log.info("Data fetch duration: {} s", stopWatch.getTotalTimeSeconds());
                });
    }

    @GetMapping("/paging")
    public Flux<TestLog> paging(int page, int size) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        return logMySQLRepository.findAllBy(
                "2024-04-23 18:35:00",
                "2024-04-23 18:35:15",
                page * size,
                size)
                .doOnComplete(() -> {
                    stopWatch.stop();
                    log.info("Data fetch duration: {} s", stopWatch.getTotalTimeSeconds());
                });
    }

    @GetMapping("/find_title/{title}")
    public Flux<TestLog> findByTitle(@PathVariable("title") String title) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        return logMySQLRepository.findByTitle(title)
                .doOnComplete(() -> {
                    stopWatch.stop();
                    log.info("Data fetch duration: {} s", stopWatch.getTotalTimeSeconds());
                });
    }

    @GetMapping("/find_content/{content}")
    public Flux<TestLog> findByContent(@PathVariable("content") String content) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        return logMySQLRepository.findByContent(content)
                .doOnComplete(() -> {
                    stopWatch.stop();
                    log.info("Data fetch duration: {} s", stopWatch.getTotalTimeSeconds());
                });
    }

    @GetMapping("/find_id/{id}")
    public Mono<TestLog> findById(@PathVariable("id") Long id) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        return logMySQLRepository.findById(id)
                .doOnSuccess(data -> {
                    stopWatch.stop();
                    log.info("Data fetch duration: {} s", stopWatch.getTotalTimeSeconds());
                });
    }

    @GetMapping("/findall")
    public Flux<TestLog> findAll() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        return logMySQLRepository.findAll()
                .doOnComplete(() -> {
                    stopWatch.stop();
                    log.info("Data fetch duration: {} s", stopWatch.getTotalTimeSeconds());
                });
    }

}
