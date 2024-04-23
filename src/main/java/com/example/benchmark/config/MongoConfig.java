package com.example.benchmark.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@EnableReactiveMongoRepositories(basePackages = "com.example.benchmark.repository.mongo")
@EnableMongoAuditing
@Configuration
public class MongoConfig {
}
