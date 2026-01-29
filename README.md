# 🎰 Asynchronous Slot Machine Battler

> A strategic PVP web game that fuses **Inventory Management** (backpack auto-battler style) with **Slot Machine Randomness**.

Built with a modern full-stack architecture: **Spring Boot + React + Docker**.

## 🎮 Game Overview

This is not just a game of luck; it is a game of probability management. You must carefully curate your "item pool" to maximize your chances of winning.

*   **🛒 Shop Phase:** Buy items with Gold to build your loadout. You have a **20-slot Backpack**, which acts as your "deck" or "pool". Keeping your pool pure is key to ensuring you draw your strongest combos in battle.
*   **⚔️ Battle Phase:** The system randomly draws **9 items** from your backpack to fill a **3x3 Grid**.
    *   **Attack & Defense:** Items have base stats.
    *   **Synergies:** Items interact with each other (e.g., *Crossbow* gains ATK if you have other weapons; *Mushroom* gains DEF for every Food item).
*   **👻 Asynchronous PVP:** You don't fight live opponents. You battle against **Snapshots** left behind by other players in the same round. This allows for instant matchmaking and a pause-anytime gameplay loop.

## 🛠️ Tech Stack

This project implements a robust, containerized microservices-like architecture.

*   **Frontend:**
    *   **Framework:** React 19 (TypeScript)
    *   **Build Tool:** Vite 7
    *   **Styling:** Pure CSS (Pixel Art Theme)
    *   **State Management:** React Context API
    *   **Routing:** React Router 7
*   **Backend:**
    *   **Language:** Java 17
    *   **Framework:** Spring Boot 3.2.2
    *   **ORM:** Spring Data JPA
    *   **Build Tool:** Maven
*   **Database:**
    *   **DB:** PostgreSQL 15 (Alpine)
    *   **Data Structure:** Relational + JSONB for flexible inventory storage
*   **Infrastructure:**
    *   **Containerization:** Docker & Docker Compose
    *   **Server:** Nginx (Frontend reverse proxy), Embedded Tomcat (Backend)

## 🚀 Quick Start

You only need **Docker Desktop** installed to run the entire game environment.

### 1. Clone the Repository
```bash
git clone https://github.com/your-username/asynchronous-slot-machine-battler.git
cd asynchronous-slot-machine-battler
```

### 2. Launch with One Command
```bash
docker-compose up --build -d
```
*Note: The first launch may take 5-10 minutes to download Maven dependencies and build the Docker images.*

### 3. Play
Open your browser and visit:
*   **Game UI:** [http://localhost](http://localhost)
*   **Backend API:** [http://localhost:8080](http://localhost:8080)

## 📂 Project Structure

```text
.
├── backend/                # Spring Boot Application
│   ├── src/main/java/      # Source Code (Controller, Service, Models)
│   ├── Dockerfile          # Multi-stage Java build
│   └── pom.xml             # Maven dependencies
├── frontend/               # React Application
│   ├── src/                # Source Code (Pages, Context, Components)
│   ├── Dockerfile          # Nginx production build
│   └── nginx.conf          # Nginx configuration
├── data/                   # Database persistence (Git-ignored)
├── docker-compose.yml      # Orchestration for Frontend, Backend, and DB
└── README.md               # Project Documentation
```

## 📚 Item Encyclopedia & Mechanics

### Item Stats & Synergies
| Tier | Item Name | Type | Base Stats | Special Ability (Synergy) |
| :--- | :--- | :--- | :--- | :--- |
| **1** | **Dagger** | WEAPON | 3 ATK / 0 DEF | *None* |
| **1** | **Wooden Shield** | SHIELD | 0 ATK / 3 DEF | *None* |
| **1** | **Banana** | FOOD | 0 ATK / 2 DEF | *None* |
| **1** | **Rock** | NATURE | 1 ATK / 1 DEF | *None* |
| **2** | **Iron Sword** | WEAPON | 5 ATK / 0 DEF | *None* |
| **2** | **Kite Shield** | SHIELD | 0 ATK / 5 DEF | *None* |
| **2** | **Crossbow** | WEAPON | 4 ATK / 0 DEF | **+2 ATK** if grid contains >1 Weapon. |
| **2** | **Mushroom** | FOOD | 0 ATK / 3 DEF | **+1 DEF** for *each* Food item in grid. |
| **3** | **Golden Sword** | WEAPON | 8 ATK / 0 DEF | *None* |
| **3** | **Platinum Shield** | SHIELD | 0 ATK / 8 DEF | *None* |
| **3** | **Magic Wand** | MAGIC | 3 ATK / 0 DEF | **+3 ATK** for *each* Magic item in grid. |
| **3** | **Vampire Ring** | ACC. | 0 ATK / 0 DEF | **+1 DEF** (Heal) for *each* Weapon in grid. |

### Shop Odds (RNG)
The shop refreshes 5 items per roll.
*   **Rounds 1-3:** 100% Tier 1 (Common)
*   **Rounds 4-6:** 60% Tier 1, **40% Tier 2 (Rare)**
*   **Rounds 7+:** 40% Tier 1, 40% Tier 2, **20% Tier 3 (Epic)**

### Economy
*   **Starting Gold:** 10g
*   **Item Cost:** 3g
*   **Sell Item:** 3g (Full Refund!)
*   **Reroll Shop:** 1g
*   **Win Reward:** +10g
*   **Loss Penalty:** -1 Life, but still +10g (Pity gold)

### Combat Logic
1.  **Matchmaking:** If no opponent snapshot exists for the current round (e.g., you are the first player), you fight a **Training Dummy** (equipped with 5 Daggers).
2.  **Grid Draw:** 9 items are selected randomly from your inventory (with replacement? No, from pool).
3.  **Damage Phase:**
    *   `Your Damage Dealt = Max(0, Your ATK - Enemy DEF)`
    *   `Your Damage Taken = Max(0, Enemy ATK - Your DEF)`
4.  **Result:** If `Damage Dealt >= Damage Taken`, you win.

## 🔌 API Reference

For developers who want to interact directly with the backend.

| Method | Endpoint | Description | Payload / Params |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/login` | Login or Register | `?username=string` |
| `GET` | `/api/shop` | Get 5 random items | `?round=int` |
| `POST` | `/api/snapshot` | Save current state | `{ userId, round, inventory, stats }` |
| `GET` | `/api/opponent` | Find opponent snapshot | `?round=int` |
| `POST` | `/api/battle` | Calculate battle result | `{ inventory: json, round: int }` |

## 📝 Development Phases

*   **Phase 1:** Infrastructure Setup (Docker, PostgreSQL).
*   **Phase 2:** Backend Initialization (Spring Boot, JPA).
*   **Phase 3:** Core Logic Implementation (User, Snapshot, Matchmaking Service).
*   **Phase 4:** Frontend Framework Setup (React + Vite).
*   **Phase 5:** Shop & Inventory System Implementation.
*   **Phase 6:** Battle Algorithm & Slot Machine Animation.
*   **Phase 7:** UI Overhaul (Dark/Pixel Art Style).
*   **Phase 8:** Full Docker Containerization & Deployment.

---
*Created with ❤️ by Cursor AI & You*
