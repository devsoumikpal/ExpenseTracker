package com.example.expensetracker

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.expensetracker.data.AppDatabase
import com.example.expensetracker.repository.TransactionRepository
import com.example.expensetracker.ui.screens.AddTransactionScreen
import com.example.expensetracker.ui.screens.AnalysisScreen
import com.example.expensetracker.ui.screens.HomeScreen
import com.example.expensetracker.ui.theme.ExpenseTrackerTheme
import com.example.expensetracker.utils.NotificationHelper
import com.example.expensetracker.viewmodel.TransactionViewModel
import com.example.expensetracker.viewmodel.TransactionViewModelFactory
import com.example.expensetracker.workers.NotificationWorker
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissions(arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 1)
        }

        val db = AppDatabase.getDatabase(this)
        val repository = TransactionRepository(db.transactionDao())
        val factory = TransactionViewModelFactory(repository, application)

        NotificationHelper.createNotificationChannel(this)
        val dailyWorkRequest = PeriodicWorkRequestBuilder<NotificationWorker>(
            1, TimeUnit.DAYS
        ).build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "daily_transaction_check",
            androidx.work.ExistingPeriodicWorkPolicy.KEEP,
            dailyWorkRequest
        )


        setContent {
            ExpenseTrackerTheme {
                val viewModel: TransactionViewModel = viewModel(factory = factory)
                val navController = rememberNavController()
                Scaffold { innerPadding->
                    NavHost(
                        navController = navController,
                        startDestination = "home",
                        modifier = Modifier.padding(innerPadding)
                    ){
                        composable("home"){
                            HomeScreen(navController, viewModel)
                        }
                        composable("add"){
                            AddTransactionScreen(navController, viewModel)
                        }
                        composable("analysis"){
                            AnalysisScreen(navController, viewModel)
                        }
                    }
                }
            }
        }
    }
}