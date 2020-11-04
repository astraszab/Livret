package com.example.livret.notes

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.livret.data.Note

@BindingAdapter("noteTitle")
fun TextView.setNoteTitle(item: Note) {
    text = item.title
}

@BindingAdapter("noteContent")
fun TextView.setNoteContent(item: Note) {
    text = item.content
}

@BindingAdapter("noteCategory")
fun TextView.setNoteCategory(item: Note) {
    text = item.category
}