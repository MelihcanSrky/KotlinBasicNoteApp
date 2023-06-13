package com.sarikaya.kotlinbasicnoteapp.storage

import android.content.Context
import com.sarikaya.kotlinbasicnoteapp.model.LoginTokenData

class SharedPrefManager private constructor(private val sharedContext: Context){

    companion object{
        private val SHARED_PREF_NAME = "my_shared_pref"
        private var mInstance: SharedPrefManager? = null
        @Synchronized
        fun getInstance(mCtx: Context): SharedPrefManager {
            if(mInstance == null) {
                mInstance = SharedPrefManager(mCtx)
            }
            return mInstance as SharedPrefManager
        }
    }

    val user: LoginTokenData
        get() {
            val sharedPreferences = sharedContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
            return LoginTokenData(
                sharedPreferences.getString("accessToken", null).toString(),
                sharedPreferences.getString("tokenType", null).toString()
            )
        }

    fun saveUser(data: LoginTokenData) {
        val sharedPreferences = sharedContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        editor.putString("accessToken", data.accessToken)
        editor.putString("tokenType", data.tokenType)
        editor.apply()
    }

    fun clear() {
        val sharedPreferences = sharedContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }
}
