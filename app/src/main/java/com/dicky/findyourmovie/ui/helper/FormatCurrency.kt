package com.dicky.findyourmovie.ui.helper

import java.text.NumberFormat
import java.util.*

object FormatCurrency {
    fun formatCurrencyUS(number: Long): String{
        val localeUS = Locale("us", "US")
        val dollarFormat = NumberFormat.getCurrencyInstance(localeUS)
        return dollarFormat.format(number)
    }
}