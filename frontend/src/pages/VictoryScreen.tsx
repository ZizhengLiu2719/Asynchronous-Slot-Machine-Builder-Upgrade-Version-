
export default function VictoryScreen() {
    return (
        <div style={{ textAlign: 'center', marginTop: 100, color: 'gold' }}>
            <h1>🏆 CONGRATULATIONS! 🏆</h1>
            <p>You are the Slot Machine King!</p>
            <button onClick={() => window.location.href = '/'}>PLAY AGAIN</button>
        </div>
    );
}
