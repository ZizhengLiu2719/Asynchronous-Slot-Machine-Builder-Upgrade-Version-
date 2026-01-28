import { useNavigate } from 'react-router-dom';

export default function BattleScreen() {
    const navigate = useNavigate();

    const handleFight = () => {
        // 假装战斗了 2 秒
        setTimeout(() => {
            alert('战斗胜利！+10 金币');

            // 轮次 +1
            const currentRound = parseInt(localStorage.getItem('round') || '1');
            if (currentRound >= 10) {
                navigate('/victory');
            } else {
                localStorage.setItem('round', (currentRound + 1).toString());
                navigate('/shop');
            }
        }, 1000);
    };

    return (
        <div style={{ padding: 50, textAlign: 'center' }}>
            <h1>⚔️ 战斗中...</h1>
            <p>正在匹配对手...</p>
            <button onClick={handleFight}>点击开始战斗 (模拟)</button>
        </div>
    );
}