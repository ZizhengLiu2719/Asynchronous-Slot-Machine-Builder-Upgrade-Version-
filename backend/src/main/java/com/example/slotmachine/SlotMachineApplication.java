package com.example.slotmachine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication // 这是一个终极魔法阵！一旦通电，它会激活所有零件、扫描所有配置，把机器人组装起来
public class SlotMachineApplication {

    public static void main(String[] args) {
        // 按下那个红色的 START 启动按钮，机器人开始工作！
        SpringApplication.run(SlotMachineApplication.class, args);
    }

}