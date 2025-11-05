package com.example.expensetracker.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.expensetracker.R
import com.example.expensetracker.data.Transaction
import com.example.expensetracker.utils.getCategoryIcon

@Composable
fun TransactionItem(
    transaction: Transaction,
    onItemClick: (() -> Unit)? = null
) {
    val isIncome = transaction.type.equals("Income", ignoreCase = true)
    val iconRes = getCategoryIcon(transaction.category)

    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 12.dp, vertical = 6.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp, 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(45.dp)
                        .background(
                           if (isIncome) Color(0xFF81C784).copy(alpha = 0.4f)
                            else Color(0xFFE57373).copy(alpha = 0.4f),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(iconRes),
                        contentDescription = transaction.category,
                        tint = if (isIncome) Color(0xFF2E7D32) else Color(0xFFC62828),
                        modifier = Modifier
                            .size(25.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column{
                    Text("₹ ${"%.2f".format(transaction.amount)}",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isIncome) Color(0xFF2E7D32) else Color(0xFFC62828)
                    )
                    Text("${transaction.category} • ${transaction.paymentMode ?: "Cash"}",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
            }

            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.Center
            ) {
                Text(transaction.date,
                    fontSize = 13.sp,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(4.dp))

                Icon(painter = painterResource(R.drawable.money_bag_icon),
                    contentDescription = null,
                    modifier = Modifier
                        .size(20.dp),
                    tint = Color.Gray
                )
            }
        }
    }
}