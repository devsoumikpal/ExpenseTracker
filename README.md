# 💰 ExpenseTracker App

ExpenseTracker is a simple yet powerful **personal finance management app** built using **Jetpack Compose** and **Room Database**.  
It helps users **track their expenses, income, and transfers**, analyze spending habits with **interactive charts**, and get **smart notifications** for budgeting.

---

## 🖼️ Screenshots

## 📱 App Screenshots

| 🏠 Home Screen | ➕ Add Transaction | 📊 Analysis Screen |
|----------------|-------------------|--------------------|
| <img src="https://github.com/devsoumikpal/ExpenseTracker/blob/master/home_screen_ui.png" width="250"/> | <img src="https://github.com/devsoumikpal/ExpenseTracker/blob/master/add_transactions_ui.png" width="250"/> | <img src="https://github.com/devsoumikpal/ExpenseTracker/blob/master/analysis_screen_ui.png" width="250"/> |

| 📉 Analysis (No Data) | 🔔 Notification Example |
|-----------------------|-------------------------|
| <img src="https://github.com/devsoumikpal/ExpenseTracker/blob/master/analysis_screen_no_data.png" width="250"/> | <img src="https://github.com/devsoumikpal/ExpenseTracker/blob/master/notification_show.png" width="250"/> |



---

## 🚀 Features

✅ **Add, Edit & Delete Transactions** — manage income, expense, and transfer records easily.  
✅ **Category-based Expense Visualization** — view detailed category charts using **PieChart (MPAndroidChart)**.  
✅ **Room Database Integration** — ensures offline access and fast local storage.  
✅ **Smart Notifications** (via WorkManager):  
- ⚠️ Balance below ₹100 alert  
- 💸 Monthly expense exceeds ₹2000  
- 🧾 Every ₹5000 spending threshold  
- 💳 Expense exceeds income warning  
- 🕒 No transactions added today reminder  
✅ **Modern UI** — fully built with Jetpack Compose and Material 3.  
✅ **Dark/Light Theme Support** (optional for UI extension).

---

## 🧠 Architecture Overview

The app follows the **MVVM (Model–View–ViewModel)** architecture pattern, ensuring a clean separation of concerns and improved testability.

### 🏗️ Layers Breakdown

#### **1️⃣ Model Layer**
- Contains data classes such as `Transaction`.
- Uses **Room Database** (`AppDatabase`, `TransactionDao`) for local persistence.
- The **Repository** (`TransactionRepository`) handles data access logic and provides a single source of truth for ViewModels.

#### **2️⃣ ViewModel Layer**
- `TransactionViewModel` acts as a bridge between UI and data layers.
- Uses **Kotlin Coroutines** and **StateFlow** for real-time data updates.
- Handles business logic like calculating balance, filtering transactions, and triggering notifications.

#### **3️⃣ View (UI) Layer**
- Built with **Jetpack Compose** for a modern declarative UI.
- Screens:  
  - 🏠 `HomeScreen` — Displays current balance, income, and recent transactions.  
  - ➕ `AddTransactionScreen` — Allows users to add or edit transactions with category and payment mode selection.  
  - 📊 `AnalysisScreen` — Shows monthly spending insights with pie charts and category-wise analysis.

### ⚙️ Supporting Components
- **WorkManager**: Schedules daily background tasks for notification alerts.
- **NotificationHelper**: Manages Android notification channels and triggers budget alerts.
- **Material3 Components**: Provides a consistent, modern design system.

---

🧩 This architecture ensures:
- Clean and scalable codebase.  
- Reactive and real-time UI updates.  
- Easy maintainability and testing.

