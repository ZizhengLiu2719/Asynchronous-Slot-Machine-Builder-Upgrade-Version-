package com.example.slotmachine.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.slotmachine.model.GameSnapshot;

public interface GameSnapshotRepository extends JpaRepository<GameSnapshot, Long> {

    // 这是一个高级魔法！用来找对手。
    // nativeQuery = true 表示我们要直接用数据库的原生语言（SQL）来下命令。
    // 命令的意思是：从 game_snapshots 表里找，
    // 1. 轮次必须等于我们要找的 round (?1 代表第一个参数)
    // 2. 这里的 user_id 不能是我们可以排除的 (?2) (可选，防止匹配到自己，暂时可以先不管)
    // 3. ORDER BY RANDOM() 意思是：把找到的结果打乱顺序
    // 4. LIMIT 1 意思是：我只要 1 个
    @Query(value = "SELECT * FROM game_snapshots WHERE round_number = ?1 ORDER BY RANDOM() LIMIT 1", nativeQuery = true)
    Optional<GameSnapshot> findRandomSnapshotByRound(Integer roundNumber);
}
