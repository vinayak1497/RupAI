# 💰 RupAI – Rural Financial Literacy Assistant 📱💡
![RupAILogo](https://github.com/user-attachments/assets/65db2504-4f31-46bd-8b41-4e7b8ee6d481)

**RupAI** (short for *Rupayya* + *AI*) is a mobile Android application built as part of the **Google Solution Challenge 2025**. It aims to improve **financial literacy** and empower **rural communities in India** by providing AI-powered tools and interactive features in a simple, modern interface.

---

## 🚀 Purpose

Rural populations often lack access to financial education, tools, and advice. RupAI bridges that gap by delivering a free, offline-friendly, AI-assisted mobile platform for:

- Budgeting 📊
- Smart Investing 💹
- Decision Making 🤖
- Learning through Quizzes 🎓
- Understanding Stocks 📈
- Managing Personal Finances 💸

---

## 🎯 Problem Statement

**How might we improve financial literacy and informed decision-making in rural Indian communities using mobile technology and AI?**

---

## 🛠️ Built With

- **Android Studio**
- **Java**
- **Firebase** (Auth + Firestore)
- **Gemini API** (Google AI)
- **AlphaVantage API** (Stock Data)
- **Modern XML UI Design**

---

## 📱 Features Overview

| Feature                  | Description                                       |
|--------------------------|---------------------------------------------------|
| 💰 Budget Planner         | Create and track budgets easily.                 |
| 🤖 AI Chatbot             | Ask financial questions in your own words.       |
| 📈 SIP Calculator         | Plan investments via SIP and compare with FD.   |
| ❓ Quiz Game              | Test your finance knowledge with MCQs.           |
| 🧾 Purchase Analyzer      | AI-driven decision tool – *“Should I buy or not?”* |
| 📊 Stock Insights         | View live stock prices, news, and graphs.       |

---

## 📂 Folder Structure (Package `com.example.finvoice`)

| File / Activity              | Purpose |
|-----------------------------|---------|
| `SplashActivity`            | App splash screen with animated logo. |
| `WelcomeActivity`           | Welcome screen with “Get Started” button. |
| `LoginActivity` / `SignupActivity` | Firebase Auth integration. |
| `BudgetPlannerActivity`     | Budget feature with Firestore. |
| `AiChatBot`                 | Gemini AI integration for financial queries. |
| `PurchaseAnalyzerActivity`  | Analyze purchases using AI and logic. |
| `QuizActivity` / `ScoreActivity` / `StartQuiz` | Finance quiz module. |
| `SIPCalculator`             | SIP vs FD calculator with sliders. |
| `StockMarketActivity`       | Shows live stock prices and chart. |
| `UserDataSingleton`        | Manages Firebase user data across app. |

---

## 🎬 App Flow

1. **Splash Screen**: Logo animation.
2. **Welcome Screen**: Get Started → login/signup.
3. **Signup or Login**: Authentication and registration done here. (auth by firebase auth, data stored by Firestore realtime database)
4. **On Boarding Menu**: Main home page with all the functionality at one page.
5. **Each module**: Opens in a clean, separate activity.

---

## 🔐 Authentication

- **Firebase Authentication**
- Supports **email & password sign-in**
- User data securely stored via Firebase Realtime DB

---

## 🧠 AI Integration

- **Gemini API** (via ExecutorService and `generateContent()` calls)
- Used in:
  - AI Chatbot
  - Purchase Analyzer

---

## 📊 Stock API Integration

-1. **AlphaVantage API**
- Real-time and historical stock data. (US Stock based)
- All API calls are **free-tier friendly**

-2. **Gemini Flash 2.O**
- Realtime AI Chat assistance, Budget Planner, Purchase Analyser.

---

## 🎨 Design

- Minimalist, clean design
- Primary color: **Purple (#6200EE)** with white backgrounds
- Consistent modern typography and spacing
- Lottie animations (optional)

---

## 🌐 Future Enhancements

- Multilingual support (Hindi, Marathi, etc.)
- Offline mode with local storage
- Voice-based AI chat for accessibility
- Gamification of quizzes and rewards

---

## 👩‍💻 Contributors

- **Developer**: Vinayak Umesh Kundar, Paarth Sanjay Kothari, Tanush Arvind Jain, Viresh Mallikarjun Kumbhar.  
- **Organization**: Google Developer Groups (GDGoCampus APSIT) – A.P. Shah Institue of Technology

---

## 📦 Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/RupAI.git
