package com.Token.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "tasks")
@Getter
@Setter
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column
    private String description;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column
    private String status; // Example: "PENDING", "COMPLETED"

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Task() {}

    public Task(String title, String description, String status, User user) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.user = user;
        this.createdAt = LocalDateTime.now();
    }

    public Task(Long id, String title, String description, String status, User user) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.user = user;
        this.createdAt = LocalDateTime.now();
    }
}
