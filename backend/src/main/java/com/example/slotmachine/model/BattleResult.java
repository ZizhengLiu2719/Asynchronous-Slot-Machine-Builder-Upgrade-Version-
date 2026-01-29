package com.example.slotmachine.model;

import java.util.List;

// This class is like a "Report Card" of the fight.
// It tells us everything that happened.
public class BattleResult {
    
    // What you had in your 9 slots (like ["Sword", "Shield", ...])
    private List<String> playerGrid;
    
    // What the bad guy had in their 9 slots
    private List<String> opponentGrid;
    
    // Your power numbers (like "ATK: 15, DEF: 5")
    private String playerStats;
    
    // The bad guy's power numbers
    private String opponentStats;
    
    // The story of the fight (like "You hit him!", "He hit you!")
    private List<String> combatLog;
    
    // Did you win? True means YES! False means Oh no...
    private boolean playerWon;

    // --- Helpers to get and set the data (Getters and Setters) ---
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
