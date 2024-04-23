package com.example.benchmark.controller;

import com.example.benchmark.domain.MvcTestLog;
import com.example.benchmark.repository.jpa.MvcMySQLRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RequiredArgsConstructor
@RequestMapping("/mysql")
@RestController
public class MvcLogMySQLController {
    private final MvcMySQLRepository mvcMySQLRepository;

    @GetMapping("/setup/{times}")
    public String setup(@PathVariable("times") int times) {
        final int chunkSize = 500000;
        int numberOfChunks = (times + chunkSize - 1) / chunkSize;

        IntStream.range(0, numberOfChunks).forEach(chunkIndex -> {
            int start = chunkIndex * chunkSize;
            int end = Math.min(start + chunkSize, times);

            List<MvcTestLog> logs = IntStream.range(start, end)
                    .mapToObj(i -> MvcTestLog.of("title" + new Random().nextInt(10000000),
                            "content" + new Random().nextInt(10000000), "author" + i))
                    .collect(Collectors.toList());

            mvcMySQLRepository.saveAll(logs);
            logs.clear();
        });
        return "Success";
    }

}
