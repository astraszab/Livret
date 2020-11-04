package com.example.livret.notedetails

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.livret.R
import com.example.livret.data.Note
import com.example.livret.databinding.FragmentNoteDetailsBinding
import com.google.firebase.auth.FirebaseAuth

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

        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.categories_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.categorySpinner.adapter = adapter
        }

        noteDetailsViewModel.noteCategory.observe(viewLifecycleOwner, {
            binding.categorySpinner.setSelection(noteDetailsViewModel.getNoteCategoryId());
        })

        return binding.root
    }

    override fun onStop() {
        super.onStop()
        val user = FirebaseAuth.getInstance().currentUser
        if (!noteDeleted) {
            val note = Note(
                args.noteId,
                user?.uid,
                binding.editNoteTitle.text.toString(),
                binding.editNoteTextContent.text.toString(),
                binding.categorySpinner.getSelectedItem().toString()
            )
            binding.noteDetailsViewModel?.onUpdateNote(note)
        }
    }

    fun onDeleteNote() {
        binding.noteDetailsViewModel?.onDeleteNote()
        noteDeleted = true
        getActivity()?.onBackPressed()
    }
}