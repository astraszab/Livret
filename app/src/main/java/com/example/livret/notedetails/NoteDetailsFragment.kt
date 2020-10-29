package com.example.livret.notedetails

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.livret.R
import com.example.livret.data.NoteF
import com.example.livret.databinding.FragmentNoteDetailsBinding

/**
 * A simple [Fragment] subclass.
 */
class NoteDetailsFragment : Fragment() {
    lateinit var binding : FragmentNoteDetailsBinding
    val args: NoteDetailsFragmentArgs by navArgs()
    var noteDeleted = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_note_details, container, false)

        val noteId = args.noteId
        val application = requireNotNull(this.activity).application

        val viewModelFactory = NoteDetailsViewModelFactory(noteId, application)
        val noteDetailsViewModel =
            ViewModelProvider(this, viewModelFactory).get(NoteDetailsViewModel::class.java)

        binding.setLifecycleOwner(this)
        binding.noteDetailsViewModel = noteDetailsViewModel

        binding.buttonDelete.setOnClickListener { _ -> onDeleteNote() }

        return binding.root
    }

    override fun onStop() {
        super.onStop()
        if (!noteDeleted) {
            val note = NoteF()
            note.title = binding.editNoteTitle.text.toString()
            note.content = binding.editNoteTextContent.text.toString()
            binding.noteDetailsViewModel?.onUpdateNote(note)
        }
    }

    fun onDeleteNote() {
        binding.noteDetailsViewModel?.onDeleteNote()
        noteDeleted = true
        getActivity()?.onBackPressed()
    }
}