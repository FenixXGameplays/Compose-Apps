package com.example.jettip.utils

fun calculateTipTotal(totalBill: Double, tipPercentage: Double): Double {
    val totalNoFormatted = if( totalBill > 1 && totalBill.toString().isNotEmpty())
        totalBill * tipPercentage else 0.0

    val total = "%.2f".format(totalNoFormatted)
    return total.toDouble()
}

fun calculateTotalPerPerson(totalBill: Double, splitBy: Int, tipPercentage: Double): Double {
    val bill = calculateTipTotal(totalBill, (tipPercentage / 100)) + totalBill
    return bill / splitBy
}