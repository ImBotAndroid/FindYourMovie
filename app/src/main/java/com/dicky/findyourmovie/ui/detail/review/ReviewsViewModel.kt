package com.dicky.findyourmovie.ui.detail.review

import androidx.lifecycle.ViewModel
import com.dicky.findyourmovie.data.networking.Repository

class ReviewsViewModel (private val repository: Repository): ViewModel() {
    fun getReviewsMovies(movieId: Int?, apiKey: String) = repository.getReviewsMovies(movieId, apiKey)
}