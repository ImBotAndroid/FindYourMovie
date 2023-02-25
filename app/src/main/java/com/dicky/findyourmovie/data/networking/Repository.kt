package com.dicky.findyourmovie.data.networking

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dicky.findyourmovie.data.api.ApiService
import com.dicky.findyourmovie.data.local.PostData
import com.dicky.findyourmovie.data.local.UserPreferences
import com.dicky.findyourmovie.data.response.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Repository private constructor(
    private val apiService: ApiService,
    private val userPreferences: UserPreferences
){

    fun loginUser(apiKey: String, username: String, password: String, requestToken: String): LiveData<LoginResponse>{
        val loginResponse = MutableLiveData<LoginResponse>()

        val client = apiService.loginUser(apiKey, username, password, requestToken)
        client.enqueue(object : Callback<LoginResponse>{
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful){
                    loginResponse.value = response.body()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
        return loginResponse
    }

    fun getNewToken(apiKey: String): LiveData<LoginResponse>{
        val tokenResponse = MutableLiveData<LoginResponse>()

        val client = apiService.getTokenForProfile(apiKey)
        client.enqueue(object : Callback<LoginResponse>{
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful){
                    tokenResponse.value = response.body()
                    Log.e(TAG, "getTokenRepository: ${response.body()}")
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
        return tokenResponse
    }

    fun getSessionId(apiKey: String, request_token: String): LiveData<SessionResponse>{
        val sessionResponse = MutableLiveData<SessionResponse>()

        val client = apiService.createNewSession(apiKey, request_token)
        client.enqueue(object : Callback<SessionResponse>{
            override fun onResponse(call: Call<SessionResponse>, response: Response<SessionResponse>
            ) {
                if (response.isSuccessful){
                    sessionResponse.value = response.body()
                }
            }

            override fun onFailure(call: Call<SessionResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
        return sessionResponse
    }

    fun getTokenPref(): LiveData<LoginResponse>{
        val getTokenData = MutableLiveData<LoginResponse>()
        getTokenData.postValue(userPreferences.getToken())
        return getTokenData
    }

    fun getSessionIdPref(): LiveData<SessionResponse>{
        val getSessionId = MutableLiveData<SessionResponse>()
        getSessionId.postValue(userPreferences.getSessionId())
        return getSessionId
    }

    fun deleteDataPref() {
        userPreferences.deleteSessionId()
        userPreferences.deleteToken()
    }

    private val _saveSearchData = MutableLiveData<List<ResultsItemSearch>>()
    val saveSearchData: LiveData<List<ResultsItemSearch>> = _saveSearchData

    fun searchMovie(apiKey: String, query: String): LiveData<SearchMovieResponse>{
        val movieSearch = MutableLiveData<SearchMovieResponse>()
        val client = apiService.searchMovie(apiKey, query)
        client.enqueue(object : Callback<SearchMovieResponse>{
            override fun onResponse(call: Call<SearchMovieResponse>, response: Response<SearchMovieResponse>) {
                if (response.isSuccessful){
                    _saveSearchData.value = response.body()?.results
                    movieSearch.value = response.body()
                }
            }

            override fun onFailure(call: Call<SearchMovieResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
        return movieSearch
    }

    private val _saveAccountDetail = MutableLiveData<AccountDetailResponse>()
    val saveAccountDetail: LiveData<AccountDetailResponse> = _saveAccountDetail

    fun getAccountDetail(apiKey: String, sessionId: String)  {

        val client = apiService.getAccountDetail(apiKey, sessionId)
        client.enqueue(object : Callback<AccountDetailResponse>{
            override fun onResponse(call: Call<AccountDetailResponse>, response: Response<AccountDetailResponse>) {
                _saveAccountDetail.value = response.body()
            }

            override fun onFailure(call: Call<AccountDetailResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    private val _getMoviesNowPlaying = MutableLiveData<List<ResultsItemUpcomingAndNowPlaying>>()
    val getMoviesNowPlaying: LiveData<List<ResultsItemUpcomingAndNowPlaying>> = _getMoviesNowPlaying

    fun getNowPlayingMovies(apiKey: String) {

        val client = apiService.getNowPlayingMovies(apiKey)
        client.enqueue(object : Callback<UpcomingAndNowPlayingMovieResponse>{
            override fun onResponse(
                call: Call<UpcomingAndNowPlayingMovieResponse>,
                response: Response<UpcomingAndNowPlayingMovieResponse>,
            ) {
                if (response.isSuccessful){
                    _getMoviesNowPlaying.value = response.body()?.results
                }
            }

            override fun onFailure(call: Call<UpcomingAndNowPlayingMovieResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    private val _getMoviesUpComing = MutableLiveData<List<ResultsItemUpcomingAndNowPlaying>>()
    val getMoviesUpComing: LiveData<List<ResultsItemUpcomingAndNowPlaying>> = _getMoviesUpComing

    fun getUpComingMovies(apiKey: String) {

        val client = apiService.getUpcomingMovies(apiKey)
        client.enqueue(object : Callback<UpcomingAndNowPlayingMovieResponse>{
            override fun onResponse(call: Call<UpcomingAndNowPlayingMovieResponse>, response: Response<UpcomingAndNowPlayingMovieResponse>) {
                if (response.isSuccessful){
                    _getMoviesUpComing.value = response.body()?.results
                }
            }

            override fun onFailure(call: Call<UpcomingAndNowPlayingMovieResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    private val _getMoviesPopular = MutableLiveData<List<ResultsPopularFavoriteSimilar>>()
    val getMoviesPopular: LiveData<List<ResultsPopularFavoriteSimilar>> = _getMoviesPopular

    fun getPopularMovies(apiKey: String){

        val client = apiService.getPopularMovies(apiKey)
        client.enqueue(object : Callback<PopularFavoriteSimilarMoviesResponse>{
            override fun onResponse(call: Call<PopularFavoriteSimilarMoviesResponse>, response: Response<PopularFavoriteSimilarMoviesResponse>) {
                if (response.isSuccessful){
                    _getMoviesPopular.value = response.body()?.results
                }
            }

            override fun onFailure(call: Call<PopularFavoriteSimilarMoviesResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    fun getFavoriteMovies(accountId: Int?, apiKey: String, sessionId: String): LiveData<List<ResultsPopularFavoriteSimilar>> {
        val getMoviesFavorite = MutableLiveData<List<ResultsPopularFavoriteSimilar>>()
        val client = apiService.getFavoriteMovies(accountId, apiKey, sessionId)
        client.enqueue(object : Callback<PopularFavoriteSimilarMoviesResponse>{
            override fun onResponse(call: Call<PopularFavoriteSimilarMoviesResponse>, response: Response<PopularFavoriteSimilarMoviesResponse>) {
                if (response.isSuccessful){
                    getMoviesFavorite.value = response.body()?.results
                }
            }

            override fun onFailure(call: Call<PopularFavoriteSimilarMoviesResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
        return getMoviesFavorite
    }

    fun postFavoriteMovies(accountId: Int?, apiKey: String, sessionId: String, postData: PostData): LiveData<RateAndMarkFavoriteMoviesResponse> {
        val postMoviesFavorite = MutableLiveData<RateAndMarkFavoriteMoviesResponse>()

        val client = apiService.postFavoriteMovies(accountId, apiKey, sessionId, postData)
        client.enqueue(object : Callback<RateAndMarkFavoriteMoviesResponse>{
            override fun onResponse(call: Call<RateAndMarkFavoriteMoviesResponse>, response: Response<RateAndMarkFavoriteMoviesResponse>) {
                if (response.isSuccessful){
                    postMoviesFavorite.value = response.body()
                }
            }

            override fun onFailure(call: Call<RateAndMarkFavoriteMoviesResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
        return postMoviesFavorite
    }

    fun getRatedMovies(accountId: Int?, apiKey: String, sessionId: String): LiveData<List<ResultsItemRated>> {
        val getMoviesRated = MutableLiveData<List<ResultsItemRated>>()

        val client = apiService.getRatedMovies(accountId, apiKey, sessionId)
        client.enqueue(object : Callback<GetMoviesRatedResponse> {
            override fun onResponse(call: Call<GetMoviesRatedResponse>, response: Response<GetMoviesRatedResponse> ) {
                if (response.isSuccessful){
                    getMoviesRated.value = response.body()?.results
                }
            }

            override fun onFailure(call: Call<GetMoviesRatedResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
        return getMoviesRated
    }

    fun postRatingMovies(movieId: Int?, apiKey: String, sessionId: String, value: Float): LiveData<RateAndMarkFavoriteMoviesResponse> {
        val postMoviesRating = MutableLiveData<RateAndMarkFavoriteMoviesResponse>()

        val client = apiService.postRatingMovies(movieId, apiKey, sessionId, value)
        client.enqueue(object : Callback<RateAndMarkFavoriteMoviesResponse>{
            override fun onResponse(call: Call<RateAndMarkFavoriteMoviesResponse>, response: Response<RateAndMarkFavoriteMoviesResponse>) {
                if (response.isSuccessful){
                    postMoviesRating.value = response.body()
                }
            }

            override fun onFailure(call: Call<RateAndMarkFavoriteMoviesResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}")
            }

        })
        return postMoviesRating
    }

    fun deleteRatingMovies(movieId: Int?, apiKey: String, sessionId: String): LiveData<RateAndMarkFavoriteMoviesResponse> {
        val deleteMoviesRating = MutableLiveData<RateAndMarkFavoriteMoviesResponse>()

        val client = apiService.deleteRatingMovies(movieId, apiKey, sessionId)
        client.enqueue(object : Callback<RateAndMarkFavoriteMoviesResponse>{
            override fun onResponse(call: Call<RateAndMarkFavoriteMoviesResponse>, response: Response<RateAndMarkFavoriteMoviesResponse>) {
                if (response.isSuccessful){
                    deleteMoviesRating.value = response.body()
                }
            }

            override fun onFailure(call: Call<RateAndMarkFavoriteMoviesResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}")
            }

        })
        return deleteMoviesRating
    }

    fun getDetailMovies(movieId: Int?, apiKey: String): LiveData<DetailMoviesResponse> {
        val getMoviesDetail = MutableLiveData<DetailMoviesResponse>()

        val client = apiService.getDetailMovies(movieId, apiKey)
        client.enqueue(object : Callback<DetailMoviesResponse> {
            override fun onResponse(call: Call<DetailMoviesResponse>, response: Response<DetailMoviesResponse> ) {
                if (response.isSuccessful){
                    getMoviesDetail.value = response.body()
                }
            }

            override fun onFailure(call: Call<DetailMoviesResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
        return getMoviesDetail
    }

    fun getVideoTrailer(movieId: Int?, apiKey: String): LiveData<GetVideosResponse>{
        val getTrailerVideo = MutableLiveData<GetVideosResponse>()

        val client = apiService.getTrailerVideo(movieId, apiKey)
        client.enqueue(object : Callback<GetVideosResponse>{
            override fun onResponse(call: Call<GetVideosResponse>, response: Response<GetVideosResponse> ) {
                if (response.isSuccessful){
                    getTrailerVideo.value = response.body()
                }
            }

            override fun onFailure(call: Call<GetVideosResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
        return getTrailerVideo
    }

    fun getCredits(movieId: Int?, apiKey: String): LiveData<List<CastItem>>{
        val getCreditsVideo = MutableLiveData<List<CastItem>>()

        val client = apiService.getCredits(movieId, apiKey)
        client.enqueue(object : Callback<GetCreditsResponse>{
            override fun onResponse(call: Call<GetCreditsResponse>, response: Response<GetCreditsResponse> ) {
                if (response.isSuccessful){
                    getCreditsVideo.value = response.body()?.cast
                }
            }

            override fun onFailure(call: Call<GetCreditsResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}")
            }

        })
        return getCreditsVideo
    }

    fun getSimilarMovies(movieId: Int?, apiKey: String): LiveData<List<ResultsPopularFavoriteSimilar>>{
        val getMoviesSimilar = MutableLiveData<List<ResultsPopularFavoriteSimilar>>()

        val client = apiService.getSimilarMovies(movieId, apiKey)
        client.enqueue(object : Callback<PopularFavoriteSimilarMoviesResponse>{
            override fun onResponse(call: Call<PopularFavoriteSimilarMoviesResponse>, response: Response<PopularFavoriteSimilarMoviesResponse>, ) {
                if (response.isSuccessful){
                    getMoviesSimilar.value = response.body()?.results
                }
            }

            override fun onFailure(call: Call<PopularFavoriteSimilarMoviesResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
        return getMoviesSimilar
    }

    fun getReviewsMovies(movieId: Int?, apiKey: String): LiveData<List<ResultsItemReviews>>{
        val getMoviesReviews = MutableLiveData<List<ResultsItemReviews>>()

        val client = apiService.getReviewsMovies(movieId, apiKey)
        client.enqueue(object : Callback<GetReviewsResponse>{
            override fun onResponse(call: Call<GetReviewsResponse>, response: Response<GetReviewsResponse>, ) {
                getMoviesReviews.value = response.body()?.results
            }

            override fun onFailure(call: Call<GetReviewsResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
        return getMoviesReviews
    }

    companion object {
        private const val TAG = "Repository"

        @Volatile
        private var instance: Repository? = null
        fun getInstance(
            apiService: ApiService,
            userPreferences: UserPreferences
        ): Repository =
            instance ?: synchronized(this) {
                instance ?: Repository(apiService, userPreferences)
            }.also { instance = it }
    }
}