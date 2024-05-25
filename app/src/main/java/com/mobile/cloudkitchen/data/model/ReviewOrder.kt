package com.mobile.cloudkitchen.data.model

data class ReviewOrder(
    val discount: String,
    val grandTotal: Int,
    val plannedDates: List<String>,
    val savedAmount: Int,
    val totalAmount: Int
)