package com.sarikaya.kotlinbasicnoteapp.utils

import com.sarikaya.kotlinbasicnoteapp.model.ErrorModel

sealed class Resource<out R : Any, out E : ErrorModel> {
    class Data<out R : Any>(val data: R) : Resource<R, Nothing>()
    class Error<out E : ErrorModel>(val error: E) : Resource<Nothing, E>()
}
