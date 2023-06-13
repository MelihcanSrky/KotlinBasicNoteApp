package com.sarikaya.kotlinbasicnoteapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sarikaya.kotlinbasicnoteapp.api.RetroRepository
import com.sarikaya.kotlinbasicnoteapp.model.AddNoteResponse
import com.sarikaya.kotlinbasicnoteapp.model.ChangePasswordResponse
import com.sarikaya.kotlinbasicnoteapp.model.ErrorModel
import com.sarikaya.kotlinbasicnoteapp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChangePasswordFragmentViewModel @Inject constructor(private val repository: RetroRepository) : ViewModel() {

    private val _changePasswordLiveData: MutableLiveData<ChangePasswordResponse> = MutableLiveData()
    val changePasswordLiveData: LiveData<ChangePasswordResponse> = _changePasswordLiveData

    private val _errorLiveData: MutableLiveData<ErrorModel> = MutableLiveData()
    val errorLiveData: LiveData<ErrorModel> = _errorLiveData

    init {
        collectFromChangePasswordFlowData()
    }

    fun collectFromChangePasswordFlowData() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.changePasswordFlowData.collect { response ->
                when(response) {
                    is Resource.Data -> {
                        _changePasswordLiveData.postValue(response.data)
                    }
                    is Resource.Error -> {
                        _errorLiveData.postValue(response.error)
                    }
                }
            }
        }
    }

    fun changePassword(password: String, newPassword: String, newPasswordConfirm: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.changePassword(password, newPassword, newPasswordConfirm)
        }
    }
}
