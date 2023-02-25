package com.dicky.findyourmovie.ui.detail

import androidx.lifecycle.ViewModel
import com.dicky.findyourmovie.data.local.PostData
import com.dicky.findyourmovie.data.networking.Repository

class DetailViewModel(private val repository: Repository): ViewModel() {
    fun getDetailMovies(movieId: Int?, apiKey: String) = repository.getDetailMovies(movieId, apiKey)

    fun postRatingMovies(movieId: Int?, apiKey: String, sessionId: String, value: Float) = repository.postRatingMovies(movieId, apiKey, sessionId, value)

    fun deleteRatingMovies(movieId: Int?, apiKey: String, sessionId: String) = repository.deleteRatingMovies(movieId, apiKey, sessionId)

    fun postFavoriteMovies(accountId: Int?, apiKey: String, sessionId: String, postData: PostData) = repository.postFavoriteMovies(accountId, apiKey, sessionId, postData)

    fun getVideoTrailer(movieId: Int?, apiKey: String) = repository.getVideoTrailer(movieId, apiKey)

    fun getCredits(movieId: Int?, apiKey: String) = repository.getCredits(movieId, apiKey)

    fun getSimilarMovies(movieId: Int?, apiKey: String) = repository.getSimilarMovies(movieId, apiKey)

    fun getRatedMovies(accountId: Int?, apiKey: String, sessionId: String) = repository.getRatedMovies(accountId, apiKey, sessionId)

    fun getFavoriteMovies(accountId: Int?, apiKey: String, sessionId: String) = repository.getFavoriteMovies(accountId, apiKey, sessionId)
}