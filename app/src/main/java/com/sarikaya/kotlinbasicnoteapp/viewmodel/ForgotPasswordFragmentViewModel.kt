package com.sarikaya.kotlinbasicnoteapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.sarikaya.kotlinbasicnoteapp.api.RetroRepository
import com.sarikaya.kotlinbasicnoteapp.model.ChangePasswordResponse
import com.sarikaya.kotlinbasicnoteapp.model.ErrorModel
import com.sarikaya.kotlinbasicnoteapp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForgotPasswordFragmentViewModel @Inject constructor(private val repository: RetroRepository) : BaseViewModel() {

    private val _forgotPasswordLiveData: MutableLiveData<ChangePasswordResponse> = MutableLiveData()
    val forgotPasswordLiveData: LiveData<ChangePasswordResponse> = _forgotPasswordLiveData

    private val _errorLiveData: MutableLiveData<ErrorModel> = MutableLiveData()
    val errorLiveData: LiveData<ErrorModel> = _errorLiveData

    init {
        collectFromForgotPasswordFlowData()
    }

    fun collectFromForgotPasswordFlowData() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.forgotPasswordFlowData.collect { response ->
                when(response) {
                    is Resource.Data -> {
                        _forgotPasswordLiveData.postValue(response.data)
                    }
                    is Resource.Error -> {
                        _errorLiveData.postValue(response.error)
                    }
                }
            }
        }
    }

    fun forgotPassword(email: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.forgotPassword(email)
        }
    }
}
