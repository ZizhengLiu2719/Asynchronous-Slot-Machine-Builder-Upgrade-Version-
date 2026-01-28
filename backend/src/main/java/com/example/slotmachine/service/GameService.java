package com.example.slotmachine.service;

import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.slotmachine.model.GameSnapshot;
import com.example.slotmachine.model.User;
import com.example.slotmachine.repository.GameSnapshotRepository;
import com.example.slotmachine.repository.UserRepository;

@Service // 告诉 Spring：我是个干活的苦力，哪里需要我，就把我派过去
public class GameService {

    @Autowired // 自动要把刚才那两个管理员请过来帮忙
    private UserRepository userRepository;
    
    @Autowired
    private GameSnapshotRepository snapshotRepository;

    // --- 功能 1: 登录或注册 ---
    public User loginOrRegister(String username) {
        // 先去仓库里找找有没有叫这个名字的人
        return userRepository.findByUsername(username)
                .orElseGet(() -> {
                    // 如果没找到 (orElseGet)，那就新建一个
                    User newUser = new User();
                    newUser.setUsername(username);
                    return userRepository.save(newUser); // 让管理员保存进数据库
                });
    }

    // --- 功能 2: 商店刷新 (简单的随机逻辑) ---
    // 这里我们先用简单的字符串代替复杂的装备对象，后面再升级
    public List<String> refreshShop(int round) {
        // 假设我们要从这个池子里抽
        String[] poolTier1 = {"Sword", "Shield", "Cookie", "Dart", "Buckler"};
        String[] poolTier2 = {"VampireFang", "FrostStaff", "SpikedShield"};
        
        List<String> shopItems = new java.util.ArrayList<>();
        Random random = new Random();

        // 每次刷 5 个商品
        for (int i = 0; i < 5; i++) {
            if (round <= 3) {
                // 前3轮只出 Tier 1
                shopItems.add(poolTier1[random.nextInt(poolTier1.length)]);
            } else {
                // 后面混合出 (简单模拟：50%概率出 Tier 2)
                if (random.nextBoolean()) {
                    shopItems.add(poolTier2[random.nextInt(poolTier2.length)]);
                } else {
                    shopItems.add(poolTier1[random.nextInt(poolTier1.length)]);
                }
            }
        }
        return shopItems;
    }

    // --- 功能 3: 上传战斗快照 ---
    public GameSnapshot saveSnapshot(Long userId, int round, String inventoryJson, String statsJson) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found!")); // 找不到人就报错

        GameSnapshot snapshot = new GameSnapshot();
        snapshot.setUser(user);
        snapshot.setRoundNumber(round);
        snapshot.setInventoryJson(inventoryJson); // 比如 "['Sword', 'Shield', ...]"
        snapshot.setStatsJson(statsJson);         // 比如 "{atk: 10, def: 5}"
        
        return snapshotRepository.save(snapshot);
    }
    
    // --- 功能 4: 找对手 ---
    public GameSnapshot findOpponent(int round) {
        // 尝试找一个同轮次的随机对手
        return snapshotRepository.findRandomSnapshotByRound(round)
                .orElse(null); // 如果没找到（比如你是全服第一个玩到这一轮的），就返回空，后面我们再处理这种尴尬情况
    }
}
