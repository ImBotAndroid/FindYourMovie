package com.dicky.findyourmovie.data.response

import com.google.gson.annotations.SerializedName

data class RateAndMarkFavoriteMoviesResponse(

	@field:SerializedName("status_message")
	val statusMessage: String,

	@field:SerializedName("status_code")
	val statusCode: Int,

	@field:SerializedName("success")
	val success: Boolean
)
