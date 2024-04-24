package com.example.benchmark.domain;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@Table("test_log")
@Document(collection = "test-log")
public class TestLog {
    @Id
    @Column("id")
    private String id;
    private String title;
    private String content;
    private String author;
    @CreatedDate
    @Column("created_at")
    private LocalDateTime createdAt;

    public TestLog(String title, String content, String author) {
        this.title = title;
        this.content = content;
        this.author = author;
    }

    public static TestLog of(String title, String content, String author) {
        return new TestLog(title, content, author);
    }
}
