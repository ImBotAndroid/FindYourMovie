package com.dicky.findyourmovie.data.response

import com.google.gson.annotations.SerializedName

data class AccountDetailResponse(

	@field:SerializedName("include_adult")
	val includeAdult: Boolean,

	@field:SerializedName("iso_3166_1")
	val iso31661: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("avatar")
	val avatar: Avatar,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("iso_639_1")
	val iso6391: String,

	@field:SerializedName("username")
	val username: String
)

data class Gravatar(

	@field:SerializedName("hash")
	val hash: String
)

data class Avatar(

	@field:SerializedName("tmdb")
	val tmdb: Tmdb,

	@field:SerializedName("gravatar")
	val gravatar: Gravatar
)

data class Tmdb(

	@field:SerializedName("avatar_path")
	val avatarPath: String
)
