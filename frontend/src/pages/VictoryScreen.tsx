
export default function VictoryScreen() {
    return (
        <div style={{ textAlign: 'center', marginTop: 100, color: 'gold' }}>
            <h1>🏆 恭喜通关！ 🏆</h1>
            <p>你是真正的老虎机之王！</p>
            <button onClick={() => window.location.href = '/'}>再玩一次</button>
        </div>
    );
}