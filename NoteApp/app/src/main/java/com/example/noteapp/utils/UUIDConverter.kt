package com.example.noteapp.utils

import androidx.room.TypeConverters
import java.util.UUID

class UUIDConverter {
    @TypeConverters
    fun fromUUID(uuid: UUID): String {
        return uuid.toString()

    }

    @TypeConverters
    fun toUUID(uuidString: String): UUID {
        return UUID.fromString(uuidString)
    }

}