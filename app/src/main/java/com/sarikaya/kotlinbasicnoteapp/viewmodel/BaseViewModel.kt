package com.sarikaya.kotlinbasicnoteapp.viewmodel

import androidx.lifecycle.ViewModel
import com.sarikaya.kotlinbasicnoteapp.utils.extensions.isEmailVaild
import com.sarikaya.kotlinbasicnoteapp.utils.extensions.isFullNameValid
import com.sarikaya.kotlinbasicnoteapp.utils.extensions.isPasswordValid
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
open class BaseViewModel @Inject constructor() : ViewModel() {

    val _fullName = MutableStateFlow("")
    val _email = MutableStateFlow("")
    val _password = MutableStateFlow("")

    fun setFullName(fullName: String): Boolean {
        _fullName.value = fullName
        val value: String = fullName
        return value.isFullNameValid()
    }

    fun setEmail(email: String): Boolean {
        _email.value = email
        val value: String = email
        return value.isEmailVaild()
    }

    fun setPassword(password: String): Boolean {
        _password.value = password
        val value: String = password
        return value.isPasswordValid()
    }

}
