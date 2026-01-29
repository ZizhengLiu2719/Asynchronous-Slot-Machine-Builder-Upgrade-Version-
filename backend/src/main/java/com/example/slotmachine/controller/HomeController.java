package com.example.slotmachine.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "<h1>🎰 Slot Machine Backend is Running!</h1><p>Please visit the Frontend to play: <a href='http://localhost'>http://localhost</a></p>";
    }
}
