# 🎰 异步老虎机大乱斗 (Asynchronous Slot Machine Battler)

> 一个融合了 **背包管理** 策略与 **老虎机随机性** 的异步 PVP 网页游戏。
> 采用 **Spring Boot + React + Docker** 全栈开发。

## 🎮 游戏介绍

这不仅仅是一个看脸的游戏，你需要精心管理你的“装备池”。

*   **🛒 商店阶段**: 用有限的金币购买装备。你的背包有 20 个格子，你需要控制“池子”的纯度，确保战斗时能摇出最强的组合。
*   **⚔️ 战斗阶段**: 系统从你的 20 个装备中随机抽取 9 个填满 3x3 网格。攻击力、防御力和特殊连携效果决定胜负。
*   **👻 异步对战**: 你面对的不是在线玩家，而是其他玩家在相同轮次留下的“快照 (Snapshot)”。随时玩，随时停。

## 🛠️ 技术栈 (Tech Stack)

本项目采用现代化的前后端分离架构，并完全容器化。

*   **前端**: React 18, TypeScript, Vite, Axios, 纯 CSS (像素风 UI)
*   **后端**: Java 17, Spring Boot 3, Spring Data JPA
*   **数据库**: PostgreSQL 15 (存储 JSONB 数据)
*   **运维**: Docker, Docker Compose, Nginx

## 🚀 快速开始 (Quick Start)

你只需要安装 **Docker Desktop** 即可运行整个游戏。

### 1. 克隆项目
```bash
git clone https://github.com/your-username/slot-machine-battler.git
cd slot-machine-battler
```

### 2. 一键启动
```bash
docker-compose up --build -d
```
*首次启动需要下载 Maven 和 Node 依赖，可能需要 5-10 分钟，请耐心等待。*

### 3. 开始游戏
打开浏览器访问：**[http://localhost](http://localhost)**

## 📂 项目结构

```text
.
├── backend/            # Java Spring Boot 后端
│   ├── src/            # 源代码 (Controller, Service, Model)
│   ├── Dockerfile      # 后端构建脚本
│   └── pom.xml         # Maven 依赖
├── frontend/           # React 前端
│   ├── src/            # 源代码 (Pages, Components, Context)
│   ├── Dockerfile      # 前端构建脚本
│   └── nginx.conf      # Nginx 配置文件
├── docker-compose.yml  # 全栈容器编排
└── README.md           # 你在这里
```

## 🎯 游戏规则

1.  **初始状态**: 10 金币，3 生命。
2.  **Round 1-3**: 商店只刷新 Tier 1 装备 (如 Rusty Sword, Wooden Shield)。
3.  **Round 4+**: 开始出现 Tier 2 强力装备 (如 Vampire Fang)。
4.  **胜利条件**: 存活并通关 Round 10。
5.  **失败条件**: 生命值归零。

## 📝 开发日志

*   **Phase 1**: 搭建 Docker & PostgreSQL 基础设施。
*   **Phase 2**: 初始化 Spring Boot 后端与数据库连接。
*   **Phase 3**: 实现核心业务逻辑 (User, Snapshot, Matchmaking)。
*   **Phase 4**: 搭建 React + Vite 前端框架。
*   **Phase 5**: 完成商店与库存管理系统。
*   **Phase 6**: 实现核心战斗算法与老虎机动画。
*   **Phase 7**: UI 大改版 (暗黑像素风格)。
*   **Phase 8**: 全栈 Docker 容器化部署。

---
*Created with ❤️ by Cursor AI & You*
