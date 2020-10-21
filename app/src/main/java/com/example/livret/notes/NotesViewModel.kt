package com.example.livret.notes

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import com.example.livret.data.Note
import com.example.livret.data.NoteDatabaseDao
import com.example.livret.formatNotes
import kotlinx.coroutines.launch

class NotesViewModel(
    val database: NoteDatabaseDao,
    application: Application) : AndroidViewModel(application) {

    private val notes = database.getAllNotes()
    val notesString = Transformations.map(notes) { notes -> formatNotes(notes) }

    fun onAddingNote() {
        viewModelScope.launch {
            val newNote = Note()
            insert(newNote)
        }
    }

    private suspend fun insert(note: Note) {
        database.insert(note)
    }
}