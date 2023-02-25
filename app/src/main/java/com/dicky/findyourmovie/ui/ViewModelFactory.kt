package com.dicky.findyourmovie.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicky.findyourmovie.data.networking.Repository
import com.dicky.findyourmovie.dependency_injection.Injection
import com.dicky.findyourmovie.ui.bookmark.BookmarkViewModel
import com.dicky.findyourmovie.ui.detail.DetailViewModel
import com.dicky.findyourmovie.ui.detail.review.ReviewsViewModel
import com.dicky.findyourmovie.ui.home.HomeViewModel
import com.dicky.findyourmovie.ui.login.LoginViewModel
import com.dicky.findyourmovie.ui.profile.ProfileViewModel
import com.dicky.findyourmovie.ui.web_view.WebViewViewModel
import com.dicky.findyourmovie.ui.search.SearchViewModel

class ViewModelFactory private constructor(private val repository: Repository) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(repository) as T
            }
            modelClass.isAssignableFrom(WebViewViewModel::class.java) -> {
                WebViewViewModel(repository) as T
            }
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(repository) as T
            }
            modelClass.isAssignableFrom(BookmarkViewModel::class.java) -> {
                BookmarkViewModel(repository) as T
            }
            modelClass.isAssignableFrom(SearchViewModel::class.java) -> {
                SearchViewModel(repository) as T
            }
            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> {
                ProfileViewModel(repository) as T
            }
            modelClass.isAssignableFrom(DetailViewModel::class.java) -> {
                DetailViewModel(repository) as T
            }
            modelClass.isAssignableFrom(ReviewsViewModel::class.java) -> {
                ReviewsViewModel(repository) as T
            }
            else -> throw Throwable("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null

        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(Injection.provideRepository(context))
            }
    }
}