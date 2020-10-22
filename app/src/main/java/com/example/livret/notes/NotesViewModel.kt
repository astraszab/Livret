package com.example.livret.notes

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import com.example.livret.data.Note
import com.example.livret.data.NoteDatabaseDao
import com.example.livret.formatNotes
import com.example.livret.notedetails.NoteDetailsFragment
import kotlinx.coroutines.launch

class NotesViewModel(
    val database: NoteDatabaseDao,
    application: Application) : AndroidViewModel(application) {

    private val notes = database.getAllNotes()
    val notesString = Transformations.map(notes) { notes -> formatNotes(notes) }

    suspend fun onAddingNote(): Long {
        var newNote : Note? = null
        val job = viewModelScope.launch {
            insert(Note())
            newNote = getLastAdded()
        }
        job.join()
        return newNote!!.noteId
    }

    private suspend fun insert(note: Note) {
        database.insert(note)
    }

    private suspend fun getLastAdded(): Note? {
        return database.getLastAdded()
    }

    fun onClear() {
        viewModelScope.launch {
            clear()
        }
    }

    suspend fun clear() {
        database.clear()
    }
}