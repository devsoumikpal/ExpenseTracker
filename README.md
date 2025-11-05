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

The app follows **MVVM (Model–View–ViewModel)** architecture:
