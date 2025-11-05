package com.example.expensetracker.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.expensetracker.R
import com.example.expensetracker.data.Transaction
import com.example.expensetracker.utils.getCategoryIcon
import com.example.expensetracker.viewmodel.TransactionViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTransactionScreen(navController: NavController, viewModel: TransactionViewModel) {
    var selectedIndex by remember { mutableIntStateOf(0) }
    val transactionOptions = listOf("Expense", "Income", "Transfer")

    var date by remember { mutableStateOf(getCurrentDate()) }
    var amount by remember { mutableStateOf(TextFieldValue("")) }
    var category by remember { mutableStateOf("Others") }
    var paymentMode by remember { mutableStateOf("Cash") }
    var note by remember { mutableStateOf(TextFieldValue("")) }
    var selectedTags by remember { mutableStateOf(listOf<String>()) }
    val availableTags = listOf("vacation", "shopping", "business", "fees")

    val context= LocalContext.current
    val calendar = Calendar.getInstance()
    val datePicker = android.app.DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            calendar.set(year, month, dayOfMonth)
            date = SimpleDateFormat(
                "dd MMM yyyy", Locale.getDefault()).format(calendar.time)
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(
        state = rememberTopAppBarState()
    )
    var showCategoryDialog by remember { mutableStateOf(false) }

    val categoryOptions = listOf(
        "Food", "Fruits & vegetables", "Groceries", "Snacks",
        "Entertainment", "Transportation", "Shopping", "Education",
        "Travel", "Salary", "Investment", "Health", "Pet",
        "Gift", "Housing", "Electronics", "Beauty", "Clothing", "Social", "Others"
    )
    Scaffold(
        topBar = {
            AddTransactionTopBar(scrollBehavior = scrollBehavior, navController = navController)
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color.White)
        ) {
            item {
                // Transaction Type Selector with dark background
                SingleChoiceSegmentedButtonRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 5.dp)
                ) {
                    transactionOptions.forEachIndexed { index, label ->
                        SegmentedButton(
                            shape = SegmentedButtonDefaults.itemShape(
                                index = index,
                                count = transactionOptions.size
                            ),
                            onClick = {selectedIndex = index},
                            selected = index == selectedIndex,
                            label = { Text(label)}
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row (
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Icon(imageVector = Icons.Outlined.DateRange,
                        contentDescription = null,
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .size(26.dp),
                        tint = Color(0xFFFF7043))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = date,
                        fontSize = 20.sp,
                        modifier = Modifier
                            .clickable{datePicker.show()}
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))
                // Amount Section
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable{}) {
                    Text("Amount", fontSize = 16.sp, modifier = Modifier.padding(horizontal = 66.dp))
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.education_icon),
                            contentDescription = "Amount",
                            modifier = Modifier.size(35.dp),
                            tint = Color(0xFFFF7043)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(12.dp))
                                .padding(horizontal = 16.dp, vertical = 10.dp)
                        ) {
                            BasicTextField(
                                value = amount,
                                onValueChange = {amount = it},
                                textStyle = TextStyle.Default,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true,
                                decorationBox = { innerTextField ->
                                    if (amount.text.isEmpty()){
                                        Text(text = "Enter amount",
                                            color = Color.LightGray,
                                            fontSize = 18.sp)
                                    }
                                    innerTextField()
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(15.dp))
                // Category Section
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .clickable{showCategoryDialog = true}
                ) {
                    Text("Category", fontSize = 16.sp, modifier = Modifier.padding(horizontal = 50.dp))
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(id = R.drawable.vegetables_icon),
                            contentDescription = null,
                            modifier = Modifier.size(35.dp),
                            tint = Color(0xFFFF7043)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(12.dp))
                                .padding(horizontal = 16.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = if (category.isEmpty()) "Select category" else category,
                                    fontSize = 16.sp,
                                    color = if (category.isEmpty()) Color.Gray else Color.Black
                                )
                                Icon(Icons.Default.KeyboardArrowDown, contentDescription = null, tint = Color.Gray)
                            }
                        }
                    }
                }

                if (showCategoryDialog) {
                    AlertDialog(
                        onDismissRequest = { showCategoryDialog = false },
                        title = { Text("Select Category", fontWeight = FontWeight.Bold) },
                        text = {
                            LazyColumn {
                                items(categoryOptions) { option ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable {
                                                category = option
                                                showCategoryDialog = false
                                            }
                                            .padding(vertical = 10.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            painter = painterResource(getCategoryIcon(option)),
                                            contentDescription = option,
                                            modifier = Modifier
                                                .size(30.dp)
                                                .padding(end = 12.dp),
                                            tint = Color(0xFFFF7043)
                                        )
                                        Text(option, fontSize = 16.sp)
                                    }
                                }
                            }
                        },
                        confirmButton = {}
                    )
                }


                Spacer(modifier = Modifier.height(15.dp))
                // Payment Mode Section
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Text("Payment Mode", fontSize = 16.sp, modifier = Modifier.padding(horizontal = 50.dp))
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.money_bag_icon),
                            contentDescription = null,
                            modifier = Modifier.size(35.dp),
                            tint = Color(0xFFFF7043)
                        )
                        Spacer(modifier = Modifier.width(12.dp))

                        val paymentOptions = listOf("Cash", "Online/Upi", "Card")
                        var selectedPaymentIndex by remember { mutableIntStateOf(paymentOptions.indexOf(paymentMode)) }

                        SingleChoiceSegmentedButtonRow(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                        ) {
                            paymentOptions.forEachIndexed { index, label ->
                                SegmentedButton(
                                    shape = SegmentedButtonDefaults.itemShape(index, paymentOptions.size),
                                    onClick = {
                                        selectedPaymentIndex = index
                                        paymentMode = paymentOptions[index]
                                    },
                                    selected = selectedPaymentIndex == index,
                                    label = {Text(label, fontSize = 13.sp)}
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(15.dp))

                // Write a note Section
                Column(modifier = Modifier
                    .fillMaxWidth()
                ) {
                    Text("Writes note", fontSize = 16.sp, modifier = Modifier.padding(horizontal = 66.dp))
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Outlined.Info,
                            contentDescription = "notes",
                            modifier = Modifier.size(30.dp),
                            tint = Color(0xFFFF7043))
                        Spacer(modifier = Modifier.width(12.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(12.dp))
                                .padding(horizontal = 16.dp, vertical = 10.dp)
                        ) {
                            BasicTextField(
                                value = note,
                                onValueChange = {note = it},
                                textStyle = TextStyle.Default,
                                modifier = Modifier.fillMaxWidth()
                                    .height(60.dp),
                                singleLine = true,
                                decorationBox = { innerTextField ->
                                    if (note.text.isEmpty()){
                                        Text(text = "Shorts note",
                                            color = Color.LightGray,
                                            fontSize = 18.sp)
                                    }
                                    innerTextField()
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
//                // Add tags Section
//                Column(modifier = Modifier
//                    .fillMaxWidth()
//                    .clickable{}
//                ) {
//                    Text("Add Tags", fontSize = 16.sp, modifier = Modifier.padding(horizontal = 66.dp))
//                    Spacer(modifier = Modifier.height(8.dp))
//                    Row(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(horizontal = 16.dp),
//                        verticalAlignment = Alignment.CenterVertically
//                    ) {
//                        Icon(Icons.Outlined.Info,
//                            contentDescription = "notes",
//                            modifier = Modifier.size(30.dp),
//                            tint = Color(0xFFFF7043))
//                        Spacer(modifier = Modifier.width(12.dp))
//                        Box(
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(12.dp))
//                                .padding(horizontal = 16.dp, vertical = 10.dp)
//                        ) {
//                            Row(
//                                modifier = Modifier.fillMaxWidth()
//                            ) {
//                                availableTags.forEach { tag ->
//                                    TagChip(
//                                        text = tag,
//                                        isSelected = selectedTags.contains(tag),
//                                        onSelect = {
//                                            selectedTags = if (selectedTags.contains(tag)) {
//                                                selectedTags - tag
//                                            } else {
//                                                selectedTags + tag
//                                            }
//                                        }
//                                    )
//                                    Spacer(modifier = Modifier.width(8.dp))
//                                }
//                            }
//                        }
//                    }
//                }


                Button(onClick = {
                    val transaction = Transaction(
                        amount = amount.text.toDoubleOrNull() ?: 0.0,
                        category = category.ifEmpty { "Others" },
                        description = "Manual entry",
                        note = note.text.ifEmpty { null },
                        type = transactionOptions[selectedIndex],
                        date = date,
                        paymentMode = paymentMode.ifEmpty { "Cash" },
                    )

                    viewModel.addTransaction(transaction)

                    navController.popBackStack()
                },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 25.dp, vertical = 12.dp),
                    enabled = amount.text.isNotEmpty(),
                    shape = RoundedCornerShape(15.dp),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 4.dp,
                        pressedElevation = 8.dp
                    )

                ) {
                    Text("Add Transaction")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTransactionTopBar(
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
            Text("Add Transactions")
        },
        navigationIcon = {
            IconButton(onClick = {navController.popBackStack()}) {
                Icon(Icons.Outlined.ArrowBack,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(start = 16.dp, end = 8.dp)
                        .size(45.dp))
            }
        }
    )
}

@Composable
fun TagChip(
    text: String,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    val backgroundColor = if (isSelected) Color.Black else Color.Transparent
    val textColor = if (isSelected) Color.White else Color.Gray
    val borderColor = if (isSelected) Color.Black else Color.LightGray

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, borderColor, RoundedCornerShape(16.dp))
            .background(backgroundColor)
            .clickable { onSelect() }
            .padding(horizontal = 16.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = textColor,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp
        )
    }
}

fun getCurrentDate(): String {
    val formatter = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
    return formatter.format(Date())
}