package com.sarikaya.kotlinbasicnoteapp.utils.extensions

import android.util.Patterns
import android.view.View

fun String?.isEmailVaild(): Boolean {
    return !isNullOrEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

fun String?.isFullNameValid(): Boolean {
    return !isNullOrEmpty()
}

fun String?.isPasswordValid(): Boolean {
    return !isNullOrEmpty() && this.length >= 6
}

fun View.isbuttonEnabled(bool: Boolean) {
    if (bool) this.enabled() else this.disabled()
}

fun View.enabled() {
    this.isEnabled = true
}

fun View.disabled() {
    this.isEnabled = false
}
