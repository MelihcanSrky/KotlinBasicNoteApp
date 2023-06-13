package com.sarikaya.kotlinbasicnoteapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.sarikaya.kotlinbasicnoteapp.api.RetroRepository
import com.sarikaya.kotlinbasicnoteapp.model.ErrorModel
import com.sarikaya.kotlinbasicnoteapp.model.GetMeResponse
import com.sarikaya.kotlinbasicnoteapp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfilePageFragmentViewModel @Inject constructor(private val repository: RetroRepository) : BaseViewModel() {

    private val _userGetMeLiveData: MutableLiveData<GetMeResponse> = MutableLiveData()
    val userGetMeLiveData: LiveData<GetMeResponse> = _userGetMeLiveData

    private val _userUpdateMeLiveData: MutableLiveData<GetMeResponse> = MutableLiveData()
    val userUpdateMeLiveData: LiveData<GetMeResponse> = _userUpdateMeLiveData

    private val _errorLiveData: MutableLiveData<ErrorModel> = MutableLiveData()
    val errorLiveData: LiveData<ErrorModel> = _errorLiveData

    init {
        collectFromFlowData()
    }

    val isSubmitEnabled: Flow<Boolean> = combine(_fullName, _email) { fullName, email ->
        val isFullNameCorrect = setFullName(fullName)
        val isemailCorrect = setEmail(email)
        return@combine isFullNameCorrect and isemailCorrect
    }

    fun collectFromFlowData() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getMeFlowData.collect { response ->
                when(response) {
                    is Resource.Data -> {
                        _userGetMeLiveData.postValue(response.data)
                    }
                    is Resource.Error -> {
                        _errorLiveData.postValue(response.error)
                    }
                }
            }
        }
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateMeFlowData.collect { response ->
                when(response) {
                    is Resource.Data -> {
                        _userUpdateMeLiveData.postValue(response.data)
                    }
                    is Resource.Error -> {
                        _errorLiveData.postValue(response.error)
                    }
                }
            }
        }
    }

    fun getMe() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getMe()
        }
    }

    fun updateMe(fullName: String, email: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateMe(fullName, email)
        }
    }
}
