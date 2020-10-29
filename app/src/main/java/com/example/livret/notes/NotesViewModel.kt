package com.example.livret.notes

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.livret.data.Note
import com.google.firebase.firestore.MetadataChanges
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class NotesViewModel(application: Application) : AndroidViewModel(application) {

    private val collection = Firebase.firestore.collection("notes")
    val notes = MutableLiveData<List<Note>>()

    init {
        updateNotesList()
        collection.addSnapshotListener(MetadataChanges.INCLUDE) { _, _ ->
            updateNotesList() }
    }

    private fun updateNotesList() {
        collection.get()
            .addOnSuccessListener { result ->
                notes.value = result.toObjects(Note::class.java)
            }
    }

    fun onClear() {
        val noteList = notes.value
        noteList?.forEach {
            collection.document(it.noteId!!).delete()
        }
    }
}