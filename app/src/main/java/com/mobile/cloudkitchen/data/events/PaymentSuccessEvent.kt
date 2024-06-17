package com.mobile.cloudkitchen.data.events

import com.razorpay.PaymentData

//data class PaymentSuccessEvent(val razorpay_payment_id:String,val razorpay_order_id:String,val razorpay_signature:String) {
data class PaymentSuccessEvent(val data: PaymentData) {


}