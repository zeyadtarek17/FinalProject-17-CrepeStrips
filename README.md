# FinalProject-17-CrepeStrips
# LeftOverPal

**LeftOverPal** is a distributed microservices-based system designed to connect users with restaurants offering surplus meals at discounted prices. The platform promotes sustainability by reducing daily food waste while providing users access to affordable, end-of-day meals.

This project is developed by 15 students from the German University in Cairo as part of the **Architecture of Massively Scalable Applications – Spring 2025** course. The system features modular services for user management, restaurant operations, food item handling, order processing, and administrative control — each implemented as an independent microservice.

---

## 🧩 Microservices Overview

| Microservice           | Description                                                                  |
|------------------------|-------------------------------------------------------------------------------|
| **User Service**       | Handles user authentication, password changes, and issue reporting           |
| **Order Service**      | Updates restaurant stock, checks order status, and prioritizes urgent orders |
| **Restaurant Service** | Manages food items and filters by category or operating hours                |
| **FoodItem Service**   | Applies discounts, filters inventory, and sorts food by rating               |
| **Admin Service**      | Processes reports, suspends food items, bans restaurants                     |
| **API Gateway**        | Routes client requests to the respective microservices                       |

---

## 🗄️ Database Design

| Service         | Database Type      | Description                                       |
|-----------------|--------------------|---------------------------------------------------|
| **User**        | PostgreSQL + Redis | Stores user data and sessions (via Redis)         |
| **Restaurant**  | MongoDB            | Contains restaurant and category documents        |
| **FoodItem**    | MongoDB            | Stores food item records                          |
| **Order**       | PostgreSQL         | Maintains order logs and statuses                 |
| **Admin**       | MongoDB            | Tracks admin data and moderation logs             |

---

## ⚙️ Technology Stack

- **Backend:** Java + Spring Boot  
- **Databases:** PostgreSQL, MongoDB, Redis  
- **Communication:** REST APIs (sync), RabbitMQ (async)  
- **Containerization:** Docker  
- **Orchestration & Load Balancing:** Kubernetes  
- **Service Routing:** Spring Cloud API Gateway  

---

## 🧠 Design Patterns Applied

| Service         | Design Patterns Used          |
|-----------------|-------------------------------|
| **User**        | Singleton, Builder            |
| **Order**       | Command, Strategy             |
| **Restaurant**  | Factory, Strategy             |
| **FoodItem**    | Factory, Observer             |
| **Admin**       | Command, Observer             |

---

## 🔁 Communication Flow

- **RESTful:** All microservices support synchronous HTTP communication  
- **RabbitMQ (Async):** Used primarily between **User → Order Service**

---
