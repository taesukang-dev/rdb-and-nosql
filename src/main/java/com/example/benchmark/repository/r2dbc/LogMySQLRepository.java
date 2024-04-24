package com.example.benchmark.repository.r2dbc;

import com.example.benchmark.domain.TestLog;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import reactor.core.publisher.Flux;

public interface LogMySQLRepository extends R2dbcRepository<TestLog, Long> {
    Flux<TestLog> findByContent(String content);

    Flux<TestLog> findByTitle(String title);

    @Query("select * from test_log where created_at between :start and :end limit :page, :size")
    Flux<TestLog> findAllBy(@Param("start") String start,@Param("end") String end,
                            @Param("page")int page, @Param("size")int size);

    @Query("select * from test_log where created_at between :start and :end")
    Flux<TestLog> findByCreatedAtBetween(String start, String end);
}
