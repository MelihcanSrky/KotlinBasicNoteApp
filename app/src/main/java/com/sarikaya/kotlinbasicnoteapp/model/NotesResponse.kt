package com.sarikaya.kotlinbasicnoteapp.model

import com.google.gson.annotations.SerializedName

data class NotesResponse(val code: String, @SerializedName("data") val allNotesData: NoteData, val message: String)
data class NoteData(
    @SerializedName("data")
    val notesDataList: List<NoteDataList>,
    @SerializedName("last_page")
    val lastPage: Int,
    @SerializedName("current_page")
    val currentPage: Int,
    @SerializedName("total")
    val totalItemCount: Int
)
data class NoteDataList(
    val id: Int,
    val title: String,
    val note: String
)
