package com.example.slotmachine.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.slotmachine.model.GameSnapshot;
import com.example.slotmachine.model.User;
import com.example.slotmachine.service.GameService;

@RestController // 告诉 Spring：我是个接待员，专门处理 HTTP 请求
@RequestMapping("/api") // 所有找我的请求，都要以 /api 开头
@CrossOrigin(origins = "*") // 允许任何人（比如我们的 React 前端）跨域访问我
public class GameController {

    @Autowired
    private GameService gameService;

    // 1. 登录接口
    // 请求方式: POST /api/login?username=xxx
    @PostMapping("/login")
    public User login(@RequestParam String username) {
        return gameService.loginOrRegister(username);
    }

    // 2. 商店刷新接口
    // 请求方式: GET /api/shop?round=1
    @GetMapping("/shop")
    public List<String> getShopItems(@RequestParam int round) {
        return gameService.refreshShop(round);
    }

    // 3. 上传快照接口
    // 请求方式: POST /api/snapshot (身体里带 JSON 数据)
    @PostMapping("/snapshot")
    public GameSnapshot uploadSnapshot(@RequestBody Map<String, Object> payload) {
        // 从 JSON 包裹里把数据拆出来
        Long userId = Long.valueOf(payload.get("userId").toString());
        int round = Integer.parseInt(payload.get("round").toString());
        String inventory = payload.get("inventory").toString();
        String stats = payload.get("stats").toString();

        return gameService.saveSnapshot(userId, round, inventory, stats);
    }
    
    // 4. 匹配对手接口
    // 请求方式: GET /api/opponent?round=1
    @GetMapping("/opponent")
    public GameSnapshot getOpponent(@RequestParam int round) {
        GameSnapshot opponent = gameService.findOpponent(round);
        if (opponent == null) {
            // 如果没找到对手，我们临时捏造一个机器人（Bot）
            // 这里的逻辑很简单，只是为了不让程序崩掉
            // 真实开发中，我们可以让 Service 直接返回一个 Bot 对象
            return null; 
        }
        return opponent;
    }
}
