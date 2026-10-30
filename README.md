# Cynbit AI Discovery Assistant

[![Live Demo](https://img.shields.io/badge/Live-Demo-brightgreen?style=for-the-badge&logo=render)](https://cynbit-ai-assistant.onrender.com)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.2.5-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-17-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://www.oracle.com/java/)
[![MySQL](https://img.shields.io/badge/MySQL-Clever_Cloud-4479A1?style=for-the-badge&logo=mysql&logoColor=white)](https://www.clever-cloud.com/)
[![Groq AI](https://img.shields.io/badge/Groq-API-F55036?style=for-the-badge)](https://groq.com/)
[![Docker](https://img.shields.io/badge/Docker-Multi--Stage-2496ED?style=for-the-badge&logo=docker&logoColor=white)](https://www.docker.com/)

An intelligent conversational discovery agent and automated lead qualification system engineered for **Cynbit Technologies**. Built with Spring Boot 3, Hibernate/JPA, Groq LLM inference, and Clever Cloud MySQL, containerized via multi-stage Docker builds and hosted on Render.

---

## 🚀 Live Application

- **Production URL:** [https://cynbit-ai-assistant.onrender.com](https://cynbit-ai-assistant.onrender.com)
- **High Availability:** Automated keep-alive synthetic pings via UptimeRobot to eliminate cold starts.

---

## 🛠️ Architecture & Tech Stack

- **Backend Framework:** Java 17, Spring Boot 3.2.5
- **Data Persistence:** Spring Data JPA, Hibernate, Clever Cloud Managed MySQL
- **AI Inference Engine:** Groq API (`openai/gpt-oss-20b` model)
- **Containerization:** Multi-Stage Docker Build (`maven:3.9.6-eclipse-temurin-17` -> `eclipse-temurin:17-jre-alpine`)
- **Hosting & Infrastructure:** Render Web Services & Clever Cloud Add-ons
- **Monitoring:** UptimeRobot 5-minute health probe

- [ Browser Client / Landing Page ]
│
▼  HTTPS
[ Render Web Service (Docker Container) ]
│                      │
▼ REST (Inference)     ▼ JDBC (Data Persistence)
[ Groq AI API ]       [ Clever Cloud MySQL ]

---

## ✨ Core Features

1. **AI Discovery Assistant:** Real-time conversational agent answering queries regarding Cynbit Technologies' engineering services.
2. **Dynamic Lead Capture:** Automatically captures client inquiries, technical specifications, and contact details directly into MySQL.
3. **Optimized Containerization:** Multi-stage Docker build separating Maven dependency compilation from the minimal Alpine JRE execution layer.
4. **Environment Isolation:** Zero hardcoded secrets; configuration dynamically driven by environment variables.

---

## ⚙️ Environment Variables

| Variable | Description |
| :--- | :--- |
| `SPRING_DATASOURCE_URL` | Cloud MySQL JDBC connection string |
| `SPRING_DATASOURCE_USERNAME` | Database username |
| `SPRING_DATASOURCE_PASSWORD` | Database password |
| `GROQ_API_KEY` | Groq Cloud inference API token |
| `PORT` | Dynamically bound server port (defaults to 8080 locally) |

---

## 💻 Local Development Setup

### Prerequisites
- JDK 17+
- Maven 3.9+ (or included `./mvnw`)
- MySQL 8.0+

