package com.example.noteapp

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.noteapp.data.NotesDataSource
import com.example.noteapp.model.Note
import com.example.noteapp.screens.NoteScreen
import com.example.noteapp.ui.theme.NoteAppTheme
import com.example.noteapp.viewmodel.NoteViewModel

class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NoteAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) {
                    val notesViewModel: NoteViewModel by viewModels()
                    NotesApp(notesViewModel)
                }
            }
        }
    }
}


@Composable
fun NotesApp(notesViewModel: NoteViewModel = viewModel()) {
    NoteScreen(
        noteList = notesViewModel.getAllNotes(),
        onRemoveNote = {
            notesViewModel.removeNote(it)
        },
        onAddNote = {
            notesViewModel.addNote(it)
        }
    )
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    NoteAppTheme {

    }
}