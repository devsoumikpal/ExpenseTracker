package com.example.expensetracker.workers

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.expensetracker.data.AppDatabase
import com.example.expensetracker.utils.NotificationHelper
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class NotificationWorker(
    private val context: Context,
    params: WorkerParameters
) : Worker(context, params) {

    override fun doWork(): Result {
        return runBlocking {
            val dao = AppDatabase.getDatabase(context).transactionDao()
            val transactions = dao.getAllTransactions().first()

            val today = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date())
            val todayTransactions = transactions.filter { it.date == today }

            if (todayTransactions.isEmpty()) {
                NotificationHelper.showNotification(
                    applicationContext,
                    "🕒 Reminder",
                    "You haven’t added any transactions today — stay on track!",
                    101
                )
            }

            Result.success()
        }

    }
}