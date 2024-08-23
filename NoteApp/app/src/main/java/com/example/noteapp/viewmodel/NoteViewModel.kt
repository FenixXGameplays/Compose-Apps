package com.example.noteapp.viewmodel

import android.widget.Toast
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.noteapp.data.NotesDataSource
import com.example.noteapp.model.Note

class NoteViewModel: ViewModel() {
    var noteList = mutableStateListOf<Note>()


    fun addNote(note: Note){
        noteList.add(note)
    }

    fun removeNote(note: Note){
        noteList.remove(note)
    }

    fun getAllNotes(): List<Note>{
        return noteList
    }

}