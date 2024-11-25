package com.example.unsripay.utils

import java.text.NumberFormat
import java.util.Locale

object CurrencyUtils {
    fun formatRupiah(amount: Int): String {
        val localeID = Locale("in", "ID") // Locale untuk Indonesia
        val formatter = NumberFormat.getCurrencyInstance(localeID)
        return formatter.format(amount).replace("Rp", "Rp ") // Menambahkan spasi setelah "Rp"
    }
}
