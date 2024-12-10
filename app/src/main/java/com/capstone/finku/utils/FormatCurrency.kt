package com.capstone.finku.utils

import java.text.NumberFormat
import java.util.Locale

fun formatToRupiah(amount: Int?): String {
    val formatter = NumberFormat.getCurrencyInstance(Locale("in", "ID"))
    formatter.maximumFractionDigits = 0 // No decimal places
    return if (amount != null) formatter.format(amount).replace("Rp", "Rp ") else "Rp 0"
}