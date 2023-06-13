package com.sarikaya.kotlinbasicnoteapp.view

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sarikaya.kotlinbasicnoteapp.R
import com.sarikaya.kotlinbasicnoteapp.adapter.NotesRecyclerViewAdapter
import com.sarikaya.kotlinbasicnoteapp.databinding.FragmentNotesPageBinding
import com.sarikaya.kotlinbasicnoteapp.model.NoteDataList
import com.sarikaya.kotlinbasicnoteapp.swipemenu.MenuButton
import com.sarikaya.kotlinbasicnoteapp.swipemenu.RecyclerItemClickListener
import com.sarikaya.kotlinbasicnoteapp.swipemenu.SwipeMenuHelper
import com.sarikaya.kotlinbasicnoteapp.viewmodel.NotesPageFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.properties.Delegates

@AndroidEntryPoint
class NotesPageFragment() : BaseFragment<FragmentNotesPageBinding>() {

    override fun getViewBinding() = FragmentNotesPageBinding.inflate(layoutInflater)
    lateinit var recyclerViewAdapter: NotesRecyclerViewAdapter
    val viewModel: NotesPageFragmentViewModel by viewModels()
    private var pageNo = 1
    var lastPage by Delegates.notNull<Int>()
    private var totalNotesList: MutableList<NoteDataList> = mutableListOf()
    private var deletedItemPosisiton: Int? = null
    private var totalDeletedItemPosisiton: Int? = null
    private var totalItem: Int = 0
    private var isDeleting = false
    private var loading = true


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        totalNotesList.clear()
        deletedItemPosisiton = null
        viewModel.getNotesList(1)
        observeLiveDatas()
        initRecyclerView()
        notesSearchFun()
        navButtons()
        editTextFocusListener()
        cancelBtn()
        swipeMenu()
    }

    private fun swipeMenu() {
        val swipe = object : SwipeMenuHelper(requireContext(), binding.notesRecyclerView, 250) {
            override fun instantiateMenuButton(
                viewHolder: RecyclerView.ViewHolder,
                buffer: MutableList<MenuButton>
            ) {
                buffer.add(
                    MenuButton(
                        requireContext(),
                        R.drawable.ic_trash,
                        Color.parseColor("#DD2C00"),
                        object : RecyclerItemClickListener {
                            override fun onClick(pos: Int) {
                                isDeleting = true
                                if (totalNotesList == recyclerViewAdapter.searchableList) {
                                    viewModel.deleteNote(pos, totalNotesList)
                                    deletedItemPosisiton = pos
                                    totalDeletedItemPosisiton = pos
                                } else {
                                    deletedItemPosisiton = pos
                                    for (i in 0..totalNotesList.size - 1) {
                                        if (totalNotesList.get(i).id == recyclerViewAdapter.searchableList.get(
                                                pos
                                            ).id) {
                                            totalDeletedItemPosisiton = i
                                        }
                                    }
                                    viewModel.deleteNote(
                                        pos,
                                        recyclerViewAdapter.searchableList
                                    )
                                }
                            }
                        }
                    )
                )
                buffer.add(
                    MenuButton(
                        requireContext(),
                        R.drawable.ic_edit,
                        Color.parseColor("#FFD164"),
                        object : RecyclerItemClickListener {
                            override fun onClick(pos: Int) {
                                val action = NotesPageFragmentDirections
                                    .actionNotesPageFragmentToNoteDetailFragment(
                                        recyclerViewAdapter.searchableList.get(pos)?.id.toString()
                                    )
                                view?.let { Navigation.findNavController(it).navigate(action) }
                            }
                        }
                    )
                )
            }
        }
    }

    private fun navButtons() {
        binding.menuView.setOnClickListener {
            Navigation.findNavController(requireView()).navigate(
                R.id.action_notesPageFragment_to_profilePageFragment
            )
        }
        binding.addNoteButton.setOnClickListener {
            val action = NotesPageFragmentDirections.actionNotesPageFragmentToNoteDetailFragment(
                "0"
            )
            Navigation.findNavController(requireView()).navigate(action)
        }
    }


    private fun initRecyclerView() {
        val layoutManager: LinearLayoutManager
        layoutManager = LinearLayoutManager(this.context)
        binding.notesRecyclerView.layoutManager = layoutManager
        recyclerViewAdapter = NotesRecyclerViewAdapter()
        binding.notesRecyclerView.adapter = recyclerViewAdapter
        var lastVisibleItem: Int
        var totalItemCount: Int
        binding.notesRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0) {
                    totalItemCount = layoutManager.getItemCount()
                    lastVisibleItem = layoutManager.findLastCompletelyVisibleItemPosition()
                    if (loading && lastVisibleItem == totalItemCount - 1) {
                        loading = false
                        pageNo += 1
                        if (pageNo <= lastPage) {
                            viewModel.getNotesList(pageNo)
                        }
                    }
                }
            }
        })
    }

    private fun observeLiveDatas() {
        viewModel.notesLiveData.observe(viewLifecycleOwner) { notesLiveData ->
            if (notesLiveData != null) {
                this.totalItem = notesLiveData.allNotesData.totalItemCount
                this.lastPage = notesLiveData.allNotesData.lastPage
                if (totalNotesList.size == 0 || (totalNotesList.size < totalItem && totalNotesList != notesLiveData.allNotesData.notesDataList)) { //TODO: bu logic düzeltilecek
                    totalNotesList.addAll(notesLiveData.allNotesData.notesDataList)
                }
                loading = true
                recyclerViewAdapter.setList(totalNotesList)
            }
        }
        viewModel.deleteNoteLiveData.observe(viewLifecycleOwner) { deleteNote ->
            if (deleteNote != null) {
                if (isDeleting) {
                    deletedItemPosisiton?.let { recyclerViewAdapter.deleteItem(it) }
                    if (totalDeletedItemPosisiton != deletedItemPosisiton) {
                        totalDeletedItemPosisiton?.let { totalNotesList.removeAt(it) }
                    } else {
                        deletedItemPosisiton?.let { totalNotesList.removeAt(it) }
                    }
                    this.totalItem = totalNotesList.size
                    Toast.makeText(context, "Note deleted!", Toast.LENGTH_LONG).show()
                    isDeleting = false
                }
            }
        }
        viewModel.errorLiveData.observe(viewLifecycleOwner) { error ->
            if (error != null) {
                Toast.makeText(context, error.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun notesSearchFun() {
        binding.searcheditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {} //

            override fun onTextChanged(charSeq: CharSequence?, p1: Int, p2: Int, p3: Int) {
                recyclerViewAdapter.filter.filter(charSeq.toString())
            }

            override fun afterTextChanged(p0: Editable?) {} //
        })
    }

    private fun cancelBtn() {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        binding.cancelButton.setOnClickListener {
            binding.searcheditText.text.clear()
            imm.hideSoftInputFromWindow(view!!.getWindowToken(), 0)
            binding.searcheditText.clearFocus()
            binding.noteParent.requestFocus()
            editTextFocusListener()
        }
    }

    private fun editTextFocusListener() {
        binding.searcheditText.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                binding.cancelButton.visibility = View.GONE
                binding.searcheditText.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.ic_search_hint,
                    0,
                    0,
                    0
                )
            } else {
                binding.cancelButton.visibility = View.VISIBLE
                binding.searcheditText.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.ic_search,
                    0,
                    0,
                    0
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        totalNotesList.clear()
        pageNo = 1
        totalItem = 0
    }
}
