package com.example.noteapp.utils

import com.example.noteapp.model.Note
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


fun formateDate(time: Long): String{
    val date = Date(time)
    val format = SimpleDateFormat("EEE, d MMM hh:mm aaa", Locale.getDefault())
    return format.format(date)
}