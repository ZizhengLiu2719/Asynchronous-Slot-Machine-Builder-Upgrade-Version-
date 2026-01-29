package com.example.slotmachine.service;

import java.util.ArrayList; // Like a toy box that can grow bigger
import java.util.List;
import java.util.Random; // A magic dice roller

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.slotmachine.model.BattleResult;
import com.example.slotmachine.model.GameSnapshot;
import com.example.slotmachine.model.User;
import com.example.slotmachine.repository.GameSnapshotRepository;
import com.example.slotmachine.repository.UserRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service // This tells the computer: "I am a worker who does the main job!"
public class GameService {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private GameSnapshotRepository snapshotRepository;

    // A tool to turn text into lists, like reading a grocery list
    private final ObjectMapper objectMapper = new ObjectMapper();

    // This helps a user sign in or creates a new name tag for them if they are new
    public User loginOrRegister(String username) {
        return userRepository.findByUsername(username)
                .orElseGet(() -> {
                    // If we don't know this person, we make a new card for them
                    User newUser = new User();
                    newUser.setUsername(username);
                    return userRepository.save(newUser);
                });
    }

    // This fills the shop with toys based on how many rounds we played
    public List<String> refreshShop(int round) {
        // Simple toys for beginners
        String[] poolTier1 = {"Sword", "Shield", "Cookie", "Dart", "Buckler"};
        // Cooler toys for pros
        String[] poolTier2 = {"VampireFang", "FrostStaff", "SpikedShield"};
        List<String> shopItems = new ArrayList<>();
        Random random = new Random(); // Magic dice

        // We pick 5 items to show in the shop
        for (int i = 0; i < 5; i++) {
            if (round <= 3) {
                // If the game just started, only show simple toys
                shopItems.add(poolTier1[random.nextInt(poolTier1.length)]);
            } else {
                // Later on, flip a coin to decide if we show a cool toy or a simple one
                if (random.nextBoolean()) {
                    shopItems.add(poolTier2[random.nextInt(poolTier2.length)]);
                } else {
                    shopItems.add(poolTier1[random.nextInt(poolTier1.length)]);
                }
            }
        }
        return shopItems;
    }

    // This takes a picture of the player's backpack and saves it, so we remember it later
    public GameSnapshot saveSnapshot(Long userId, int round, String inventoryJson, String statsJson) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found!")); // Uh oh, who is this?

        GameSnapshot snapshot = new GameSnapshot();
        snapshot.setUser(user);
        snapshot.setRoundNumber(round);
        snapshot.setInventoryJson(inventoryJson);
        snapshot.setStatsJson(statsJson);
        
        return snapshotRepository.save(snapshot);
    }
    
    // This looks for someone else to play against who is at the same level
    public GameSnapshot findOpponent(int round) {
        return snapshotRepository.findRandomSnapshotByRound(round).orElse(null);
    }

    // --- Core Battle Logic ---
    // This is where the fighting happens! But don't worry, it's just math.
    public BattleResult processBattle(Long userId, int round) {
        BattleResult result = new BattleResult();
        List<String> logs = new ArrayList<>();
        
        try {
            // 1. Get player info
            // In a real game, we would look in the database.
            // But here we are just practicing.
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return result;
    }
    
    // --- The Real Fight Calculator ---
    // This takes your backpack and the bad guy's backpack and sees who wins
    public BattleResult calculateBattle(String playerInventoryJson, String opponentInventoryJson) {
        BattleResult result = new BattleResult();
        List<String> logs = new ArrayList<>();
        Random random = new Random();

        try {
            // 1. Open the backpacks (turn text into lists)
            List<String> playerPool = objectMapper.readValue(playerInventoryJson, new TypeReference<List<String>>(){});
            List<String> opponentPool;
            
            if (opponentInventoryJson == null || opponentInventoryJson.isEmpty()) {
                // If nobody is there to fight, we make a Robot with Swords
                opponentPool = new ArrayList<>();
                for(int i=0; i<5; i++) opponentPool.add("Sword");
                logs.add("No opponent found. Fighting a Robot...");
            } else {
                opponentPool = objectMapper.readValue(opponentInventoryJson, new TypeReference<List<String>>(){});
                logs.add("Opponent found!");
            }

            // 2. Spin the Slot Machine! (Pick 9 items randomly)
            List<String> playerGrid = drawGrid(playerPool, random);
            List<String> opponentGrid = drawGrid(opponentPool, random);
            
            result.setPlayerGrid(playerGrid);
            result.setOpponentGrid(opponentGrid);

            // 3. Count the points (Attack and Defense)
            int playerAtk = calculateAtk(playerGrid);
            int playerDef = calculateDef(playerGrid);
            int opponentAtk = calculateAtk(opponentGrid);
            int opponentDef = calculateDef(opponentGrid);

            result.setPlayerStats("ATK: " + playerAtk + " | DEF: " + playerDef);
            result.setOpponentStats("ATK: " + opponentAtk + " | DEF: " + opponentDef);

            // 4. Fight!
            // If bad guy hits harder than your shield, you get hurt.
            int playerDmgTaken = Math.max(0, opponentAtk - playerDef);
            int opponentDmgTaken = Math.max(0, playerAtk - opponentDef);
            
            logs.add("You dealt " + opponentDmgTaken + " damage!");
            logs.add("You took " + playerDmgTaken + " damage!");

            // 5. Who won?
            // If you hit them harder than they hit you, you win!
            if (opponentDmgTaken >= playerDmgTaken) {
                result.setPlayerWon(true);
                logs.add("Victory!");
            } else {
                result.setPlayerWon(false);
                logs.add("Defeat...");
            }
            
            result.setCombatLog(logs);

        } catch (Exception e) {
            e.printStackTrace();
            logs.add("Battle Error: " + e.getMessage());
            result.setCombatLog(logs);
        }
        
        return result;
    }

    // Helper: Pick 9 random items from the bag to fill the slots
    private List<String> drawGrid(List<String> pool, Random random) {
        List<String> grid = new ArrayList<>();
        if (pool.isEmpty()) return grid;
        
        for (int i = 0; i < 9; i++) {
            // Close your eyes and pick one!
            grid.add(pool.get(random.nextInt(pool.size())));
        }
        return grid;
    }

    // Helper: Count how much attack power we have
    private int calculateAtk(List<String> grid) {
        int atk = 0;
        for (String item : grid) {
            if ("Sword".equals(item)) atk += 5;
            if ("Dart".equals(item)) atk += 3;
            if ("VampireFang".equals(item)) atk += 4;
        }
        return atk;
    }

    // Helper: Count how much defense power we have
    private int calculateDef(List<String> grid) {
        int def = 0;
        for (String item : grid) {
            if ("Shield".equals(item)) def += 5;
            if ("Buckler".equals(item)) def += 3;
            if ("SpikedShield".equals(item)) def += 6;
        }
        return def;
    }
}
