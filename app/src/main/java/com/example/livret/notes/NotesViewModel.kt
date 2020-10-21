package com.example.livret.notes

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.livret.data.NoteDatabaseDao

class NotesViewModel(
    val database: NoteDatabaseDao,
    application: Application) : AndroidViewModel(application) {
}