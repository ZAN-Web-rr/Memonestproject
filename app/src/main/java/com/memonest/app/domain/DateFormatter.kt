package com.memonest.app.domain

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateFormatter {
    private val displayFormat = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())

    fun format(timestamp: Long): String {
        return displayFormat.format(Date(timestamp))
    }
}
