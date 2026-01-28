import axios from 'axios';
import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';

export default function ShopScreen() {
    const navigate = useNavigate();
    const [shopItems, setShopItems] = useState<string[]>([]);
    const round = localStorage.getItem('round') || '1';

    // 页面一加载，就去后端拉取商品列表
    useEffect(() => {
        axios.get(`http://localhost:8080/api/shop?round=${round}`)
            .then(res => setShopItems(res.data))
            .catch(err => console.error(err));
    }, []);

    return (
        <div style={{ padding: 20 }}>
            <h2>🛒 商店 (Round {round})</h2>
            <div style={{ display: 'flex', gap: 10 }}>
                {shopItems.map((item, index) => (
                    <div key={index} style={{ border: '1px solid black', padding: 20 }}>
                        {item}
                    </div>
                ))}
            </div>
            <br />
            <button onClick={() => navigate('/battle')} style={{ padding: 10, background: 'red', color: 'white' }}>
                ⚔️ 去战斗
            </button>
        </div>
    );
}