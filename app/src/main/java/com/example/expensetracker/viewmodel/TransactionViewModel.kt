package com.example.expensetracker.viewmodel


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensetracker.data.Transaction
import com.example.expensetracker.repository.TransactionRepository
import com.example.expensetracker.utils.NotificationHelper
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TransactionViewModel(private val repository: TransactionRepository,
    private val appContext: Application) : AndroidViewModel(appContext) {
    val allTransactions: StateFlow<List<Transaction>> = repository.allTransactions.stateIn(viewModelScope,
        SharingStarted.Lazily, emptyList())

    fun addTransaction(transaction: Transaction) = viewModelScope.launch {
        repository.addTransaction(transaction)

        checkAndNotify(transaction)
    }

    fun deleteTransaction(transaction: Transaction) = viewModelScope.launch {
        repository.deleteTransaction(transaction)
    }

    private suspend fun checkAndNotify(transaction: Transaction) {
        val transactions = repository.allTransactions.first()

        val totalIncome = transactions.filter { it.type == "Income" }.sumOf { it.amount }
        val totalExpense = transactions.filter { it.type == "Expense" }.sumOf { it.amount }
        val balance = totalIncome - totalExpense

        // 1️⃣ Balance below ₹100
        if (balance < 100) {
            NotificationHelper.showNotification(
                appContext,
                "⚠️ Low Balance Alert",
                "Your balance is dangerously low — ₹${"%.2f".format(balance)} left.",
                1
            )
        }

        // 2️⃣ Monthly expense exceeds ₹2000
        if (totalExpense > 2000 && totalExpense < 5000) {
            NotificationHelper.showNotification(
                appContext,
                "💸 Spending Alert",
                "You've crossed ₹2000 in expenses this month!",
                2
            )
        }

        // 3️⃣ Each ₹5000 threshold
        if (totalExpense % 5000 in 0.0..100.0) {
            NotificationHelper.showNotification(
                appContext,
                "🧾 Budget Update",
                "You’ve spent ₹${"%.0f".format(totalExpense)} so far — review your budget!",
                3
            )
        }

        // 4️⃣ Expenses more than income
        if (totalExpense > totalIncome * 1.3) {
            NotificationHelper.showNotification(
                appContext,
                "🚨 Overspending Warning",
                "Your expenses are 30% higher than your income this month!",
                4
            )
        }

        // 5️⃣ Large single transaction
        if (transaction.amount > 1000) {
            NotificationHelper.showNotification(
                appContext,
                "💳 Big Transaction",
                "You spent ₹${transaction.amount} on ${transaction.category}.",
                5
            )
        }
    }
}