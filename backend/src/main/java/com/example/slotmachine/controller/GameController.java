package com.example.slotmachine.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.slotmachine.model.BattleResult;
import com.example.slotmachine.model.GameSnapshot;
import com.example.slotmachine.model.User;
import com.example.slotmachine.service.GameService;

@RestController // This tells the computer: "I am the receptionist who answers the phone calls!"
@RequestMapping("/api") // All calls to me must start with "/api"
@CrossOrigin(origins = "*") // I am friendly! I talk to anyone, even the website.
public class GameController {

    @Autowired
    private GameService gameService;

    // 1. Sign In Door
    // Someone knocks and says: "Let me in, my name is..."
    // Request: POST /api/login?username=xxx
    @PostMapping("/login")
    public User login(@RequestParam String username) {
        return gameService.loginOrRegister(username);
    }

    // 2. Shop Shelf
    // Someone asks: "What can I buy in round 1?"
    // Request: GET /api/shop?round=1
    @GetMapping("/shop")
    public List<String> getShopItems(@RequestParam int round) {
        return gameService.refreshShop(round);
    }

    // 3. Camera Snap
    // Someone says: "Save my game!"
    // Request: POST /api/snapshot (with a box of data)
    @PostMapping("/snapshot")
    public GameSnapshot uploadSnapshot(@RequestBody Map<String, Object> payload) {
        // Unpacking the box of data
        Long userId = Long.valueOf(payload.get("userId").toString());
        int round = Integer.parseInt(payload.get("round").toString());
        String inventory = payload.get("inventory").toString();
        String stats = payload.get("stats").toString();

        return gameService.saveSnapshot(userId, round, inventory, stats);
    }
    
    // 4. Find a Challenger
    // Someone asks: "Who can I fight in round 1?"
    // Request: GET /api/opponent?round=1
    @GetMapping("/opponent")
    public GameSnapshot getOpponent(@RequestParam int round) {
        GameSnapshot opponent = gameService.findOpponent(round);
        if (opponent == null) {
            // If nobody is around, we return nothing (or a ghost!)
            // In real life, we might send a Robot here.
            return null; 
        }
        return opponent;
    }

    // 5. The Arena (Main Fight)
    // Someone says: "I want to fight! Here is my bag of items."
    // Request: POST /api/battle
    // Box contents: { "inventory": "['Sword', ...]", "round": 1 }
    @PostMapping("/battle")
    public BattleResult fight(@RequestBody Map<String, Object> payload) {
        String inventoryJson = payload.get("inventory").toString();
        int round = Integer.parseInt(payload.get("round").toString());
        
        // 1. Find a bad guy to fight
        GameSnapshot opponent = gameService.findOpponent(round);
        String opponentInventory = (opponent != null) ? opponent.getInventoryJson() : null;
        
        // 2. Start the math fight!
        return gameService.calculateBattle(inventoryJson, opponentInventory);
    }
}
