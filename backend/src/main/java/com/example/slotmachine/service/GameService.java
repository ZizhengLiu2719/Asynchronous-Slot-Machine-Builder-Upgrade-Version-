package com.example.slotmachine.service;

import java.util.ArrayList; // <--- 引入新写的战报类
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.slotmachine.model.BattleResult;
import com.example.slotmachine.model.GameSnapshot;
import com.example.slotmachine.model.User;
import com.example.slotmachine.repository.GameSnapshotRepository;
import com.example.slotmachine.repository.UserRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class GameService {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private GameSnapshotRepository snapshotRepository;

    // 用来把 JSON 字符串变成 Java 列表的工具
    private final ObjectMapper objectMapper = new ObjectMapper();

    public User loginOrRegister(String username) {
        return userRepository.findByUsername(username)
                .orElseGet(() -> {
                    User newUser = new User();
                    newUser.setUsername(username);
                    return userRepository.save(newUser);
                });
    }

    public List<String> refreshShop(int round) {
        String[] poolTier1 = {"Sword", "Shield", "Cookie", "Dart", "Buckler"};
        String[] poolTier2 = {"VampireFang", "FrostStaff", "SpikedShield"};
        List<String> shopItems = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < 5; i++) {
            if (round <= 3) {
                shopItems.add(poolTier1[random.nextInt(poolTier1.length)]);
            } else {
                if (random.nextBoolean()) {
                    shopItems.add(poolTier2[random.nextInt(poolTier2.length)]);
                } else {
                    shopItems.add(poolTier1[random.nextInt(poolTier1.length)]);
                }
            }
        }
        return shopItems;
    }

    public GameSnapshot saveSnapshot(Long userId, int round, String inventoryJson, String statsJson) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found!"));

        GameSnapshot snapshot = new GameSnapshot();
        snapshot.setUser(user);
        snapshot.setRoundNumber(round);
        snapshot.setInventoryJson(inventoryJson);
        snapshot.setStatsJson(statsJson);
        
        return snapshotRepository.save(snapshot);
    }
    
    public GameSnapshot findOpponent(int round) {
        return snapshotRepository.findRandomSnapshotByRound(round).orElse(null);
    }

    // --- 新增：核心战斗逻辑 ---
    public BattleResult processBattle(Long userId, int round) {
        BattleResult result = new BattleResult();
        List<String> logs = new ArrayList<>();
        
        try {
            // 1. 获取玩家数据
            // 这里简化处理：直接假设玩家刚刚上传了快照，我们取最新的那个
            // 真实项目中应该从前端传过来的 inventory 计算，或者查数据库
            // 为了演示方便，我们假设前端传过来的 inventoryJson 已经被存到最新的 snapshot 里了
            // 这里我们偷个懒，直接让前端把 inventory 发过来会更好，但为了不改 Controller 接口，
            // 我们假设玩家的库存就是刚刚存进去的那个。
            
            // 更好的做法：直接让 Controller 把 inventory 传进来。
            // 我们先用假数据测试一下流程
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return result;
    }
    
    // --- 真正的战斗计算方法 ---
    // 这个方法接收玩家的库存(JSON)和对手的库存(JSON)，算出结果
    public BattleResult calculateBattle(String playerInventoryJson, String opponentInventoryJson) {
        BattleResult result = new BattleResult();
        List<String> logs = new ArrayList<>();
        Random random = new Random();

        try {
            // 1. 解析库存
            List<String> playerPool = objectMapper.readValue(playerInventoryJson, new TypeReference<List<String>>(){});
            List<String> opponentPool;
            
            if (opponentInventoryJson == null || opponentInventoryJson.isEmpty()) {
                // 如果没有对手，就生成一个全是 "Rusty Sword" 的机器人
                opponentPool = new ArrayList<>();
                for(int i=0; i<5; i++) opponentPool.add("Sword");
                logs.add("没有找到对手，正在与机器人对战...");
            } else {
                opponentPool = objectMapper.readValue(opponentInventoryJson, new TypeReference<List<String>>(){});
                logs.add("匹配到对手！");
            }

            // 2. 老虎机抽奖！(从库存里随机抽 9 个)
            List<String> playerGrid = drawGrid(playerPool, random);
            List<String> opponentGrid = drawGrid(opponentPool, random);
            
            result.setPlayerGrid(playerGrid);
            result.setOpponentGrid(opponentGrid);

            // 3. 计算属性 (这里是简化的数值逻辑)
            int playerAtk = calculateAtk(playerGrid);
            int playerDef = calculateDef(playerGrid);
            int opponentAtk = calculateAtk(opponentGrid);
            int opponentDef = calculateDef(opponentGrid);

            result.setPlayerStats("ATK: " + playerAtk + " | DEF: " + playerDef);
            result.setOpponentStats("ATK: " + opponentAtk + " | DEF: " + opponentDef);

            // 4. 互殴
            int playerDmgTaken = Math.max(0, opponentAtk - playerDef);
            int opponentDmgTaken = Math.max(0, playerAtk - opponentDef);
            
            logs.add("你造成了 " + opponentDmgTaken + " 点伤害！");
            logs.add("你受到了 " + playerDmgTaken + " 点伤害！");

            // 5. 判定胜负 (伤害高者胜，或者受伤害少者胜)
            // 简单规则：谁造成的净伤害高谁赢
            if (opponentDmgTaken >= playerDmgTaken) {
                result.setPlayerWon(true);
                logs.add("战斗胜利！");
            } else {
                result.setPlayerWon(false);
                logs.add("战斗失败...");
            }
            
            result.setCombatLog(logs);

        } catch (Exception e) {
            e.printStackTrace();
            logs.add("战斗计算出错: " + e.getMessage());
            result.setCombatLog(logs);
        }
        
        return result;
    }

    // 辅助方法：从池子里随机抽 9 个填满格子
    private List<String> drawGrid(List<String> pool, Random random) {
        List<String> grid = new ArrayList<>();
        if (pool.isEmpty()) return grid;
        
        for (int i = 0; i < 9; i++) {
            // 随机抽一个
            grid.add(pool.get(random.nextInt(pool.size())));
        }
        return grid;
    }

    // 辅助方法：计算攻击力
    private int calculateAtk(List<String> grid) {
        int atk = 0;
        for (String item : grid) {
            if ("Sword".equals(item)) atk += 5;
            if ("Dart".equals(item)) atk += 3;
            if ("VampireFang".equals(item)) atk += 4;
        }
        return atk;
    }

    // 辅助方法：计算防御力
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