package com.example.slotmachine.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

// This is the "ID Card" for a player.
@Entity // This tells Java: "This is a real thing we want to save."
@Table(name = "users") // We put all cards in a drawer called "users"
public class User {

    @Id // This is the unique number on the card.
    @GeneratedValue(strategy = GenerationType.IDENTITY) // The machine stamps a new number automatically (1, 2, 3...)
    private Long id;

    @Column(unique = true, nullable = false) // No two people can have the same name!
    private String username;

    // When did this person join?
    private LocalDateTime createdAt;

    // Before we save this card, we stamp the time on it.
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now(); 
    }

    // --- Helpers to read and write on the card (Getters and Setters) ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
