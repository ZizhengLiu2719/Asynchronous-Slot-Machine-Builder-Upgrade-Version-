import { BrowserRouter, Route, Routes } from 'react-router-dom';
import BattleScreen from './pages/BattleScreen';
import ShopScreen from './pages/ShopScreen';
import StartScreen from './pages/StartScreen';
import VictoryScreen from './pages/VictoryScreen';

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<StartScreen />} />
        <Route path="/shop" element={<ShopScreen />} />
        <Route path="/battle" element={<BattleScreen />} />
        <Route path="/victory" element={<VictoryScreen />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;