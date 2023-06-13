package com.sarikaya.kotlinbasicnoteapp.view

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.sarikaya.kotlinbasicnoteapp.R
import com.sarikaya.kotlinbasicnoteapp.databinding.FragmentNoteDetailBinding
import com.sarikaya.kotlinbasicnoteapp.viewmodel.NoteDetailFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NoteDetailFragment : BaseFragment<FragmentNoteDetailBinding>() {

    override fun getViewBinding() = FragmentNoteDetailBinding.inflate(layoutInflater)
    val viewModel: NoteDetailFragmentViewModel by viewModels()
    private val noteArgs: NoteDetailFragmentArgs by navArgs()
    private var haveArgs = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        haveArgsListener()
        observeLiveData()
        navBackBtn()
        setButtonListener()
    }

    private fun navBackBtn() {
        binding.backButton.setOnClickListener {
            Navigation.findNavController(requireView())
                .navigate(R.id.action_noteDetailFragment_to_notesPageFragment)
        }
    }

    private fun haveArgsListener() {
        haveArgs = noteArgs.id != null && noteArgs.id != "0"

        if (haveArgs) {
            viewModel.getNote(noteArgs.id.toString())
        }
    }

    private fun setButtonListener() {
        binding.editNoteButton.setOnClickListener {
            if (haveArgs) {
                if ((binding.editNoteTitle.text.toString() != "") && (binding.editNoteNote.text.toString() != "")) {
                    viewModel.putNote(
                        noteArgs.id.toString(),
                        binding.editNoteTitle.text.toString(),
                        binding.editNoteNote.text.toString()
                    )
                } else {
                    Toast.makeText(context, "Error.", Toast.LENGTH_LONG).show()
                }
            } else {
                if ((binding.editNoteTitle.text.toString() != "") && (binding.editNoteNote.text.toString() != "")) {
                    viewModel.addNote(
                        binding.editNoteTitle.text.toString(),
                        binding.editNoteNote.text.toString()
                    )
                } else {
                    Toast.makeText(context, "Error.", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun observeLiveData() {
        viewModel.addNoteLiveData.observe(viewLifecycleOwner) { addNoteData ->
            if (addNoteData != null) {
                Navigation.findNavController(requireView())
                    .navigate(R.id.action_noteDetailFragment_to_notesPageFragment)
            }
        }
        viewModel.getNoteLiveData.observe(viewLifecycleOwner) { getNote ->
            if (getNote != null) {
                binding.editNoteTitle.setText(getNote.data.title)
                binding.editNoteNote.setText(getNote.data.note)
            }
        }
        viewModel.putNoteLiveData.observe(viewLifecycleOwner) { addNoteData ->
            if (addNoteData != null) {
                Navigation.findNavController(requireView())
                    .navigate(R.id.action_noteDetailFragment_to_notesPageFragment)
            }
        }
        viewModel.errorLiveData.observe(viewLifecycleOwner) { error ->
            if (error != null) {
                Toast.makeText(context, error.message, Toast.LENGTH_LONG).show()
            }
        }
    }
}
