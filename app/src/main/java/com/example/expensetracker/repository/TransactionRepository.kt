package com.example.expensetracker.repository


import com.example.expensetracker.data.Transaction
import com.example.expensetracker.data.TransactionDao

class TransactionRepository(private val transactionDao: TransactionDao) {
    val allTransactions = transactionDao.getAllTransactions()

    suspend fun addTransaction(transaction: Transaction) =
        transactionDao.insert(transaction)

    suspend fun deleteTransaction(transaction: Transaction) =
        transactionDao.delete(transaction)
}