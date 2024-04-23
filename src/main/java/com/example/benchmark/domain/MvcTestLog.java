package com.example.benchmark.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "test_log")
@EntityListeners(AuditingEntityListener.class)
public class MvcTestLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String content;
    private String author;
    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public MvcTestLog(String title, String content, String author) {
        this.title = title;
        this.content = content;
        this.author = author;
    }

    public static MvcTestLog of(String title, String content, String author) {
        return new MvcTestLog(title, content, author);
    }

}
