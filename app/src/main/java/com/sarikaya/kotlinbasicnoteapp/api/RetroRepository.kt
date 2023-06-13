package com.sarikaya.kotlinbasicnoteapp.api

import android.content.Context
import com.sarikaya.kotlinbasicnoteapp.model.*
import com.sarikaya.kotlinbasicnoteapp.storage.SharedPrefManager
import com.sarikaya.kotlinbasicnoteapp.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject

open class RetroRepository @Inject constructor(
    private val retrofitServiceInstance: RetrofitServiceInstance,
    context: Context
) {
    val accessToken = "Bearer " + SharedPrefManager.getInstance(context).user.accessToken

    private val registerChannelData = Channel<Resource<SignResponse, ErrorModel>> {  }
    val registerFlowData: Flow<Resource<SignResponse, ErrorModel>> = registerChannelData.receiveAsFlow()

    suspend fun createUser(
        fullName: String,
        email: String,
        password: String
    ) {
        withContext(Dispatchers.IO) {
            val response = retrofitServiceInstance.createUser(fullName, email, password)
            if (response.isSuccessful && response.body() != null) {
                registerChannelData.send(Resource.Data(response.body()!!))
            } else {
                registerChannelData.send(Resource.Error(ErrorModel(response.message())))
            }
        }
    }

    private val loginChannelData = Channel<Resource<SignResponse, ErrorModel>> {  }
    val loginFlowData: Flow<Resource<SignResponse, ErrorModel>> = loginChannelData.receiveAsFlow()

    suspend fun loginUser(
        email: String,
        password: String
    ) {
        withContext(Dispatchers.IO) {
            val response = retrofitServiceInstance.userLogin(email, password)
            if (response.isSuccessful && response.body() != null) {
                loginChannelData.send(Resource.Data(response.body()!!))
            } else {
                loginChannelData.send(Resource.Error(ErrorModel(response.message())))
            }
        }
    }

    private val getNotesChannelData = Channel<Resource<NotesResponse, ErrorModel>> {  }
    val getNotesFlowData: Flow<Resource<NotesResponse, ErrorModel>> = getNotesChannelData.receiveAsFlow()

    suspend fun getNotes(pageNo: Int) {
        withContext(Dispatchers.IO) {
            val response = retrofitServiceInstance.getNotes(pageNo, accessToken)
            if (response.isSuccessful && response.body() != null) {
                getNotesChannelData.send(Resource.Data(response.body()!!))
            } else {
                getNotesChannelData.send(Resource.Error(ErrorModel(response.message())))
            }
        }
    }

    private val addNoteChannelData = Channel<Resource<AddNoteResponse, ErrorModel>> {  }
    val addNoteFlowData: Flow<Resource<AddNoteResponse, ErrorModel>> = addNoteChannelData.receiveAsFlow()

    suspend fun addNote(title: String, note: String) {
        withContext(Dispatchers.IO) {
            val response = retrofitServiceInstance.addNote(accessToken, title, note)
            if (response.isSuccessful && response.body() != null) {
                addNoteChannelData.send(Resource.Data(response.body()!!))
            } else {
                addNoteChannelData.send(Resource.Error(ErrorModel(response.message())))
            }
        }
    }

    private val getNoteChannelData = Channel<Resource<GetNoteResponse, ErrorModel>> {  }
    val getNoteFlowData: Flow<Resource<GetNoteResponse, ErrorModel>> = getNoteChannelData.receiveAsFlow()

    suspend fun getNote(id: String) {
        withContext(Dispatchers.IO) {
            val response = retrofitServiceInstance.getNote(accessToken, id.toInt())
            if (response.isSuccessful && response.body() != null) {
                getNoteChannelData.send(Resource.Data(response.body()!!))
            } else {
                getNoteChannelData.send(Resource.Error(ErrorModel(response.message())))
            }
        }
    }

    private val putNoteChannelData = Channel<Resource<AddNoteResponse, ErrorModel>> {  }
    val putNoteFlowData: Flow<Resource<AddNoteResponse, ErrorModel>> = putNoteChannelData.receiveAsFlow()

    suspend fun putNote(id: String, title: String, note: String) {
        withContext(Dispatchers.IO) {
            val response = retrofitServiceInstance.putNote(accessToken, id.toInt(), title, note)
            if (response.isSuccessful && response.body() != null) {
                putNoteChannelData.send(Resource.Data(response.body()!!))
            } else {
                putNoteChannelData.send(Resource.Error(ErrorModel(response.message())))
            }
        }
    }

    private val deleteNoteChannelData = Channel<Resource<GetNoteResponse, ErrorModel>> {  }
    val deleteNoteFlowData: Flow<Resource<GetNoteResponse, ErrorModel>> = deleteNoteChannelData.receiveAsFlow()

    suspend fun deleteNote(id: String) {
        withContext(Dispatchers.IO) {
            val response = retrofitServiceInstance.deleteNote(accessToken, id.toInt())
            if (response.isSuccessful && response.body() != null) {
                deleteNoteChannelData.send(Resource.Data(response.body()!!))
            } else {
                deleteNoteChannelData.send(Resource.Error(ErrorModel(response.message())))
            }
        }
    }

    private val getMeChannelData = Channel<Resource<GetMeResponse, ErrorModel>> {  }
    val getMeFlowData: Flow<Resource<GetMeResponse, ErrorModel>> = getMeChannelData.receiveAsFlow()

    suspend fun getMe() {
        withContext(Dispatchers.IO) {
            val response = retrofitServiceInstance.getMe(accessToken)
            if (response.isSuccessful && response.body() != null) {
                getMeChannelData.send(Resource.Data(response.body()!!))
            } else {
                getMeChannelData.send(Resource.Error(ErrorModel(response.message())))
            }
        }
    }

    private val changePasswordChannelData = Channel<Resource<ChangePasswordResponse, ErrorModel>> {  }
    val changePasswordFlowData: Flow<Resource<ChangePasswordResponse, ErrorModel>> = changePasswordChannelData.receiveAsFlow()

    suspend fun changePassword(password: String, newPassword: String, newPasswordCondfirm: String) {
        withContext(Dispatchers.IO) {
            val response = retrofitServiceInstance.updatePassword(
                accessToken,
                password, newPassword, newPasswordCondfirm
            )
            if (response.isSuccessful && response.body() != null) {
                changePasswordChannelData.send(Resource.Data(response.body()!!))
            } else {
                changePasswordChannelData.send(Resource.Error(ErrorModel(response.message())))
            }
        }
    }

    private val forgotPasswordChannelData = Channel<Resource<ChangePasswordResponse, ErrorModel>> {  }
    val forgotPasswordFlowData: Flow<Resource<ChangePasswordResponse, ErrorModel>> = forgotPasswordChannelData.receiveAsFlow()

    suspend fun forgotPassword(email: String) {
        withContext(Dispatchers.IO) {
            val response = retrofitServiceInstance.forgotPassword(email)
            if (response.isSuccessful && response.body() != null) {
                forgotPasswordChannelData.send(Resource.Data(response.body()!!))
            } else {
                forgotPasswordChannelData.send(Resource.Error(ErrorModel(response.message())))
            }
        }
    }

    private val updateMeChannelData = Channel<Resource<GetMeResponse, ErrorModel>> {  }
    val updateMeFlowData: Flow<Resource<GetMeResponse, ErrorModel>> = updateMeChannelData.receiveAsFlow()

    suspend fun updateMe(fullName: String, email: String) {
        withContext(Dispatchers.IO) {
            val response = retrofitServiceInstance.updateMe(accessToken, fullName, email)
            if (response.isSuccessful && response.body() != null) {
                updateMeChannelData.send(Resource.Data(response.body()!!))
            } else {
                updateMeChannelData.send(Resource.Error(ErrorModel(response.message())))
            }
        }
    }
}
