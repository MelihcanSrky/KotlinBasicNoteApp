package com.sarikaya.kotlinbasicnoteapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.sarikaya.kotlinbasicnoteapp.api.RetroRepository
import com.sarikaya.kotlinbasicnoteapp.model.ErrorModel
import com.sarikaya.kotlinbasicnoteapp.model.LoginTokenData
import com.sarikaya.kotlinbasicnoteapp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginPageFragmentViewModel @Inject constructor(val repository: RetroRepository) : BaseViewModel() {

    private val _loginTokenData: MutableLiveData<LoginTokenData> = MutableLiveData()
    val loginTokenData: LiveData<LoginTokenData> = _loginTokenData

    private val _errorLiveData: MutableLiveData<ErrorModel> = MutableLiveData()
    val errorLiveData: LiveData<ErrorModel> = _errorLiveData

    private val _isSuccessLoading: MutableLiveData<Boolean> = MutableLiveData()
    val isSuccessLoading: LiveData<Boolean> = _isSuccessLoading

    init {
        collectFromLoginFlowData()
    }

    val isSubmitEnabled: Flow<Boolean> = combine(_email, _password) {email, password ->
        val isemailCorrect = setEmail(email)
        val isPasswordCorrect = setPassword(password)
        return@combine isPasswordCorrect and isemailCorrect
    }

    fun collectFromLoginFlowData() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.loginFlowData.collect { response ->
                when(response) {
                    is Resource.Data -> {
                        _loginTokenData.postValue(response.data.data)
                        _isSuccessLoading.postValue(true)
                    }
                    is Resource.Error -> {
                        _errorLiveData.postValue(response.error)
                        _isSuccessLoading.postValue(false)
                    }
                }
            }
        }
    }

    fun userLogin(email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.loginUser(email, password)
        }
    }
}
