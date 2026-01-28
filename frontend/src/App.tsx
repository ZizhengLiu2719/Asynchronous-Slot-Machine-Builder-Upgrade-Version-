import { BrowserRouter, Route, Routes } from 'react-router-dom';
import { GameProvider } from './context/GameContext'; // <--- 引入这个
import BattleScreen from './pages/BattleScreen';
import ShopScreen from './pages/ShopScreen';
import StartScreen from './pages/StartScreen';
import VictoryScreen from './pages/VictoryScreen';

function App() {
  return (
    <GameProvider>  {/* <--- 包裹起来 */}
      <BrowserRouter>
        <Routes>
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