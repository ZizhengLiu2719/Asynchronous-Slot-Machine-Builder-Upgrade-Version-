import { createContext, useContext, useState, type ReactNode } from 'react';

interface GameState {
  gold: number;
  lives: number;
  inventory: string[]; // 你的背包，最多20个
  addToInventory: (item: string) => boolean; // 买东西方法
  removeFromInventory: (index: number) => void; // 卖东西方法
  updateGold: (amount: number) => void;
  updateLives: (amount: number) => void;
}

const GameContext = createContext<GameState | undefined>(undefined);

export function GameProvider({ children }: { children: ReactNode }) {
  const [gold, setGold] = useState(10); // 初始 10 金币
  const [lives, setLives] = useState(3); // 初始 3 条命
  const [inventory, setInventory] = useState<string[]>([]); // 初始空背包

  const addToInventory = (item: string) => {
    if (inventory.length >= 20) {
      alert("背包满了！");
      return false;
    }
    if (gold < 3) {
      alert("金币不足！(每个 3 金币)");
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
    setGold(gold + 3); // 原价回收
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
