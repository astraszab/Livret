package com.example.livret.notes

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.livret.MainActivity
import com.example.livret.R
import com.example.livret.data.Note
import com.example.livret.databinding.FragmentNotesBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class NotesFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentNotesBinding>(
            inflater,
            R.layout.fragment_notes, container, false
        )

        val application = requireNotNull(this.activity).application

        val viewModelFactory = NotesViewModelFactory(application)
        val notesViewModel =
            ViewModelProvider(this, viewModelFactory).get(NotesViewModel::class.java)

        binding.setLifecycleOwner(this)
        binding.notesViewModel = notesViewModel

        binding.fabAddNote.setOnClickListener { view: View -> onAddingNote(view) }
        binding.fabScrollUp.setOnClickListener { _ ->
            binding.notesList.smoothScrollToPosition(0)
        }

        val adapter = NoteAdapter(NoteListener { noteId ->
            onClickingNote(noteId)
        })
        binding.notesList.adapter = adapter

        notesViewModel.notes.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        val liveUID: MutableLiveData<String?> = (getActivity() as MainActivity).userId
        liveUID.observe(viewLifecycleOwner, Observer { notesViewModel.updateNotesList() })

        notesViewModel.availableCategories.observe(viewLifecycleOwner, Observer {
            it?.let {
                val categoryAdapter = ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_spinner_item,
                    it
                )
                categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.categoryFilter.adapter = categoryAdapter
            }
        })

        binding.searchString.addTextChangedListener { text ->
            notesViewModel.setSearchQuery(text.toString())
        }

        binding.categoryFilter.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {}

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    (parent?.getChildAt(0) as TextView?)
                        ?.setTextColor(
                            getResources()
                                .getColor(R.color.secondaryTextColor)
                        )
                    notesViewModel.setCategoryFilter(
                        binding.categoryFilter.getSelectedItem().toString()
                    )
                }
            }

        return binding.root
    }

    private fun onAddingNote(view: View) {
        val user = FirebaseAuth.getInstance().currentUser
        val document = Firebase.firestore.collection("notes").document()
        val note = Note(
            document.id, user?.uid,
            "",
            "",
            "Home"
        )

        document.set(note)
            .addOnSuccessListener {
                val action = NotesFragmentDirections
                    .actionNotesFragmentToNoteDetailsFragment(note.noteId!!)
                view.findNavController().navigate(action)
            }
            .addOnFailureListener { e ->
                Log.w("NotesFragment", "Error adding document", e)
            }
    }

    private fun onClickingNote(noteId: String?) {
        val action = NotesFragmentDirections.actionNotesFragmentToNoteDetailsFragment(noteId!!)
        getView()?.findNavController()?.navigate(action)
    }
}