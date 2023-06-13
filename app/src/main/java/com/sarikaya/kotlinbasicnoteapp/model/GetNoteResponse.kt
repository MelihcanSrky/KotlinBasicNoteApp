package com.sarikaya.kotlinbasicnoteapp.model

data class GetNoteResponse(val code: String, val data: NoteDataList, val message: String)
