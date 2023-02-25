package com.dicky.findyourmovie.ui.profile

import androidx.lifecycle.ViewModel
import com.dicky.findyourmovie.data.networking.Repository

class ProfileViewModel (private val repository: Repository): ViewModel() {
    fun getRatedMovies(accountId: Int?, apiKey: String, sessionId: String) = repository.getRatedMovies(accountId, apiKey, sessionId)

    fun deleteDataPref() = repository.deleteDataPref()

    fun getNewToken(apiKey: String) = repository.getNewToken(apiKey)
}