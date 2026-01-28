import axios from 'axios';
import { useState } from 'react';
import { useNavigate } from 'react-router-dom';

export default function StartScreen() {
    const [username, setUsername] = useState('');
    const navigate = useNavigate();

    const handleStart = async () => {
        if (!username) return alert('请输入名字！');
        try {
            const response = await axios.post(`http://localhost:8080/api/login?username=${username}`);
            localStorage.setItem('userId', response.data.id);
            localStorage.setItem('username', response.data.username);
            localStorage.setItem('round', '1');
            navigate('/shop');
        } catch (error) {
            console.error(error); // 打印错误，解决 linter 报错
            alert('连接后端失败！');
        }
    };

    return (
        <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', gap: 30 }}>
            <h1 style={{ fontSize: '3rem', color: 'gold', marginBottom: 0 }}>🎰 Slot Battler</h1>
            <p style={{ color: '#aaa', marginTop: -20 }}>--- 异步 PVP 像素地牢 ---</p>

            <div className="card" style={{ padding: 40, width: 400 }}>
                <h3 style={{ marginBottom: 20 }}>请输入勇者姓名</h3>
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
                    开始冒险 START
                </button>
            </div>
        </div>
    );
}
