package com.example.livret.notedetails

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.livret.data.NoteDatabaseDao
import java.lang.IllegalArgumentException

class NoteDetailsViewModelFactory(
    private val dataSource: NoteDatabaseDao,
    private val noteId: Long,
    private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NoteDetailsViewModel::class.java)) {
            return NoteDetailsViewModel(dataSource, noteId, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}