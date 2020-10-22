package com.example.livret.notedetails

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.livret.R
import com.example.livret.data.Note
import com.example.livret.data.NoteDatabase
import com.example.livret.databinding.FragmentNoteDetailsBinding
import com.example.livret.notes.NotesViewModel

/**
 * A simple [Fragment] subclass.
 */
class NoteDetailsFragment : Fragment() {
    val args: NoteDetailsFragmentArgs by navArgs()
    var binding : FragmentNoteDetailsBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_note_details, container, false)

        val noteId = args.noteId
        val application = requireNotNull(this.activity).application
        val dataSource = NoteDatabase.getInstance(application).noteDatabaseDao

        val viewModelFactory = NoteDetailsViewModelFactory(dataSource, noteId, application)
        val noteDetailsViewModel =
            ViewModelProvider(this, viewModelFactory).get(NoteDetailsViewModel::class.java)

        binding?.setLifecycleOwner(this)
        binding?.noteDetailsViewModel = noteDetailsViewModel

        return binding!!.root
    }

    override fun onStop() {
        super.onStop()
        val note = Note()
        note.title = binding!!.editNoteTitle.text.toString()
        note.content = binding!!.editNoteTextContent.text.toString()
        binding!!.noteDetailsViewModel!!.onUpdateNote(note)
    }
}