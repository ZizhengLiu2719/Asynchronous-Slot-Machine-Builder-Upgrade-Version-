import axios from 'axios';
import { useEffect, useRef, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useGame } from '../context/GameContext';

// This is the Report Card we get from the server
interface BattleResult {
    playerGrid: string[]; opponentGrid: string[];
    playerStats: string; opponentStats: string;
    combatLog: string[]; playerWon: boolean;
    playerName: string; opponentName: string;
}

export default function BattleScreen() {
    const navigate = useNavigate();
    const { inventory, updateGold, updateLives, lives } = useGame();
    const [loading, setLoading] = useState(true);
    const [result, setResult] = useState<BattleResult | null>(null);
    const [displayGrid, setDisplayGrid] = useState<string[]>(Array(9).fill("❓")); // Mystery boxes
    const round = localStorage.getItem('round') || '1';
    const username = localStorage.getItem('username') || 'Hero';
    const hasStarted = useRef(false);

    useEffect(() => {
        if (result) {
            console.log("Result updated:", result.playerName, result.opponentName);
        }
    }, [result]);
    
    useEffect(() => {
        // Only start the fight once!
        if (hasStarted.current) return;
        hasStarted.current = true;

        const startBattle = async () => {
            try {
                // Send our backpack to the server and ask: "Did I win?"
                const response = await axios.post('http://localhost:8080/api/battle', {
                    inventory: JSON.stringify(inventory), 
                    round: round,
                    username: username
                });
                const battleResult: BattleResult = response.data;
                setResult(battleResult);

                // Make the slot machine effect (rolling animation)
                let ticks = 0;
                const interval = setInterval(() => {
                    const randomGrid = Array(9).fill("").map(() => getItemIcon("Random"));
                    setDisplayGrid(randomGrid);
                    ticks++;
                    // Stop rolling after a little while
                    if (ticks > 25) {
                        clearInterval(interval);
                        // Show the real items
                        setDisplayGrid(battleResult.playerGrid.map(getItemIcon));
                        setLoading(false);
                        // Give gold or take life
                        if (battleResult.playerWon) updateGold(10);
                        else { updateGold(10); updateLives(-1); }
                    }
                }, 80);
            } catch (e) { 
                console.error(e); 
                alert("Error connecting to server"); 
                navigate('/shop'); 
            }
        };
        startBattle();
    }, [inventory, navigate, round, updateGold, updateLives]);

    // Go to next round or Game Over
    const handleNext = () => {
        if (!result?.playerWon && lives - 1 <= 0) {
            alert("GAME OVER"); window.location.href = '/'; return;
        }
        const currentRound = parseInt(round);
        if (result?.playerWon && currentRound >= 10) navigate('/victory');
        else {
            localStorage.setItem('round', (currentRound + 1).toString());
            navigate('/shop');
        }
    };

    return (
        <div style={{ maxWidth: 800, margin: '0 auto' }}>
            <h1 style={{ color: '#ff4444', marginBottom: 40 }}>⚔️ BATTLE START ⚔️</h1>

            <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'flex-start', gap: 20 }}>
                {/* Your Board */}
                <div className="card" style={{ width: 200, borderColor: '#4a90e2' }}>
                    <h3 style={{ color: '#4a90e2' }}>{result?.playerName || username}</h3>
                    <div style={{
                        display: 'grid', gridTemplateColumns: 'repeat(3, 1fr)', gap: 5,
                        background: '#000', padding: 5, border: '2px solid #333'
                    }}>
                        {displayGrid.map((item, i) => (
                            <div key={i} style={{
                                height: 50, display: 'flex', alignItems: 'center', justifyContent: 'center',
                                background: '#222', fontSize: 24
                            }}>{item}</div>
                        ))}
                    </div>
                    <div style={{ marginTop: 10, fontSize: '0.8em', minHeight: 40 }}>
                        {loading ? "ROLLING..." : result?.playerStats}
                    </div>
                </div>

                <div style={{ alignSelf: 'center', fontSize: 40, color: '#ff4444', fontWeight: 900 }}>VS</div>

                {/* Enemy Board */}
                <div className="card" style={{ width: 200, borderColor: '#ff4444' }}>
                    <h3 style={{ color: '#ff4444' }}>{result?.opponentName || "ENEMY"}</h3>
                    <div style={{
                        display: 'grid', gridTemplateColumns: 'repeat(3, 1fr)', gap: 5,
                        background: '#000', padding: 5, border: '2px solid #333',
                        filter: loading ? 'blur(4px)' : 'none', transition: 'filter 0.5s'
                    }}>
                        {(loading ? Array(9).fill("❓") : (result?.opponentGrid || []).map(getItemIcon)).map((item, i) => (
                            <div key={i} style={{
                                height: 50, display: 'flex', alignItems: 'center', justifyContent: 'center',
                                background: '#222', fontSize: 24
                            }}>{item}</div>
                        ))}
                    </div>
                    <div style={{ marginTop: 10, fontSize: '0.8em', minHeight: 40 }}>
                        {loading ? "???" : result?.opponentStats}
                    </div>
                </div>
            </div>

            {/* Results Board */}
            {!loading && result && (
                <div className="card" style={{ marginTop: 40, borderColor: result.playerWon ? 'gold' : 'gray', animation: 'popIn 0.5s' }}>
                    <h2 style={{ color: result.playerWon ? 'gold' : 'gray' }}>
                        {result.playerWon ? "🏆 VICTORY!" : "💀 DEFEAT..."}
                    </h2>
                    <div style={{ textAlign: 'left', background: '#000', padding: 10, fontFamily: 'monospace', fontSize: '0.9em', color: '#ccc' }}>
                        {result.combatLog.map((log, i) => <div key={i}>&gt; {log}</div>)}
                    </div>
                    <button onClick={handleNext} style={{ marginTop: 20, width: '100%' }}>
                        CONTINUE &gt;&gt;
                    </button>
                </div>
            )}
        </div>
    );
}

// Helper: Turns names into Pictures
function getItemIcon(name: string) {
    if (name.includes("Random")) return ["⚔️", "🛡️", "🍌", "🏹"][Math.floor(Math.random() * 4)];

    // Weapons
    if (name.includes("Sword")) return "⚔️";
    if (name.includes("Dagger")) return "🗡️";
    if (name.includes("Crossbow")) return "🏹";
    
    // Shields/Defense
    if (name.includes("Shield")) return "🛡️";
    if (name.includes("Buckler")) return "🌰";
    
    // Food/Nature
    if (name.includes("Cookie")) return "🍪";
    if (name.includes("Banana")) return "🍌";
    if (name.includes("Mushroom")) return "🍄";
    if (name.includes("Rock")) return "🪨";
    
    // Magic/Accessories
    if (name.includes("Wand")) return "🪄";
    if (name.includes("Ring")) return "💍";
    if (name.includes("Fang")) return "🧛";
    
    return "📦";
}
