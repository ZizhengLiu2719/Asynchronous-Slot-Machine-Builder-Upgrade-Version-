package com.example.slotmachine.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

// This is a "Save File" or a "Photograph" of your game.
// It remembers what items you had at a specific time.
@Entity
@Table(name = "game_snapshots") // We call the box "game_snapshots" in the database
public class GameSnapshot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Whose photo is this?
    // Many photos can belong to ONE user.
    @ManyToOne 
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Which level was this? (1, 2, 3...)
    private Integer roundNumber;

    // Important! We need to save the list of items.
    // Since a list is hard to put in a small box, we write it down as a long text (JSON).
    // "TEXT" means: "Give me a huge piece of paper to write on!"
    @Column(columnDefinition = "TEXT") 
    private String inventoryJson; 

    // We also save your power numbers here as text.
    @Column(columnDefinition = "TEXT")
    private String statsJson;

    // When did we take this photo?
    private LocalDateTime createdAt;

    // Before we put this in the box, we check the clock.
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    // --- Helpers to get and set the data (Getters and Setters) ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getRoundNumber() {
        return roundNumber;
    }

    public void setRoundNumber(Integer roundNumber) {
        this.roundNumber = roundNumber;
    }

    public String getInventoryJson() {
        return inventoryJson;
    }

    public void setInventoryJson(String inventoryJson) {
        this.inventoryJson = inventoryJson;
    }

    public String getStatsJson() {
        return statsJson;
    }

    public void setStatsJson(String statsJson) {
        this.statsJson = statsJson;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
