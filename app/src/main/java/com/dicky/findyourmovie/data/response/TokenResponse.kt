package com.dicky.findyourmovie.data.response

import com.google.gson.annotations.SerializedName

data class TokenResponse(

	@field:SerializedName("expires_at")
	val expiresAt: String,

	@field:SerializedName("success")
	val success: Boolean,

	@field:SerializedName("request_token")
	val requestToken: String
)
