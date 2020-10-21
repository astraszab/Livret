package com.example.livret

import android.os.Build
import android.text.Html
import android.text.Spanned
import androidx.core.text.HtmlCompat
import com.example.livret.data.Note
import java.lang.StringBuilder

fun formatNotes(notes: List<Note>): Spanned {
    val sb = StringBuilder()
    sb.apply {
        notes.forEach {
            append("<br>")
            append("${it.noteId.toString()}. ")
            append("<b>${it.title}</b>")
            append(" ${it.content}")
        }
    }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        return Html.fromHtml(sb.toString(), Html.FROM_HTML_MODE_LEGACY)
    } else {
        return HtmlCompat.fromHtml(sb.toString(), HtmlCompat.FROM_HTML_MODE_LEGACY)
    }
}