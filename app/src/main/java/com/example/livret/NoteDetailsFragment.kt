package com.example.livret

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.livret.databinding.FragmentNoteDetailsBinding

/**
 * A simple [Fragment] subclass.
 */
class NoteDetailsFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = DataBindingUtil.inflate<FragmentNoteDetailsBinding>(inflater,
                R.layout.fragment_note_details, container, false)
        return binding.root
    }
}