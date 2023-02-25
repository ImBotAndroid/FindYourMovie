package com.dicky.findyourmovie.ui.bookmark

import androidx.lifecycle.ViewModel
import com.dicky.findyourmovie.data.networking.Repository

class BookmarkViewModel(private val repository: Repository) : ViewModel() {

    fun getFavoriteMovies(accountId: Int?, apiKey: String, sessionId: String) = repository.getFavoriteMovies(accountId, apiKey, sessionId)

    fun getRatedMovies(accountId: Int?, apiKey: String, sessionId: String) = repository.getRatedMovies(accountId, apiKey, sessionId)
}