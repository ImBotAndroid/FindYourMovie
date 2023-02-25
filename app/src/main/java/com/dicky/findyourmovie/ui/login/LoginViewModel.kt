package com.dicky.findyourmovie.ui.login

import androidx.lifecycle.ViewModel
import com.dicky.findyourmovie.data.networking.Repository

class LoginViewModel (private val repository: Repository): ViewModel() {
    fun loginUser(apiKey: String, username: String, password: String, requestToken: String) = repository.loginUser(apiKey, username, password, requestToken)

    fun getNewToken(apiKey: String) = repository.getNewToken(apiKey)

    fun getSessionIdPref() = repository.getSessionIdPref()
}