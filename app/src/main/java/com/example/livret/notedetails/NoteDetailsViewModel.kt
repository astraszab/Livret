package com.example.livret.notedetails

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.livret.data.Note
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Source
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class NoteDetailsViewModel(
    val noteId: String,
    application: Application
) : AndroidViewModel(application) {
    private val document = Firebase.firestore.collection("notes").document(noteId)
    private val note = MutableLiveData<Note>()
    val noteCategory = MutableLiveData<String>("")
    val noteTitle = Transformations.map(note) { note -> note.title }
    val noteContent = Transformations.map(note) { note -> note.content }
    var categoriesAvailable = MutableLiveData(listOf<String>())
    var noteLoaded = false

    init {
        loadCategories()
        document.get(Source.CACHE)
            .addOnSuccessListener { document ->
                if (document != null) {
                    Log.d("NoteDetailsViewModel", "DocumentSnapshot data: ${document.data}")
                    note.value = document.toObject<Note>()
                    noteCategory.value = note.value?.category
                    noteLoaded = true
                } else {
                    Log.d("NoteDetailsViewModel", "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d("NoteDetailsViewModel", "get failed with ", exception)
            }
    }

    private fun loadCategories() {
        val user = FirebaseAuth.getInstance().currentUser
        Firebase.firestore.collection("notes")
            .whereEqualTo("ownerUID", user?.uid).get(Source.CACHE)
            .addOnSuccessListener { result ->
                val resCategories = mutableSetOf("Home", "Work", "Other")
                result.forEach {
                    resCategories.add(it["category"].toString())
                }
                resCategories.add("Add custom...")
                categoriesAvailable.value = resCategories.toList()
            }
    }

    fun onUpdateNote(note: Note) {
        document.set(note)
    }

    fun onDeleteNote() {
        document.delete()
    }

    fun getNoteCategoryId(): Int {
        val categoryIndex = categoriesAvailable.value!!.indexOf(noteCategory.value)
        if (categoryIndex == -1) return 0
        return categoryIndex
    }
}