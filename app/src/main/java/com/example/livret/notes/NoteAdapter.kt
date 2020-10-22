package com.example.livret.notes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.livret.R
import com.example.livret.data.Note

class NoteAdapter: RecyclerView.Adapter<NoteAdapter.ViewHolder>() {
    var data = listOf<Note>()
    set(value) {
        field = value
        notifyDataSetChanged()
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(itemView: View) : RecyclerView.ViewHolder(itemView){
        val noteTitle : TextView = itemView.findViewById(R.id.note_title)
        val noteContent : TextView = itemView.findViewById(R.id.note_content)

        fun bind(item: Note) {
            noteTitle.text = item.title
            noteContent.text = item.content
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater
                    .inflate(R.layout.list_item_note, parent, false)
                return ViewHolder(view)
            }
        }
    }

}