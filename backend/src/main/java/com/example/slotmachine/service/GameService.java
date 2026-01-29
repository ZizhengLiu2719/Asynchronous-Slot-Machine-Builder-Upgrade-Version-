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

    // --- Item System Definition ---
    private static class ItemDef {
        String name;
        String type; // WEAPON, SHIELD, MAGIC, FOOD, NATURE, ACCESSORY
        int baseAtk;
        int baseDef;
        int tier;
        String description;

        public ItemDef(String name, String type, int baseAtk, int baseDef, int tier, String description) {
            this.name = name;
            this.type = type;
            this.baseAtk = baseAtk;
            this.baseDef = baseDef;
            this.tier = tier;
            this.description = description;
        }
    }

    private static final List<ItemDef> ALL_ITEMS = new ArrayList<>();
    
    static {
        // Tier 1 (Basic)
        ALL_ITEMS.add(new ItemDef("Dagger", "WEAPON", 3, 0, 1, "Small but sharp."));
        ALL_ITEMS.add(new ItemDef("WoodenShield", "SHIELD", 0, 3, 1, "Basic protection."));
        ALL_ITEMS.add(new ItemDef("Banana", "FOOD", 0, 2, 1, "Healthy snack. (+2 HP)"));
        ALL_ITEMS.add(new ItemDef("Rock", "NATURE", 1, 1, 1, "Solid reliability."));

        // Tier 2 (Advanced - Synergies)
        ALL_ITEMS.add(new ItemDef("IronSword", "WEAPON", 5, 0, 2, "Standard soldier gear."));
        ALL_ITEMS.add(new ItemDef("KiteShield", "SHIELD", 0, 5, 2, "Good coverage."));
        ALL_ITEMS.add(new ItemDef("Crossbow", "WEAPON", 4, 0, 2, "Crit chance? +2 ATK if you have another Weapon."));
        ALL_ITEMS.add(new ItemDef("Mushroom", "FOOD", 0, 3, 2, "Magic fungus. +1 DEF for every Food."));

        // Tier 3 (Epic - High Scaling)
        ALL_ITEMS.add(new ItemDef("GoldenSword", "WEAPON", 8, 0, 3, "Shiny and deadly."));
        ALL_ITEMS.add(new ItemDef("PlatinumShield", "SHIELD", 0, 8, 3, "Unbreakable."));
        ALL_ITEMS.add(new ItemDef("MagicWand", "MAGIC", 3, 0, 3, "Arcane power. +3 ATK for every Magic item."));
        ALL_ITEMS.add(new ItemDef("VampireRing", "ACCESSORY", 0, 0, 3, "Life steal. +1 Heal (Def) per Weapon."));
    }

    private ItemDef getItem(String name) {
        return ALL_ITEMS.stream()
                .filter(i -> i.name.equals(name))
                .findFirst()
                .orElse(new ItemDef(name, "UNKNOWN", 1, 1, 1, "Mystery Item"));
    }

    // This fills the shop with toys based on how many rounds we played
    public List<String> refreshShop(int round) {
        List<String> shopItems = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < 5; i++) {
            // Determine Tier based on round
            int maxTier = 1;
            if (round >= 4) maxTier = 2;
            if (round >= 7) maxTier = 3;

            // Roll for tier
            int rolledTier = 1;
            int roll = random.nextInt(100);
            
            if (maxTier == 3) {
                if (roll < 20) rolledTier = 3; // 20% Epic
                else if (roll < 60) rolledTier = 2; // 40% Rare
                else rolledTier = 1; // 40% Common
            } else if (maxTier == 2) {
                if (roll < 40) rolledTier = 2; // 40% Rare
                else rolledTier = 1; // 60% Common
            }

            final int finalTier = rolledTier;
            List<ItemDef> pool = ALL_ITEMS.stream().filter(item -> item.tier == finalTier).toList();
            
            if (!pool.isEmpty()) {
                shopItems.add(pool.get(random.nextInt(pool.size())).name);
            } else {
                shopItems.add("Rock"); // Fallback
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
                // If nobody is there to fight, we make a Training Dummy with Daggers
                opponentPool = new ArrayList<>();
                for(int i=0; i<5; i++) opponentPool.add("Dagger");
                logs.add("No opponent found. Fighting a Training Dummy...");
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
            int[] playerStats = calculateGridStats(playerGrid);
            int[] opponentStats = calculateGridStats(opponentGrid);

            int playerAtk = playerStats[0];
            int playerDef = playerStats[1];
            int opponentAtk = opponentStats[0];
            int opponentDef = opponentStats[1];

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

    // New Helper: Calculate stats with Synergies!
    private int[] calculateGridStats(List<String> grid) {
        int atk = 0;
        int def = 0;
        
        List<ItemDef> items = new ArrayList<>();
        for(String name : grid) items.add(getItem(name));

        // First pass: Base stats
        for(ItemDef item : items) {
            atk += item.baseAtk;
            def += item.baseDef;
        }

        // Second pass: Synergies
        for(ItemDef item : items) {
            if ("Crossbow".equals(item.name)) {
                // +2 ATK if another Weapon exists
                long weaponCount = items.stream().filter(i -> "WEAPON".equals(i.type)).count();
                if (weaponCount > 1) atk += 2;
            }
            if ("Mushroom".equals(item.name)) {
                // +1 DEF per Food (including itself)
                long foodCount = items.stream().filter(i -> "FOOD".equals(i.type)).count();
                def += (int) foodCount;
            }
            if ("MagicWand".equals(item.name)) {
                // +3 ATK per Magic item
                long magicCount = items.stream().filter(i -> "MAGIC".equals(i.type)).count();
                atk += (int) magicCount * 3;
            }
            if ("VampireRing".equals(item.name)) {
                // +1 DEF (Heal) per Weapon
                long weaponCount = items.stream().filter(i -> "WEAPON".equals(i.type)).count();
                def += (int) weaponCount;
            }
        }
        
        return new int[]{atk, def};
    }
}
