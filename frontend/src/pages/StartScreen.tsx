import axios from 'axios';
import { useState } from 'react';
import { useNavigate } from 'react-router-dom';

export default function StartScreen() {
    const [username, setUsername] = useState('');
    const navigate = useNavigate();

    // When you click the BIG BUTTON
    const handleStart = async () => {
        if (!username) return alert('Please enter your name!');
        try {
            // Knock knock on the server's door
            const response = await axios.post(`http://localhost:8080/api/login?username=${username}`);
            
            // Save your ticket so we remember who you are
            localStorage.setItem('userId', response.data.id);
            localStorage.setItem('username', response.data.username);
            localStorage.setItem('round', '1');
            
            // Go to the Shop!
            navigate('/shop');
        } catch (error) {
            console.error(error); // Oops, something broke
            alert('Failed to connect to server!');
        }
    };

    return (
        <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', gap: 30 }}>
            <h1 style={{ fontSize: '3rem', color: 'gold', marginBottom: 0 }}>🎰 Slot Battler</h1>
            <p style={{ color: '#aaa', marginTop: -20 }}>--- Async PVP Pixel Dungeon ---</p>

            <div className="card" style={{ padding: 40, width: 400 }}>
                <h3 style={{ marginBottom: 20 }}>Enter Hero Name</h3>
                <input
                    type="text"
                    placeholder="Hero Name"
                    value={username}
                    onChange={e => setUsername(e.target.value)}
                    style={{
                        padding: 15, fontSize: 18, width: '100%',
                        background: '#111', color: 'white', border: '2px solid #555',
                        marginBottom: 20, textAlign: 'center', fontFamily: 'inherit'
                    }}
                />
                <button onClick={handleStart} style={{ width: '100%', fontSize: 20, borderColor: 'gold', color: 'gold' }}>
                    START ADVENTURE
                </button>
            </div>
        </div>
    );
}
