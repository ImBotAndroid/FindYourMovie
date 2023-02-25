package com.dicky.findyourmovie.data.api

import com.dicky.findyourmovie.data.local.PostData
import com.dicky.findyourmovie.data.response.*
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @GET("authentication/token/new")
    fun getTokenForProfile(
        @Query("api_key") apiKey: String
    ): Call<LoginResponse>

    @FormUrlEncoded
    @POST("authentication/token/validate_with_login")
    fun loginUser(
        @Query("api_key") apiKey: String,
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("request_token") requestToken: String
    ): Call<LoginResponse>

    @FormUrlEncoded
    @POST("authentication/session/new")
    fun createNewSession(
        @Query("api_key") apiKey: String,
        @Field("request_token") requestToken: String
    ): Call<SessionResponse>

    @GET("search/movie")
    fun searchMovie(
        @Query("api_key") apiKey: String,
        @Query("query") query: String
    ): Call<SearchMovieResponse>

    @GET("account")
    fun getAccountDetail(
        @Query("api_key") apiKey: String,
        @Query("session_id") sessionId: String
    ): Call<AccountDetailResponse>

    @GET("account/{account_id}/rated/movies")
    fun getRatedMovies(
        @Path("account_id") accountId: Int?,
        @Query("api_key") apiKey: String,
        @Query("session_id") sessionId: String
    ): Call<GetMoviesRatedResponse>

    @FormUrlEncoded
    @POST("movie/{movie_id}/rating")
    fun postRatingMovies(
        @Path("movie_id") movieId: Int?,
        @Query("api_key") apiKey: String,
        @Query("session_id") sessionId: String,
        @Field("value") value: Float
    ): Call<RateAndMarkFavoriteMoviesResponse>

    @DELETE("movie/{movie_id}/rating")
    fun deleteRatingMovies(
        @Path("movie_id") movieId: Int?,
        @Query("api_key") apiKey: String,
        @Query("session_id") sessionId: String
    ): Call<RateAndMarkFavoriteMoviesResponse>

    @GET("account/{account_id}/favorite/movies")
    fun getFavoriteMovies(
        @Path("account_id") accountId: Int?,
        @Query("api_key") apiKey: String,
        @Query("session_id") sessionId: String
    ): Call<PopularFavoriteSimilarMoviesResponse>

    @POST("account/{account_id}/favorite")
    fun postFavoriteMovies(
        @Path("account_id") accountId: Int?,
        @Query("api_key") apiKey: String,
        @Query("session_id") sessionId: String,
        @Body postData: PostData
    ): Call <RateAndMarkFavoriteMoviesResponse>

    @GET("movie/popular")
    fun getPopularMovies(
        @Query("api_key") apiKey: String
    ): Call<PopularFavoriteSimilarMoviesResponse>

    @GET("movie/upcoming")
    fun getUpcomingMovies(
        @Query("api_key") apiKey: String
    ): Call<UpcomingAndNowPlayingMovieResponse>

    @GET("movie/now_playing")
    fun getNowPlayingMovies(
        @Query("api_key") apiKey: String
    ): Call<UpcomingAndNowPlayingMovieResponse>

    @GET("movie/{movie_id}")
    fun getDetailMovies(
        @Path("movie_id") movieId: Int?,
        @Query("api_key") apiKey: String
    ): Call<DetailMoviesResponse>

    @GET("movie/{movie_id}/videos")
    fun getTrailerVideo(
        @Path("movie_id") movieId: Int?,
        @Query("api_key") apiKey: String
    ): Call<GetVideosResponse>

    @GET("movie/{movie_id}/credits")
    fun getCredits(
        @Path("movie_id") movieId: Int?,
        @Query("api_key") apiKey: String
    ): Call<GetCreditsResponse>

    @GET("movie/{movie_id}/similar")
    fun getSimilarMovies(
        @Path("movie_id") movieId: Int?,
        @Query("api_key") apiKey: String
    ): Call<PopularFavoriteSimilarMoviesResponse>

    @GET("movie/{movie_id}/reviews")
    fun getReviewsMovies(
        @Path("movie_id") movieId: Int?,
        @Query("api_key") apiKey: String
    ): Call<GetReviewsResponse>
}