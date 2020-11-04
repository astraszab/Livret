package com.example.livret.notedetails

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
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
    lateinit var binding: FragmentNoteDetailsBinding
    val args: NoteDetailsFragmentArgs by navArgs()
    var noteDeleted = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_note_details, container, false
        )

        val noteId = args.noteId
        val application = requireNotNull(this.activity).application

        val viewModelFactory = NoteDetailsViewModelFactory(noteId, application)
        val noteDetailsViewModel =
            ViewModelProvider(this, viewModelFactory).get(NoteDetailsViewModel::class.java)

        binding.setLifecycleOwner(this)
        binding.noteDetailsViewModel = noteDetailsViewModel

        binding.buttonDelete.setOnClickListener { _ -> onDeleteNote() }

        noteDetailsViewModel.categoriesAvailable.observe(viewLifecycleOwner, {
            val categoryAdapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item,
                it
            )
            categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.categorySpinner.adapter = categoryAdapter

            if (noteDetailsViewModel.noteLoaded) {
                binding.categorySpinner.setSelection(noteDetailsViewModel.getNoteCategoryId())
            }

            noteDetailsViewModel.noteCategory.observe(viewLifecycleOwner, {
                binding.categorySpinner.setSelection(noteDetailsViewModel.getNoteCategoryId())
            })
        })

        setupAddNewCategoryListener()

        return binding.root
    }

    override fun onStop() {
        super.onStop()
        if (!noteDeleted) {
            updateNote()
        }
    }

    private fun updateNote() {
        val note = binding.noteDetailsViewModel?.getNote()
        if (note != null) {
            if (binding.editTextAddCategory.isVisible) {
                note.category = binding.editTextAddCategory.text.toString()
            } else {
                note.category = binding.categorySpinner.getSelectedItem().toString()
            }
            note.title = binding.editNoteTitle.text.toString()
            note.content = binding.editNoteTextContent.text.toString()
            binding.noteDetailsViewModel?.onUpdateNote(note)
        }
    }

    private fun onDeleteNote() {
        binding.noteDetailsViewModel?.onDeleteNote()
        noteDeleted = true
        getActivity()?.onBackPressed()
    }

    private fun setupAddNewCategoryListener() {
        binding.categorySpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {}

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    if (binding.categorySpinner.getSelectedItem().toString() == "Add custom...") {
                        binding.editTextAddCategory.visibility = View.VISIBLE
                    } else {
                        binding.editTextAddCategory.visibility = View.GONE
                    }
                }
            }
    }
}