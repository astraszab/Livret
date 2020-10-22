package com.example.livret.notedetails

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import com.example.livret.data.Note
import com.example.livret.data.NoteDatabaseDao
import kotlinx.coroutines.launch

class NoteDetailsViewModel(
    val database: NoteDatabaseDao,
    val noteId: Long,
    application: Application
) : AndroidViewModel(application) {
    private val note = database.getLive(noteId)
    val noteTitle = Transformations.map(note) { note -> note.title }
    val noteContent = Transformations.map(note) { note -> note.content }

    fun onUpdateNote(note: Note) {
        viewModelScope.launch {
            note.noteId = noteId
            database.update(note)
        }
    }

    suspend fun update(note: Note) {
        database.update(note)
    }
}