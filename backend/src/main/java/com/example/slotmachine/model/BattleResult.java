package com.example.slotmachine.model;

import java.util.List;

// 这个类就像一份“战报”，详细记录了战斗发生的一切
public class BattleResult {
    
    // 你的 9 个格子抽到了什么 (比如 ["Sword", "Shield", ...])
    private List<String> playerGrid;
    
    // 对手的 9 个格子抽到了什么
    private List<String> opponentGrid;
    
    // 你的总属性 (比如 "攻击: 15, 防御: 5")
    private String playerStats;
    
    // 对手的总属性
    private String opponentStats;
    
    // 战斗日志 (比如 "你造成了 10 点伤害", "你赢了")
    private List<String> combatLog;
    
    // 最终结果: true 代表你赢了，false 代表你输了
    private boolean playerWon;

    // --- 下面是手写的 Getters 和 Setters ---
    public List<String> getPlayerGrid() { return playerGrid; }
    public void setPlayerGrid(List<String> playerGrid) { this.playerGrid = playerGrid; }
    
    public List<String> getOpponentGrid() { return opponentGrid; }
    public void setOpponentGrid(List<String> opponentGrid) { this.opponentGrid = opponentGrid; }
    
    public String getPlayerStats() { return playerStats; }
    public void setPlayerStats(String playerStats) { this.playerStats = playerStats; }
    
    public String getOpponentStats() { return opponentStats; }
    public void setOpponentStats(String opponentStats) { this.opponentStats = opponentStats; }
    
    public List<String> getCombatLog() { return combatLog; }
    public void setCombatLog(List<String> combatLog) { this.combatLog = combatLog; }
    
    public boolean isPlayerWon() { return playerWon; }
    public void setPlayerWon(boolean playerWon) { this.playerWon = playerWon; }
}
