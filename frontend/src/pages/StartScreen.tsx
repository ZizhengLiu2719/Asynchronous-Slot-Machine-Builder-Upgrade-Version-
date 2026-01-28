import axios from 'axios';
import { useState } from 'react';
import { useNavigate } from 'react-router-dom';

export default function StartScreen() {
    const [username, setUsername] = useState('');
    const navigate = useNavigate(); // 这是一个导航员，帮我们跳到别的页面

    const handleStart = async () => {
        if (!username) return alert('请输入名字！');

        try {
            // 给后端打电话：喂，我是 xxx，我要登录！
            const response = await axios.post(`http://localhost:8080/api/login?username=${username}`);
            console.log('登录成功:', response.data);

            // 把用户ID存到浏览器里（localStorage），这样刷新页面也不会忘
            localStorage.setItem('userId', response.data.id);
            localStorage.setItem('username', response.data.username);
            localStorage.setItem('round', '1'); // 初始是第1轮

            // 跳到商店页
            navigate('/shop');
        } catch (error) {
            alert('连接后端失败，请确认后端已启动！');
            console.error(error);
        }
    };

    return (
        <div style={{ padding: 50, textAlign: 'center' }}>
            <h1>🎰 异步老虎机大乱斗</h1>
            <input
                type="text"
                placeholder="输入你的名字"
                value={username}
                onChange={e => setUsername(e.target.value)}
                style={{ padding: 10, fontSize: 20 }}
            />
            <br /><br />
            <button onClick={handleStart} style={{ padding: 10, fontSize: 20 }}>
                开始游戏
            </button>
        </div>
    );
}