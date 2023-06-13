package com.sarikaya.kotlinbasicnoteapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sarikaya.kotlinbasicnoteapp.api.RetroRepository
import com.sarikaya.kotlinbasicnoteapp.model.AddNoteResponse
import com.sarikaya.kotlinbasicnoteapp.model.ErrorModel
import com.sarikaya.kotlinbasicnoteapp.model.GetNoteResponse
import com.sarikaya.kotlinbasicnoteapp.model.NoteDataList
import com.sarikaya.kotlinbasicnoteapp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteDetailFragmentViewModel @Inject constructor(private val repository: RetroRepository) : ViewModel() {

    init {
        collectFromAddNoteFlowData()
        collectFromGetNoteFlowData()
        collectFromPutNoteFlowData()
    }

    private val _addNoteLiveData: MutableLiveData<AddNoteResponse> = MutableLiveData()
    val addNoteLiveData: LiveData<AddNoteResponse> = _addNoteLiveData

    private val _getNoteLiveData: MutableLiveData<GetNoteResponse> = MutableLiveData()
    val getNoteLiveData: LiveData<GetNoteResponse> = _getNoteLiveData

    private val _putNoteLiveData: MutableLiveData<AddNoteResponse> = MutableLiveData()
    val putNoteLiveData: LiveData<AddNoteResponse> = _putNoteLiveData

    private val _errorLiveData: MutableLiveData<ErrorModel> = MutableLiveData()
    val errorLiveData: LiveData<ErrorModel> = _errorLiveData

    fun collectFromAddNoteFlowData() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addNoteFlowData.collect { response ->
                when(response) {
                    is Resource.Data -> {
                        _addNoteLiveData.postValue(response.data)
                    }
                    is Resource.Error -> {
                        _errorLiveData.postValue(response.error)
                    }
                }
            }
        }
    }

    fun collectFromPutNoteFlowData() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.putNoteFlowData.collect { response ->
                when(response) {
                    is Resource.Data -> {
                        _putNoteLiveData.postValue(response.data)
                    }
                    is Resource.Error -> {
                        _errorLiveData.postValue(response.error)
                    }
                }
            }
        }
    }

    fun collectFromGetNoteFlowData() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getNoteFlowData.collect { response ->
                when(response) {
                    is Resource.Data -> {
                        _getNoteLiveData.postValue(response.data)
                    }
                    is Resource.Error -> {
                        _errorLiveData.postValue(response.error)
                    }
                }
            }
        }
    }

    fun addNote(title: String, note: String){
        viewModelScope.launch(Dispatchers.IO) {
            repository.addNote(title, note)
        }
    }

    fun putNote(id: String, title: String, note: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.putNote(id, title, note)
        }
    }

    fun getNote(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getNote(id)
        }
    }
}
