package com.example.livret.notes

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.livret.data.Note
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.MetadataChanges
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*

class NotesViewModel(application: Application) : AndroidViewModel(application) {
    private val collection = Firebase.firestore.collection("notes")
    val notes = MutableLiveData<List<Note>>()
    var notesBuffer : List<Note>? = null
    private var searchQuery : String = ""

    init {
        updateNotesList()
        collection.addSnapshotListener(MetadataChanges.INCLUDE) { _, _ ->
            updateNotesList() }
    }

    fun updateNotesList() {
        val user = FirebaseAuth.getInstance().currentUser
        Log.w("updateNotesList", "${user?.displayName}")
        collection.whereEqualTo("ownerUID", user?.uid).get()
            .addOnSuccessListener { result ->
                if (searchQuery.isEmpty()) {
                    notes.value = result.toObjects(Note::class.java)
                } else {
                    notesBuffer = result.toObjects(Note::class.java)
                    onSearch()
                }
            }
    }

    fun onClear() {
        val noteList = notes.value
        noteList?.forEach {
            collection.document(it.noteId!!).delete()
        }
    }

    fun setSearchQuery(value: String) {
        if (searchQuery.isEmpty() and value.isNotEmpty()) {
            onBeforeSearch()
            searchQuery = value
            onSearch()
        } else if (searchQuery.isNotEmpty() and value.isNotEmpty()) {
            searchQuery = value
            onSearch()
        } else if (searchQuery.isNotEmpty() and value.isEmpty()) {
            searchQuery = value
            onSearchFinished()
        }
    }

    private fun onBeforeSearch() {
        notesBuffer = notes.value
    }

    private fun onSearch() {
        notes.value = notesBuffer?.filter {
            it.title.toLowerCase(Locale.getDefault())
                .contains(searchQuery.toLowerCase(Locale.getDefault()))
        }
    }

    private fun onSearchFinished() {
        notes.value = notesBuffer
    }
}