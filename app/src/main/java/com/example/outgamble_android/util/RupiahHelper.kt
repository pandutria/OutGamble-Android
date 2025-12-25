package com.example.outgamble_android.util

import java.text.NumberFormat
import java.util.Locale

object RupiahHelper {
    fun format(num: Int): String {
        val formatter = NumberFormat.getNumberInstance(Locale("id", "ID"))
        return formatter.format(num)
    }
}