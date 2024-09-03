package com.example.noteapp.viewmodel

import android.widget.Toast
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.noteapp.data.NotesDataSource
import com.example.noteapp.model.Note
import com.example.noteapp.repository.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteViewModel(private val repository: NoteRepository): ViewModel() {

    private val _noteList = MutableStateFlow<List<Note>>(emptyList())
    val noteList = _noteList.asStateFlow()
    //    var noteList = repository.getAllNotes()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getAllNotes()
                .distinctUntilChanged()
                .collect{ listNotes ->
                    if(!listNotes.isNullOrEmpty()){
                        _noteList.value = listNotes
                    }else{
                        _noteList.value = emptyList()
                    }
                }
        }
    }

     fun addNote(note: Note) = viewModelScope.launch { repository.addNote(note) }
     fun removeNote(note: Note) = viewModelScope.launch { repository.deleteNote(note) }
     fun updateNote(note: Note) = viewModelScope.launch { repository.update(note) }

    fun getAllNotes(): Flow<List<Note>> {
        return repository.getAllNotes()
    }

}