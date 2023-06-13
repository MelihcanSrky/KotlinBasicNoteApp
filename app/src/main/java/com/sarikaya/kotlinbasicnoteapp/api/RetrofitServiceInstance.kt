package com.sarikaya.kotlinbasicnoteapp.api

import com.sarikaya.kotlinbasicnoteapp.model.*
import retrofit2.Response
import retrofit2.http.*

interface RetrofitServiceInstance {

    @FormUrlEncoded
    @POST("auth/register")
    suspend fun createUser(
        @Field("full_name") fullName: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Response<SignResponse>

    @FormUrlEncoded
    @POST("auth/login")
    suspend fun userLogin(
        @Field("email") email: String,
        @Field("password") password: String
    ): Response<SignResponse>

    @GET("users/me/notes")
    suspend fun getNotes(
        @Query("page") pageNo: Int,
        @Header("Authorization") accessToken: String
    ): Response<NotesResponse>

    @FormUrlEncoded
    @POST("notes")
    suspend fun addNote(
        @Header("Authorization") accessToken: String,
        @Field("title") title: String,
        @Field("note") note: String
    ): Response<AddNoteResponse>

    @GET("notes/{noteId}")
    suspend fun getNote(
        @Header("Authorization") accessToken: String,
        @Path("noteId") noteId: Int
    ): Response<GetNoteResponse>

    @FormUrlEncoded
    @PUT("notes/{noteId}")
    suspend fun putNote(
        @Header("Authorization") accessToken: String,
        @Path("noteId") noteId: Int,
        @Field("title") title: String,
        @Field("note") note: String
    ): Response<AddNoteResponse>

    @DELETE("notes/{noteId}")
    suspend fun deleteNote(
        @Header("Authorization") accessToken: String,
        @Path("noteId") noteId: Int
    ): Response<GetNoteResponse>

    @GET("users/me")
    suspend fun getMe(
        @Header("Authorization") accessToken: String
    ): Response<GetMeResponse>

    @FormUrlEncoded
    @PUT("users/me/password")
    suspend fun updatePassword(
        @Header("Authorization") accessToken: String,
        @Field("password") password: String,
        @Field("new_password") newPassword: String,
        @Field("new_password_confirmation") newPasswordConfirmation: String
    ): Response<ChangePasswordResponse>

    @FormUrlEncoded
    @POST("auth/forgot-password")
    suspend fun forgotPassword(
        @Field("email") email: String
    ): Response<ChangePasswordResponse>

    @FormUrlEncoded
    @PUT("users/me")
    suspend fun updateMe(
        @Header("Authorization") accessToken: String,
        @Field("full_name") fullName: String,
        @Field("email") email: String
    ): Response<GetMeResponse>
}
