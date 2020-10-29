package com.example.livret.notedetails

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.livret.data.Note
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class NoteDetailsViewModel(
    val noteId: String,
    application: Application
) : AndroidViewModel(application) {
    private val document = Firebase.firestore.collection("notes").document(noteId)
    private val note = MutableLiveData<Note>()
    val noteTitle = Transformations.map(note) { note -> note.title }
    val noteContent = Transformations.map(note) { note -> note.content }

    init {
        document.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    Log.d("NoteDetailsViewModel", "DocumentSnapshot data: ${document.data}")
                    note.value = document.toObject<Note>()
                } else {
                    Log.d("NoteDetailsViewModel", "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d("NoteDetailsViewModel", "get failed with ", exception)
            }
    }

    fun onUpdateNote(note: Note) {
        document.set(note)
    }

    fun onDeleteNote() {
        document.delete()
    }
}