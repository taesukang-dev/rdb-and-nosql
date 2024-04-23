package com.example.benchmark.repository.r2dbc;

import com.example.benchmark.domain.TestLog;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface LogMySQLRepository extends R2dbcRepository<TestLog, Long> {
}
