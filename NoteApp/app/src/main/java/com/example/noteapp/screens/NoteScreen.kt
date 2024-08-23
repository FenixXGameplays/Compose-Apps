package com.example.noteapp.screens

import android.widget.Space
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.noteapp.R
import com.example.noteapp.components.NoteButton
import com.example.noteapp.components.NoteInputText
import com.example.noteapp.data.NotesDataSource
import com.example.noteapp.model.Note
import com.example.noteapp.viewmodel.NoteViewModel
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteScreen(
    noteList: List<Note>,
    onAddNote: (Note) -> Unit = {
    },
    onRemoveNote: (Note) -> Unit = {}
) {
    val valueNote = remember {
        mutableStateOf("")
    }
    var valueDescription by remember {
        mutableStateOf("")
    }

    val context = LocalContext.current
    Column(modifier = Modifier.padding(6.dp)) {
        TopAppBar(title = {
            Text(text = stringResource(id = R.string.app_name))
        },
            actions = {
                Icon(imageVector = Icons.Rounded.Notifications, contentDescription = "Notification")
            })
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally) {
            NoteInputText(
                modifier = Modifier.padding(top = 9.dp, bottom = 9.dp),
                text = valueNote.value,
                label = "Title",
                onTextChange = { newValue ->
                    if(newValue.all {
                        it.isLetter() || it.isWhitespace()
                    }){
                        valueNote.value = newValue
                    }
                }
            )

            NoteInputText(
                modifier = Modifier.padding(top = 9.dp, bottom = 9.dp),
                text = valueDescription,
                label = "Description",
                onTextChange = { newValue ->
                    if(newValue.all {
                            it.isLetter() || it.isWhitespace()
                        }){
                        valueDescription = newValue
                    }
                }
            )


            NoteButton(text = "Save", onClick = {
                if(valueNote.value.isNotEmpty() && valueDescription.isNotEmpty()){
                    onAddNote(Note(title = valueNote.value, description = valueDescription))
                    valueNote.value = ""
                    valueDescription = ""
                    Toast.makeText(context, "Note Added", Toast.LENGTH_SHORT).show()
                }
            })


        }
        Spacer(modifier = Modifier.height(16.dp))

        HorizontalDivider()

        Spacer(modifier = Modifier.height(16.dp))

        if(noteList.isNotEmpty()){
            LazyColumn {
                items(noteList) { note ->

                    NoteRow(note = note, onNoteClicked = {
                        onRemoveNote(note)
                    })
                }
            }
        }else{
            Text(modifier = Modifier.padding(16.dp), text = "No Notes")
        }
    }

}

@Composable
fun NoteRow(
    modifier: Modifier = Modifier,
    note: Note,
    onNoteClicked: (Note) -> Unit,
    ){
    Surface(
        modifier
            .padding(4.dp)
            .clip(
                RoundedCornerShape(
                    topEnd = 33.dp,
                    bottomStart = 33.dp
                )
            )
            .fillMaxWidth(),
        color = Color.Gray.copy(alpha = 0.5f),

        ) {
        Column (
            modifier
                .clickable { onNoteClicked(note) }
                .padding(horizontal = 14.dp, vertical = 6.dp),
            horizontalAlignment = Alignment.Start){
            Text(text = note.title,
                style = androidx.compose.material3.MaterialTheme.typography.titleMedium
            )
            Text(text = note.description,
                style = androidx.compose.material3.MaterialTheme.typography.bodyMedium
            )
            Text(text = note.entryDate.format(DateTimeFormatter.ofPattern("EEE, d MMM")),
                style = androidx.compose.material3.MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NotesScreenPreview(){
    NoteScreen(noteList = NotesDataSource().loadNotes(),onAddNote= {}, onRemoveNote = {})
}