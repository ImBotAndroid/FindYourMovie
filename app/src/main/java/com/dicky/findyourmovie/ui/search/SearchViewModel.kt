package com.dicky.findyourmovie.ui.search

import androidx.lifecycle.ViewModel
import com.dicky.findyourmovie.data.networking.Repository

class SearchViewModel(private val repository: Repository) : ViewModel() {
    fun searchMovie(apiKey: String, query: String) = repository.searchMovie(apiKey, query)

    fun saveSearchData() = repository.saveSearchData

    fun getRatedMovies(accountId: Int?, apiKey: String, sessionId: String) = repository.getRatedMovies(accountId, apiKey, sessionId)
}