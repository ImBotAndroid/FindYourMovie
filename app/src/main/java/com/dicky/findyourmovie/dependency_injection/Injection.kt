package com.dicky.findyourmovie.dependency_injection

import android.content.Context
import com.dicky.findyourmovie.data.api.ApiConfig
import com.dicky.findyourmovie.data.local.UserPreferences
import com.dicky.findyourmovie.data.networking.Repository

object Injection {
    fun provideRepository(context: Context): Repository {
        val apiService = ApiConfig.getApiService()
        val userPreferences = UserPreferences.getInstance(context)
        return Repository.getInstance(apiService, userPreferences)
    }
}