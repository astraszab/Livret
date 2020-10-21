package com.example.livret.notes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.livret.R
import com.example.livret.data.NoteDatabase
import com.example.livret.databinding.FragmentNotesBinding

/**
 * A simple [Fragment] subclass.
 */
class NotesFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = DataBindingUtil.inflate<FragmentNotesBinding>(inflater,
            R.layout.fragment_notes, container, false)

        val application = requireNotNull(this.activity).application
        val dataSource = NoteDatabase.getInstance(application).noteDatabaseDao

        val viewModelFactory = NotesViewModelFactory(dataSource, application)
        val notesViewModel =
            ViewModelProvider(this, viewModelFactory).get(NotesViewModel::class.java)

        binding.setLifecycleOwner(this)
        binding.notesViewModel = notesViewModel

//        binding.fabAddNote.setOnClickListener{ view: View ->
//            view.findNavController().navigate(R.id.action_notesFragment_to_noteDetailsFragment)
//        }
        return binding.root
    }
}