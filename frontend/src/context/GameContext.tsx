import { createContext, useContext, useState, type ReactNode } from 'react';

interface GameState {
  gold: number;
  lives: number;
  inventory: string[]; // Backpack (Inventory): Just like your school bag, holds up to 20 toys.
  addToInventory: (item: string) => boolean; // Buy: Put a toy in your bag.
  removeFromInventory: (index: number) => void; // Sell: Take a toy out of your bag.
  updateGold: (amount: number) => void;
  updateLives: (amount: number) => void;
}

const GameContext = createContext<GameState | undefined>(undefined);

export function GameProvider({ children }: { children: ReactNode }) {
  const [gold, setGold] = useState(10); // Start with 10 Gold Coins
  const [lives, setLives] = useState(3); // Start with 3 Hearts
  const [inventory, setInventory] = useState<string[]>([]); // Empty bag at the start

  const addToInventory = (item: string) => {
    if (inventory.length >= 20) {
      alert("Backpack full!");
      return false;
    }
    if (gold < 3) {
      alert("Not enough gold! (3 gold each)");
      return false;
    }
    setInventory([...inventory, item]);
    setGold(gold - 3);
    return true;
  };

  const removeFromInventory = (index: number) => {
    const newInv = [...inventory];
    newInv.splice(index, 1);
    setInventory(newInv);
    setGold(gold + 3); // Full refund!
  };

  return (
    <GameContext.Provider value={{
      gold, lives, inventory,
      addToInventory, removeFromInventory,
      updateGold: (amount) => setGold(g => g + amount),
      updateLives: (amount) => setLives(l => l + amount)
    }}>
      {children}
    </GameContext.Provider>
  );
}

// eslint-disable-next-line react-refresh/only-export-components
export function useGame() {
  const context = useContext(GameContext);
  if (!context) throw new Error("useGame must be used within a GameProvider");
  return context;
}
