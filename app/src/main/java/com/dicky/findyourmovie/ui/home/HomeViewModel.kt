package com.dicky.findyourmovie.ui.home

import androidx.lifecycle.ViewModel
import com.dicky.findyourmovie.data.networking.Repository

class HomeViewModel(private val repository: Repository) : ViewModel() {

    fun getAccountDetail(apiKey: String, sessionId: String) = repository.getAccountDetail(apiKey, sessionId)

    fun getNowPlayingMovies(apiKey: String) = repository.getNowPlayingMovies(apiKey)

    fun getUpComingMovies(apiKey: String) = repository.getUpComingMovies(apiKey)

    fun getPopularMovies(apiKey: String) = repository.getPopularMovies(apiKey)

    fun getMoviesPopular() = repository.getMoviesPopular

    fun getMoviesUpComing() = repository.getMoviesUpComing

    fun getMoviesNowPlaying() = repository.getMoviesNowPlaying

    fun getRatedMovies(accountId: Int?, apiKey: String, sessionId: String) = repository.getRatedMovies(accountId, apiKey, sessionId)

    fun getFavoriteMovies(accountId: Int?, apiKey: String, sessionId: String) = repository.getFavoriteMovies(accountId, apiKey, sessionId)

    fun saveAccountDetail() = repository.saveAccountDetail
}