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

class NotesViewModel(application: Application) : AndroidViewModel(application) {
    private val collection = Firebase.firestore.collection("notes")
    val notes = MutableLiveData<List<Note>>()

    init {
        updateNotesList()
        collection.addSnapshotListener(MetadataChanges.INCLUDE) { _, _ ->
            updateNotesList() }
    }

    private fun updateNotesList() {
        val user = FirebaseAuth.getInstance().currentUser
        Log.w("updateNotesList", "${user?.displayName}")
        collection.whereEqualTo("ownerUID", user?.uid).get()
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