package com.example.locationproject.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

object DateTime {
    @SuppressLint("SimpleDateFormat")
    fun getTime(milliSeconds: Long): String {
        val format = SimpleDateFormat("dd/MM/yyyy hh:mm:ss")
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = milliSeconds
        return format.format(calendar.time)
    }
}