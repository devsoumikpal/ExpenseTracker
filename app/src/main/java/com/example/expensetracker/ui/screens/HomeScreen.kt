package com.example.expensetracker.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.expensetracker.R
import com.example.expensetracker.ui.components.TransactionItem
import com.example.expensetracker.viewmodel.TransactionViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController, viewModel: TransactionViewModel) {
    val currentMonth = remember { SimpleDateFormat("MMM yyyy", Locale.getDefault()).format(Date()) }

    val transactions by viewModel.allTransactions.collectAsState()

    val totalIncome = transactions.filter { it.type == "Income" }.sumOf { it.amount }
    val totalExpense = transactions.filter { it.type == "Expense" }.sumOf { it.amount }
    val  balance = totalIncome - totalExpense

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(
        state = rememberTopAppBarState()
    )
    Scaffold(
        topBar = {
            HomeTopBar(scrollBehavior = scrollBehavior)
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {navController.navigate("add")}
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Transaction")
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .padding(vertical = 5.dp)
                                .clip(RoundedCornerShape(30.dp))
                                .background(Color(0xFFdb0b0b))
                                .padding(horizontal = 10.dp, vertical = 8.dp)
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Box(
                                    modifier = Modifier
                                        .padding(2.dp)
                                        .size(35.dp)
                                        .background(
                                            Color.White.copy(alpha = 0.4f),
                                            shape = CircleShape
                                        )
                                ) {
                                    Icon(Icons.Outlined.KeyboardArrowDown,
                                        contentDescription = null,
                                        modifier = Modifier.size(30.dp),
                                        tint = Color.White)
                                }
                                Column(
                                    modifier = Modifier.padding(start = 10.dp, end = 35.dp),
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Text("Spending", color = Color.White.copy(alpha = 0.7f))
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text("₹${totalExpense}", color = Color.White,fontSize = 18.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.width(5.dp))
                        Box(
                            modifier = Modifier
                                .padding(vertical = 5.dp)
                                .clip(RoundedCornerShape(30.dp))
                                .background(Color(0XFF18a308))
                                .padding(horizontal = 10.dp, vertical = 8.dp)
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Box(
                                    modifier = Modifier
                                        .padding(2.dp)
                                        .size(35.dp)
                                        .background(
                                            Color.White.copy(alpha = 0.5f),
                                            shape = CircleShape
                                        ),
                                ) {
                                    Icon(Icons.Outlined.KeyboardArrowDown,
                                        contentDescription = null,
                                        modifier = Modifier.size(30.dp),
                                        tint = Color.White)
                                }
                                Column(
                                    modifier = Modifier.padding(start = 10.dp, end = 35.dp),
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Text("Income", color = Color.White.copy(alpha = 0.7f))
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text("₹${totalIncome}", color = Color.White,fontSize = 18.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(35.dp))
                            .background(Color.Black.copy(alpha = 0.4f))
                            .padding(vertical = 15.dp, horizontal = 20.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Balance: ₹${"%.2f".format(balance)}", color = Color.White, fontSize = 17.sp)
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Recent Transactions", color = Color.Black, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Text("See all", color = Color.Black, fontSize = 15.sp,
                        modifier = Modifier.clickable{ navController.navigate("analysis")})
                }

            }

            if (transactions.isEmpty()){
                item {
                    Text("No transaction yet. ", color = Color.Gray, modifier = Modifier.padding(20.dp))
                }
            }else {
                items(transactions) {txn ->
                    TransactionItem(transaction = txn)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopBar(
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior
) {
    TopAppBar(
        modifier = modifier
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(100.dp)),
        scrollBehavior = scrollBehavior,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f)
        ),
        windowInsets = WindowInsets(top = 0.dp),
        title = {
            Text("This month",
                color = MaterialTheme.colorScheme.onBackground)
        },
        navigationIcon = {
            Icon(
                painter = painterResource(id = R.drawable.expense_tracker),
                contentDescription = null,
                modifier = Modifier.padding(start = 12.dp, end = 4.dp)
                    .size(50.dp)
            )
        }
    )
}

//@Composable
//fun TransactionItem(
//    intRes: Int,
//    amount: String,
//    category: String,
//    date: String,
//    isIncome: Boolean,
//    paymentMode: String
//) {
//    Card(modifier = Modifier
//        .fillMaxWidth()
//        .padding(horizontal = 12.dp, vertical = 6.dp),
//        shape = RoundedCornerShape(16.dp),
//        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
//    ) {
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(12.dp, 10.dp),
//            verticalAlignment = Alignment.CenterVertically,
//            horizontalArrangement = Arrangement.SpaceBetween
//        ) {
//            Row(
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                Box(
//                    modifier = Modifier
//                        .size(45.dp)
//                        .background(
//                            Color.Gray.copy(alpha = 0.2f),
//                            shape = CircleShape
//                        ),
//                    contentAlignment = Alignment.Center
//                ) {
//                    Icon(
//                        painter = painterResource(intRes),
//                        contentDescription = t,
//                        tint = Color.Magenta,
//                        modifier = Modifier
//                            .size(25.dp)
//                            .padding(4.dp)
//                    )
//                }
//                Spacer(modifier = Modifier.width(10.dp))
//                Column{
//                    Text("₹ $amount",
//                        fontSize = 22.sp,
//                        fontWeight = FontWeight.Bold,
//                        color = Color.Black)
//                    Text(paymentMode,
//                        fontSize = 14.sp,
//                        color = Color.Gray
//                    )
//                }
//            }
//
//            Column(
//                horizontalAlignment = Alignment.End,
//                verticalArrangement = Arrangement.Center
//            ) {
//                Text(date,
//                    fontSize = 13.sp,
//                    color = Color.Gray
//                )
//                Spacer(modifier = Modifier.height(4.dp))
//
//                Icon(painter = painterResource(R.drawable.money_bag_icon),
//                    contentDescription = null,
//                    modifier = Modifier
//                        .size(30.dp)
//                        .padding(4.dp)
//                )
//            }
//        }
//    }
//}