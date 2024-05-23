package com.mobile.cloudkitchen.data.model

data class ProcessOrder(
    val discount: String,
    val grandTotal: Int,
    val plannedDates: List<String>,
    val savedAmount: Int,
    val totalAmount: Int
)