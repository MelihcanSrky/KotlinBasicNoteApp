package com.sarikaya.kotlinbasicnoteapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sarikaya.kotlinbasicnoteapp.api.RetroRepository
import com.sarikaya.kotlinbasicnoteapp.model.*
import com.sarikaya.kotlinbasicnoteapp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotesPageFragmentViewModel @Inject constructor(private val repository: RetroRepository) : ViewModel() {

    private val _notesLiveData: MutableLiveData<NotesResponse> = MutableLiveData()
    val notesLiveData: LiveData<NotesResponse> = _notesLiveData

    private val _deleteNoteLiveData: MutableLiveData<GetNoteResponse> = MutableLiveData()
    val deleteNoteLiveData: LiveData<GetNoteResponse> = _deleteNoteLiveData

    private val _errorLiveData: MutableLiveData<ErrorModel> = MutableLiveData()
    val errorLiveData: LiveData<ErrorModel> = _errorLiveData

    init {
        collectFromGetNotesFlowData()
        collectFromDeleteNoteFlowData()
    }

    fun collectFromGetNotesFlowData() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getNotesFlowData.collect { response ->
                when(response) {
                    is Resource.Data -> {
                        _notesLiveData.postValue(response.data)
                    }
                    is Resource.Error -> {
                        _errorLiveData.postValue(response.error)
                    }
                }
            }
        }
    }

    fun collectFromDeleteNoteFlowData() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteNoteFlowData.collect { response ->
                when(response) {
                    is Resource.Data -> {
                        _deleteNoteLiveData.postValue(response.data)
                    }
                    is Resource.Error -> {
                        _errorLiveData.postValue(response.error)
                    }
                }
            }
        }
    }

    fun getNotesList(pageNo: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getNotes(pageNo)
        }
    }

    fun deleteNote(pos: Int, totalNotesList: MutableList<NoteDataList>) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteNote(totalNotesList.get(pos)?.id.toString())
        }
    }
}
