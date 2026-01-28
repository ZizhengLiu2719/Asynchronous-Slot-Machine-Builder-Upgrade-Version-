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

// @Data
@Entity
@Table(name = "game_snapshots") // 表名叫 game_snapshots
public class GameSnapshot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 这里有个连线：这个快照属于哪个玩家？
    // ManyToOne 的意思是：很多个快照可能都属于同一个玩家（因为玩家每玩一轮都会存个快照）
    @ManyToOne 
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // 这是第几轮？(1-10)
    private Integer roundNumber;

    // 重点来了！我们要存装备池。
    // 因为装备池是一个复杂的列表（比如有剑、盾、药水），很难用传统的表格存。
    // 我们用 JSON 格式（就像一段长长的文本描述）来存。
    // columnDefinition = "TEXT" 意思就是：给这一列很多很多空间，让我能写小作文
    @Column(columnDefinition = "TEXT") 
    private String inventoryJson; 

    // 这里存这9个装备算出来的总属性（攻击力、防御力等），也是用长文本存
    @Column(columnDefinition = "TEXT")
    private String statsJson;

    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    // --- 下面是手写的 Getters 和 Setters ---

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
