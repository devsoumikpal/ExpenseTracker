package com.example.expensetracker.utils

import com.example.expensetracker.R


fun getCategoryIcon(categoryName: String): Int {
    return when (categoryName) {
        "Food", "Fruits & vegetables", "Groceries", "Snacks" -> R.drawable.fastfood_icon
        "Entertainment", "Netflix Subscription" -> R.drawable.entertainment_icon
        "Transportation", "Olu Cab to Office", "Car" -> R.drawable.transport_icon
        "Shopping" -> R.drawable.shopping_cart_icon
        "Telephone" -> R.drawable.telephone_icon
        "Education" -> R.drawable.education_icon
        "Beauty" -> R.drawable.beauty_icon
        "Sport" -> R.drawable.sports_icon
        "Social" -> R.drawable.social_icon
        "Clothing" -> R.drawable.clothing_icon
        "Cigarette" -> R.drawable.cigarette_icon
        "Electronics" -> R.drawable.electronics_icon
        "Travel" -> R.drawable.travel_icon
        "Health" -> R.drawable.health_icon
        "Pet" -> R.drawable.pet_icon
        "Repair" -> R.drawable.repair_icon
        "Housing", "Home" -> R.drawable.home_icon
        "Gift" -> R.drawable.gift_icon
        "Donate" -> R.drawable.donate_icon
        "Baby" -> R.drawable.baby_icon
        "Vegetable", "Fruit" -> R.drawable.vegetables_icon
        "Salary", "Bonus" -> R.drawable.salary_icon
        "Investment" -> R.drawable.invest_icon
        else -> R.drawable.budget_icon
    }
}