package com.example.benchmark.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

@EnableR2dbcRepositories(basePackages = "com.example.benchmark.repository.r2dbc")
@Configuration
public class MySQLConfig {
}
