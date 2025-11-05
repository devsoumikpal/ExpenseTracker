# 💰 ExpenseTracker App

ExpenseTracker is a simple yet powerful **personal finance management app** built using **Jetpack Compose** and **Room Database**.  
It helps users **track their expenses, income, and transfers**, analyze spending habits with **interactive charts**, and get **smart notifications** for budgeting.

---

## 🖼️ Screenshots

| Home Screen | Add Transaction | Analysis Screen |
|--------------|----------------|-----------------|
| ![Home](screenshots/home.png) | ![Add](screenshots/add_transaction.png) | ![Analysis](screenshots/analysis.png) |


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
