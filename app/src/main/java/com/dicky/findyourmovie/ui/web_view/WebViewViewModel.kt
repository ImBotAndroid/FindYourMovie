package com.dicky.findyourmovie.ui.web_view

import androidx.lifecycle.ViewModel
import com.dicky.findyourmovie.data.networking.Repository

class WebViewViewModel(private val repository: Repository): ViewModel() {

    fun getTokenPref() = repository.getTokenPref()

    fun getSessionId(apiKey: String, token: String) = repository.getSessionId(apiKey, token)
}