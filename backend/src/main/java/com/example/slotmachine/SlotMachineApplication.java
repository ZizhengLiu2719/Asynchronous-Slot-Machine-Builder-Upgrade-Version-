package com.example.slotmachine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication // 这个注解告诉 Java：这是一个 Spring Boot 应用
public class SlotMachineApplication {

    public static void main(String[] args) {
        // 启动 Spring Boot
        SpringApplication.run(SlotMachineApplication.class, args);
    }

}