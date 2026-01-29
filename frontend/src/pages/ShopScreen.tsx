import axios from 'axios';
import { useCallback, useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useGame } from '../context/GameContext';

export default function ShopScreen() {
    const navigate = useNavigate();
    // Get tools from our game box
    const { gold, lives, inventory, addToInventory, removeFromInventory } = useGame();
    const [shopItems, setShopItems] = useState<string[]>([]);
    const round = localStorage.getItem('round') || '1';

    // Ask the shopkeeper: "What do you have today?"
    const fetchShop = useCallback(() => {
        axios.get(`http://localhost:8080/api/shop?round=${round}`)
            .then(res => setShopItems(res.data))
            .catch(err => console.error(err));
    }, [round]);

    // When we walk into the shop, look at the items
    useEffect(() => {
        fetchShop();
    }, [fetchShop]);

    // Ready to fight!
    const handleStartBattle = async () => {
        const userId = localStorage.getItem('userId');
        try {
            // Save our bag before leaving
            await axios.post('http://localhost:8080/api/snapshot', {
                userId: userId, round: round,
                inventory: JSON.stringify(inventory), stats: "{}"
            });
            navigate('/battle');
        } catch (e) {
            console.error(e);
            alert("Failed to save game!");
        }
    };

    return (
        <div style={{ maxWidth: 800, margin: '0 auto', textAlign: 'left' }}>
            {/* Top Bar: Shows Round, Money, and Hearts */}
            <header className="card" style={{
                display: 'flex', justifyContent: 'space-between', alignItems: 'center',
                marginBottom: 30, background: '#222', borderColor: 'gold'
            }}>
                <h2 style={{ margin: 0, color: 'white' }}>🛒 Round <span style={{ color: 'gold', fontSize: '1.5em' }}>{round}</span></h2>
                <div style={{ fontSize: '1.2em' }}>
                    <span style={{ marginRight: 20, color: 'gold' }}>💰 {gold}</span>
                    <span style={{ color: '#ff4444' }}>❤️ {lives}</span>
                </div>
            </header>

            {/* Shop Area: Buy things here */}
            <div className="card" style={{ marginBottom: 30 }}>
                <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: 15 }}>
                    <h3 style={{ margin: 0 }}>🎁 SHOP (3 Gold)</h3>
                    <button onClick={fetchShop} style={{ fontSize: '0.8em', padding: '5px 10px' }}>🎲 REROLL (1G)</button>
                </div>
                <div style={{ display: 'flex', gap: 15, flexWrap: 'wrap' }}>
                    {shopItems.map((item, index) => (
                        <button key={index} onClick={() => addToInventory(item)} style={{
                            width: 100, height: 100, display: 'flex', flexDirection: 'column',
                            alignItems: 'center', justifyContent: 'center', gap: 5,
                            border: '2px solid #666', background: '#333'
                        }}>
                            <div style={{ fontSize: 30 }}>{getItemIcon(item)}</div>
                            <div style={{ fontSize: 12 }}>{item}</div>
                        </button>
                    ))}
                </div>
            </div>

            {/* Backpack Area: Sell things here */}
            <div className="card" style={{ marginBottom: 30, background: '#1a1a1a' }}>
                <h3 style={{ marginTop: 0 }}>🎒 BACKPACK ({inventory.length}/20)</h3>
                <div style={{
                    display: 'grid', gridTemplateColumns: 'repeat(10, 1fr)', gap: 8
                }}>
                    {inventory.map((item, index) => (
                        <div key={index} onClick={() => removeFromInventory(index)} style={{
                            aspectRatio: '1/1', border: '2px solid #4caf50',
                            display: 'flex', alignItems: 'center', justifyContent: 'center',
                            cursor: 'pointer', background: '#2e3b2e', fontSize: 20,
                            position: 'relative'
                        }} title="Click to Sell">
                            {getItemIcon(item)}
                        </div>
                    ))}
                    {/* Empty Slots */}
                    {[...Array(20 - inventory.length)].map((_, i) => (
                        <div key={`empty-${i}`} style={{
                            aspectRatio: '1/1', border: '2px dashed #444',
                            background: '#222'
                        }} />
                    ))}
                </div>
                <p style={{ fontSize: '0.8em', color: '#666', marginTop: 10 }}>* Click green items to SELL</p>
            </div>

            <button onClick={handleStartBattle} style={{
                width: '100%', padding: 20, fontSize: 24,
                background: '#ff4444', borderColor: '#ff0000', textShadow: '1px 1px black'
            }}>
                ⚔️ F I G H T !
            </button>
        </div>
    );
}

// Helper: Turns names into Emoji Pictures
function getItemIcon(name: string) {
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
