import axios from 'axios';
import { useCallback, useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useGame } from '../context/GameContext';

export default function ShopScreen() {
    const navigate = useNavigate();
    const { gold, lives, inventory, addToInventory, removeFromInventory } = useGame();
    const [shopItems, setShopItems] = useState<string[]>([]);
    const round = localStorage.getItem('round') || '1';

    const fetchShop = useCallback(() => {
        axios.get(`http://localhost:8080/api/shop?round=${round}`)
            .then(res => setShopItems(res.data))
            .catch(err => console.error(err));
    }, [round]);

    useEffect(() => {
        fetchShop();
    }, [fetchShop]);

    const handleStartBattle = async () => {
        const userId = localStorage.getItem('userId');
        try {
            await axios.post('http://localhost:8080/api/snapshot', {
                userId: userId, round: round,
                inventory: JSON.stringify(inventory), stats: "{}"
            });
            navigate('/battle');
        } catch (e) {
            console.error(e);
            alert("上传快照失败！");
        }
    };

    return (
        <div style={{ maxWidth: 800, margin: '0 auto', textAlign: 'left' }}>
            {/* 顶部状态栏 */}
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

            {/* 商店区域 */}
            <div className="card" style={{ marginBottom: 30 }}>
                <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: 15 }}>
                    <h3 style={{ margin: 0 }}>🎁 商店 (3金币)</h3>
                    <button onClick={fetchShop} style={{ fontSize: '0.8em', padding: '5px 10px' }}>🎲 刷新 (1G)</button>
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

            {/* 库存区域 */}
            <div className="card" style={{ marginBottom: 30, background: '#1a1a1a' }}>
                <h3 style={{ marginTop: 0 }}>🎒 背包 ({inventory.length}/20)</h3>
                <div style={{
                    display: 'grid', gridTemplateColumns: 'repeat(10, 1fr)', gap: 8
                }}>
                    {inventory.map((item, index) => (
                        <div key={index} onClick={() => removeFromInventory(index)} style={{
                            aspectRatio: '1/1', border: '2px solid #4caf50',
                            display: 'flex', alignItems: 'center', justifyContent: 'center',
                            cursor: 'pointer', background: '#2e3b2e', fontSize: 20,
                            position: 'relative'
                        }} title="点击卖出">
                            {getItemIcon(item)}
                        </div>
                    ))}
                    {/* 空格子 */}
                    {[...Array(20 - inventory.length)].map((_, i) => (
                        <div key={`empty-${i}`} style={{
                            aspectRatio: '1/1', border: '2px dashed #444',
                            background: '#222'
                        }} />
                    ))}
                </div>
                <p style={{ fontSize: '0.8em', color: '#666', marginTop: 10 }}>* 点击绿色格子卖出装备</p>
            </div>

            <button onClick={handleStartBattle} style={{
                width: '100%', padding: 20, fontSize: 24,
                background: '#ff4444', borderColor: '#ff0000', textShadow: '1px 1px black'
            }}>
                ⚔️ 进 入 战 斗
            </button>
        </div>
    );
}

// 辅助函数：把文字转成 Emoji 图标
function getItemIcon(name: string) {
    if (name.includes("Sword")) return "⚔️";
    if (name.includes("Shield")) return "🛡️";
    if (name.includes("Cookie")) return "🍪";
    if (name.includes("Dart")) return "🎯";
    if (name.includes("Buckler")) return "🌰";
    if (name.includes("Fang")) return "🧛";
    return "📦";
}