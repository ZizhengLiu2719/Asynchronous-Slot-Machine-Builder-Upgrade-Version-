package com.example.slotmachine.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

// @Data // 我们暂时不用 Lombok 这个魔法了，手动写 getter/setter 虽然麻烦点，但更稳健
@Entity // 告诉 Java：这不仅仅是个代码，它对应数据库里的一张表，名字叫 user
@Table(name = "users") // 给这张表起个名字叫 "users"
public class User {

    @Id // 这是身份证号，每个人必须独一无二
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 身份证号自动生成，比如第一个人是1，第二个是2，不用我们操心
    private Long id;

    @Column(unique = true, nullable = false) // 名字必须唯一，而且不能为空（不能叫无名氏）
    private String username;

    // 注册时间。LocalDateTime 就是“现在的年月日时分秒”
    private LocalDateTime createdAt;

    // 这个方法会在泥人刚被捏出来存进数据库之前自动执行
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now(); // 记下当前时间
    }

    // --- 下面是手写的 Getters 和 Setters (取值和赋值的方法) ---

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
