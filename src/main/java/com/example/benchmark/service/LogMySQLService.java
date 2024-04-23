package com.example.benchmark.service;

import com.example.benchmark.domain.TestLog;
import com.example.benchmark.repository.r2dbc.LogMySQLRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class LogMySQLService {

    private final LogMySQLRepository logMySQLRepository;

    public Flux<TestLog> mysql_setup(int times) {
        final int chunkSize = 1000000;

        return Flux.range(0, times)
                .buffer(chunkSize)
                .flatMap(chunk -> {
                    List<TestLog> logs = chunk.stream()
                            .map(i -> TestLog.of("title" + new Random().nextInt(10000000), "content" + new Random().nextInt(10000000), "author" + i))
                            .collect(Collectors.toList());
                    return logMySQLRepository.saveAll(logs)
                            .then(Mono.fromRunnable(logs::clear));
                });
    }

}
