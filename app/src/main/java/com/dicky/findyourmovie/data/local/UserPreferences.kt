package com.dicky.findyourmovie.data.local

import android.annotation.SuppressLint
import android.content.Context
import com.dicky.findyourmovie.data.response.LoginResponse
import com.dicky.findyourmovie.data.response.SessionResponse

class UserPreferences(context: Context) {

    private val preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun setToken(value: LoginResponse) {
        val editor = preferences.edit()
        editor.putString(DATE, value.expiresAt)
        editor.putBoolean(SUCCESS.toString(), value.success)
        editor.putString(TOKEN, value.requestToken)
        editor.apply()
    }

    fun getToken(): LoginResponse {
        val date = preferences.getString(DATE, "").toString()
        val success = preferences.getBoolean(SUCCESS.toString(), false)
        val token = preferences.getString(TOKEN, "").toString()
        return LoginResponse(date, success, token)
    }

    fun deleteToken(){
        val editor = preferences.edit()
        editor.putString(DATE, "")
        editor.putBoolean(SUCCESS.toString(), true)
        editor.putString(TOKEN, "")
        editor.apply()
    }

    fun setSessionId(value: SessionResponse?) {
        val editor = preferences.edit()
        editor.putString(SESSION_ID, value?.sessionId)
        editor.apply()
    }

    fun getSessionId(): SessionResponse {
        val success = preferences.getBoolean(SUCCESS.toString(), false)
        val sessionId = preferences.getString(SESSION_ID, "").toString()
        return SessionResponse(success, sessionId)
    }

    fun deleteSessionId(){
        val editor = preferences.edit()
        editor.putString(SESSION_ID, "")
        editor.apply()
    }

    fun getAccountId(): UserData {
        val accountId = preferences.getInt(ACCOUNT_ID, 0)
        val username = preferences.getString(USERNAME, "")
        val name = preferences.getString(NAME, "")
        val image = preferences.getString(IMAGE, "")
        return UserData(accountId, username, name, image)
    }

    @SuppressLint("CommitPrefEdits")
    fun setAccountId(value: Int) {
        val editor = preferences.edit()
        editor.putInt(ACCOUNT_ID, value)
        editor.apply()
    }

    companion object {
        private const val PREFS_NAME = "user_pref"
        private const val SUCCESS = false
        private const val DATE = "date"
        private const val TOKEN = "token"
        private const val SESSION_ID = "session_id"
        private const val ACCOUNT_ID = "account_id"
        private const val USERNAME = "username"
        private const val NAME = "name"
        private const val IMAGE = "image"

        @Volatile
        private var instance: UserPreferences? = null

        fun getInstance(context: Context): UserPreferences {
            return instance ?: synchronized(this) {
                instance ?: UserPreferences(context)
            }
        }
    }
}