import { BrowserRouter, Route, Routes } from 'react-router-dom';
import { GameProvider } from './context/GameContext'; // <--- Bring in the Game Box
import BattleScreen from './pages/BattleScreen';
import ShopScreen from './pages/ShopScreen';
import StartScreen from './pages/StartScreen';
import VictoryScreen from './pages/VictoryScreen';

function App() {
  return (
    // We wrap everything in the GameProvider so all pages can share the toys (data)
    <GameProvider>  
      <BrowserRouter>
        <Routes>
          {/* These are like maps. If you go to "/", you see the Start Screen. */}
          <Route path="/" element={<StartScreen />} />
          <Route path="/shop" element={<ShopScreen />} />
          <Route path="/battle" element={<BattleScreen />} />
          <Route path="/victory" element={<VictoryScreen />} />
        </Routes>
      </BrowserRouter>
    </GameProvider>
  );
}

export default App;
