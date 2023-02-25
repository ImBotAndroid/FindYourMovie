package com.dicky.findyourmovie.data.response

import com.google.gson.annotations.SerializedName

data class SessionResponse(

	@field:SerializedName("success")
	val success: Boolean,

	@field:SerializedName("session_id")
	val sessionId: String
)
