package com.example.noteapp.data

import com.example.noteapp.model.Note

class NotesDataSource {
    fun loadNotes(): List<Note>{
        return listOf(
            Note(title = "1", description = "Uno"),
            Note(title = "2", description = "Dos"),
            Note(title = "3", description = "Tres"),
            Note(title = "4", description = "Cuatro"),
            Note(title = "5", description = "Cinco"),
            Note(title = "6", description = "Seis"),
            Note(title = "7", description = "Siete"),
            Note(title = "8", description = "Ocho"),
            Note(title = "9", description = "Nueve"),
            Note(title = "10", description = "Diez"),
            Note(title = "11", description = "Once"),
            Note(title = "12", description = "Doce"),
        )
    }
}