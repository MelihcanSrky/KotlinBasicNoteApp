package com.sarikaya.kotlinbasicnoteapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.sarikaya.kotlinbasicnoteapp.R
import com.sarikaya.kotlinbasicnoteapp.databinding.RecyclerListItemBinding
import com.sarikaya.kotlinbasicnoteapp.model.NoteDataList

class NotesRecyclerViewAdapter() : RecyclerView.Adapter<NotesRecyclerViewAdapter.MyHolderView>(), Filterable {

    var noteList: List<NoteDataList> = listOf()
    var searchableList: MutableList<NoteDataList> = arrayListOf()
    private var onNothingFound: (() -> Unit)? = null

    fun setList(data: List<NoteDataList>) {
        noteList = data
        searchableList.clear()
        searchableList.addAll(noteList)
        notifyDataSetChanged()
    }

    class MyHolderView(view: View) : RecyclerView.ViewHolder(view) {
        val binding = RecyclerListItemBinding.bind(view)
    }

    fun deleteItem(position: Int) {
        searchableList.removeAt(position)
        notifyItemRemoved(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesRecyclerViewAdapter.MyHolderView {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.recycler_list_item,
            parent,
            false
        )
        return MyHolderView(view)
    }

    override fun onBindViewHolder(holder: NotesRecyclerViewAdapter.MyHolderView, position: Int) {
        val searhList = searchableList[position]
        with(holder) {
            binding.titleText.text = searhList.title
            binding.noteText.text = searhList.note
        }
    }

    override fun getItemCount(): Int {
        return searchableList.size
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            private val filterResults = FilterResults()
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                searchableList.clear()
                if (constraint.isNullOrBlank()) {
                    noteList.let { searchableList.addAll(it) }
                } else {
                    val filterPattern = constraint.toString().toLowerCase().trim { it <= ' ' }
                    for (item in 0..noteList.size) {
                        if (noteList[item].title.toLowerCase().contains(filterPattern)) {
                            searchableList.add(noteList[item])
                        }
                    }
                }
                return filterResults.also {
                    it.values = searchableList
                }
            }

            override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
                if (searchableList.isNullOrEmpty()) {
                    onNothingFound?.invoke()
                }
                notifyDataSetChanged()
            }
        }
    }
}
