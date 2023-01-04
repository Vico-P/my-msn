package com.mymsn.entities;

import java.time.LocalDateTime;
import java.util.UUID;

import com.mymsn.utils.enums.Action;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "logs")
public class Log {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "action", length = 10)
    @Enumerated(EnumType.STRING)
    private Action action;

    @Column(name = "message", length = 255)
    private String message;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public UUID getId() {
        return this.id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Action getAction() {
        return this.action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Log id(UUID id) {
        setId(id);
        return this;
    }

    public Log action(Action action) {
        setAction(action);
        return this;
    }

    public Log message(String message) {
        setMessage(message);
        return this;
    }

    public Log createdAt(LocalDateTime createdAt) {
        setCreatedAt(createdAt);
        return this;
    }
}
