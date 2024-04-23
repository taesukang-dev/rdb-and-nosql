package com.example.benchmark.repository.jpa;

import com.example.benchmark.domain.MvcTestLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MvcMySQLRepository extends JpaRepository<MvcTestLog, Long> {
}
