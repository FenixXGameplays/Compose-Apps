package com.example.noteapp.utils

import androidx.room.TypeConverters
import java.util.Date

class DateConverter {

    @TypeConverters
    fun timeStampFromDate(date: Date): Long {
        return date.time
    }

    @TypeConverters
    fun dateFromTimeStamp(timestamp: Long): Date {
        return Date(timestamp)
    }

}