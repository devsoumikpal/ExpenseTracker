package com.example.expensetracker.ui.screens

import android.content.Context
import android.view.ViewGroup
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.rounded.KeyboardArrowRight
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.example.expensetracker.utils.getCategoryIcon
import com.example.expensetracker.viewmodel.TransactionViewModel
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import java.util.Calendar
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalysisScreen(navController: NavController, viewModel: TransactionViewModel) {

    val calendar = Calendar.getInstance()
    var selectedMonth by remember { mutableStateOf(calendar.get(Calendar.MONTH)) }
    var selectedYear by remember { mutableStateOf(calendar.get(Calendar.YEAR)) }
    var showPickerDialog by remember { mutableStateOf(false) }

    val transactions by viewModel.allTransactions.collectAsState()

    val filteredTransactions = transactions.filter { txn ->
        val parts = txn.date.split(" ")
        if (parts.size == 3) {
            val month = listOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")
                .indexOfFirst { it.equals(parts[1].take(3), ignoreCase = true) }
            val year = parts[2].toIntOrNull() ?: 0
            month == selectedMonth && year == selectedYear
        } else false
    }

    val categoryTotals = filteredTransactions
        .filter { it.type.equals("Expense", ignoreCase = true) }
        .groupBy { it.category }
        .mapValues { entry -> entry.value.sumOf { it.amount }.toFloat() }

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(
        state = rememberTopAppBarState()
    )
    Scaffold(
        topBar = {
            AnalysisTopBar(scrollBehavior = scrollBehavior, navController = navController)
        }
    ) {paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            MonthYearSelectionBar(
                selectedMonth = selectedMonth,
                selectedYear = selectedYear,
                onMonthYearChanged = {newMonth, newYear ->
                    selectedMonth = newMonth
                    selectedYear = newYear
                },
                onPickerCLick = {
                    showPickerDialog = true
                }
            )

            Card (
                modifier = Modifier
                    .padding(top = 10.dp)
                    .background(Color.LightGray.copy(alpha = 0.3f)),
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Category wise spending (%)",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp))
                    if (categoryTotals.isEmpty()) {
                        Text(
                            text = "No expenses found for this month.",
                            color = Color.Gray,
                            modifier = Modifier.padding(20.dp)
                        )
                    } else {
                        ExpenseDonutChart(expenseData = categoryTotals)
                    }
                }
            }

            LazyColumn {
                items(categoryTotals.entries.toList()){index ->
                    SpendingCategory(
                        category = index.key,
                        amount = index.value.toDouble()
                    )
                }
            }

            if (showPickerDialog) {
                MonthYearPickerDialog(
                    currentMonth = selectedMonth,
                    currentYear = selectedYear,
                    onConfirm = { month, year ->
                        selectedMonth = month
                        selectedYear = year
                        showPickerDialog = false
                    },
                    onCancel = { showPickerDialog = false }
                )
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalysisTopBar(
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior,
    navController: NavController
) {
    TopAppBar(
        modifier = modifier,
        scrollBehavior = scrollBehavior,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background.copy(alpha = 0.7f)
        ),
        windowInsets = WindowInsets(top = 0.dp),
        title = {
            Text("Back to Home")
        },
        navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    Icons.Outlined.ArrowBack,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(start = 16.dp, end = 8.dp)
                        .size(45.dp)
                )
            }
        }
    )
}

@Composable
fun MonthYearSelectionBar(
    selectedMonth: Int,
    selectedYear: Int,
    onMonthYearChanged: (Int, Int) -> Unit,
    onPickerCLick: () -> Unit
) {
    val months = listOf(
        "January", "February", "March", "April", "May", "June",
        "July", "August", "September", "October", "November", "December"
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(vertical = 8.dp, horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Rounded.KeyboardArrowLeft,
            contentDescription = "Previous Month",
            modifier = Modifier
                .size(32.dp)
                .clickable {
                    var newMonth = selectedMonth - 1
                    var newYear = selectedYear
                    if (newMonth < 0) {
                        newMonth = 11
                        newYear--
                    }
                    onMonthYearChanged(newMonth, newYear)
                }
        )

        // Month + Year text
        Text(
            text = "${months[selectedMonth]} $selectedYear",
            style = TextStyle(
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            ),
            modifier = Modifier.clickable {
                onPickerCLick()// open dialog when text tapped
            }
        )

        // Right arrow - next month
        Icon(
            imageVector = Icons.Rounded.KeyboardArrowRight,
            contentDescription = "Next month",
            modifier = Modifier
                .size(30.dp)
                .clickable {
                    var newMonth = selectedMonth + 1
                    var newYear = selectedYear
                    if (newMonth > 11) {
                        newMonth = 0
                        newYear++
                    }
                    onMonthYearChanged(newMonth, newYear)
                }
        )
    }
}

@Composable
fun MonthYearPickerDialog(
    currentMonth: Int,
    currentYear: Int,
    onConfirm: (Int, Int) -> Unit,
    onCancel: () -> Unit
) {
    var selectedMonth by remember { mutableStateOf(currentMonth) }
    var selectedYear by remember { mutableStateOf(currentYear) }

    val months = listOf(
        "Jan", "Feb", "Mar", "Apr", "May", "Jun",
        "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
    )

    AlertDialog(
        onDismissRequest = { onCancel() },
        confirmButton = {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                TextButton (onClick = onCancel) {
                    Text("Cancel")
                }
                Button(onClick = { onConfirm(selectedMonth, selectedYear) }) {
                    Text("Ok")
                }
            }
        },
        title = {
            Text(
                text = "Select Month & Year",
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier.padding(bottom = 8.dp)
            )
        },
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // YEAR SELECTOR
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Rounded.KeyboardArrowLeft,
                        contentDescription = "Previous Year",
                        modifier = Modifier
                            .size(32.dp)
                            .clickable { selectedYear-- }
                    )
                    Text(
                        text = "$selectedYear",
                        style = TextStyle(
                            fontSize = 22.sp,
                            fontWeight = FontWeight.SemiBold
                        ),
                        modifier = Modifier.padding(horizontal = 12.dp)
                    )
                    Icon(
                        imageVector = Icons.Rounded.KeyboardArrowRight,
                        contentDescription = "Next Year",
                        modifier = Modifier
                            .size(32.dp)
                            .clickable { selectedYear++ }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // MONTH GRID
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    modifier = Modifier
                        .height(250.dp)
                        .padding(horizontal = 8.dp)
                ) {
                    itemsIndexed(months) { index, month ->
                        Box(
                            modifier = Modifier
                                .padding(6.dp)
                                .clip(RoundedCornerShape(12.dp,))
                                .background(
                                    if (index == selectedMonth)
                                        Color(0xFF6CB14C)
                                    else
                                        Color(0xFFF2F2F2)
                                )
                                .clickable { selectedMonth = index }
                                .height(40.dp)
                                .fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = month,
                                color = if (index == selectedMonth) Color.White else Color.Black,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun ExpenseDonutChart(expenseData : Map<String, Float>) {
    AndroidView(
        factory = {ctx: Context ->
            PieChart(ctx).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )

                // Style
                isDrawHoleEnabled = true
                holeRadius = 45f
                transparentCircleRadius = 50f
                setUsePercentValues(true)
                setDrawEntryLabels(false)
                description.isEnabled = false

                legend.isEnabled = true
                legend.textSize = 12f
                legend.orientation = Legend.LegendOrientation.HORIZONTAL
                legend.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
                legend.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
                legend.form = Legend.LegendForm.CIRCLE

                setDrawCenterText(true)
                centerText = "Expenses"
                setCenterTextSize(16f)
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .padding(8.dp),
        update = { pieChart ->
            val cleanedData = expenseData
                .filter { it.value > 0f }
                .toList()
                .sortedByDescending { it.second }

            if (cleanedData.isEmpty()){
                pieChart.clear()
                pieChart.centerText = "No Data"
                return@AndroidView
            }

            val entries = expenseData.map { (category, value) ->
                PieEntry(value, category)
            }

            val colors = mutableListOf<Int>()

            val predefined= listOf(
                "#FF7043", "#4CAF50", "#03A9F4", "#FFC107", "#9C27B0",
                "#E91E63", "#009688", "#8BC34A", "#CDDC39", "#FF9800",
                "#F44336", "#607D8B"
            )

            entries.forEachIndexed { index, _ ->
                colors.add(android.graphics.Color.parseColor(predefined[index % predefined.size]))
            }

            val dataSet = PieDataSet(entries, "").apply {
                setColors(colors)
                sliceSpace = 2f
                valueTextSize = 12f
                valueTextColor = android.graphics.Color.BLACK
            }

            val pieData = PieData(dataSet)
            pieChart.data = pieData


            val total = cleanedData.sumOf { it.second.toDouble() }
            pieChart.centerText = "₹%.0f".format(total)

            //refresh
            pieChart.invalidate()
        }
    )
}

@Composable
fun SpendingCategory(
//    image: Image,
    category: String,
    amount: Double
) {
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
            ){
                Image(painter = painterResource(id = getCategoryIcon(category)),
                    contentDescription = category,
                    modifier = Modifier
                        .size(45.dp)
                        .padding(4.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = category,
                    style = MaterialTheme.typography.bodyLarge,
                    fontFamily = FontFamily.Default,
                    fontWeight = FontWeight.Medium
                )
            }
            Text(
                text = "₹${"%.2f".format(amount)}",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun SpendingDetailItem(
    category: String,
    amount: Double,
    percentage: Double,
    percentageChange: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = getCategoryIcon(category)),
                contentDescription = category,
                modifier = Modifier.size(32.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = category,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "${"%.1f".format(percentage)}% of total",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "₹${"%.2f".format(amount)}",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = percentageChange,
                    style = MaterialTheme.typography.bodySmall,
                    color = if (percentageChange.startsWith("+")) Color.Green else Color.Red
                )
            }
        }
    }
}

private fun calculatePercentageChange(currentAmount: Double, totalSpending: Double): String {
    if (totalSpending == 0.0) return "+0.0%"

    return when {
        currentAmount / totalSpending > 0.3 -> "+${Random.nextInt(20, 41)}.${Random.nextInt(0, 10)}%"
        currentAmount / totalSpending > 0.15 -> "+${Random.nextInt(10, 26)}.${Random.nextInt(0, 10)}%"
        currentAmount / totalSpending > 0.05 -> "-${Random.nextInt(5, 16)}.${Random.nextInt(0, 10)}%"
        else -> "-${Random.nextInt(20, 51)}.${Random.nextInt(0, 10)}%"
    }
}
