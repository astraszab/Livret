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
    private var notesBuffer: List<Note>? = null
    val availableCategories = MutableLiveData<List<String>>()
    private var searchQuery = ""
    private var categoryFilter = "All"

    init {
        updateNotesList()
        collection.addSnapshotListener(MetadataChanges.INCLUDE) { _, _ ->
            updateNotesList()
        }
    }

    fun updateNotesList() {
        val user = FirebaseAuth.getInstance().currentUser
        Log.w("updateNotesList", "${user?.displayName}")
        collection.whereEqualTo("ownerUID", user?.uid).get()
            .addOnSuccessListener { result ->
                notesBuffer = result.toObjects(Note::class.java)
                updateAvailableCategories()
                updateNotesListView()
            }
    }

    private fun updateNotesListView() {
        var resNotes: List<Note>?
        if (searchQuery.isEmpty()) {
            resNotes = notesBuffer
        } else {
            resNotes = notesBuffer?.filter {
                it.title.toLowerCase(Locale.getDefault())
                    .contains(searchQuery.toLowerCase(Locale.getDefault()))
            }
        }
        if (categoryFilter != "All") {
            resNotes = resNotes?.filter {
                it.category == categoryFilter
            }
        }
        notes.value = resNotes
    }

    private fun updateAvailableCategories() {
        val resCategories = mutableSetOf("All")
        notesBuffer?.forEach { resCategories.add(it.category) }
        availableCategories.value = resCategories.toList()
    }

    fun onClear() {
        val noteList = notes.value
        noteList?.forEach {
            collection.document(it.noteId!!).delete()
        }
        updateNotesList()
    }

    fun setSearchQuery(value: String) {
        searchQuery = value
        updateNotesListView()
    }

    fun setCategoryFilter(value: String) {
        categoryFilter = value
        updateNotesListView()
    }
}