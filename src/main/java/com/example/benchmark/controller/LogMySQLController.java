package com.example.benchmark.controller;

import com.example.benchmark.service.LogMySQLService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/mysql")
@RestController
public class LogMySQLController {
    private final LogMySQLService logMySQLService;




}
