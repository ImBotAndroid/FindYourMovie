package com.dicky.findyourmovie.data.local

import com.google.gson.annotations.SerializedName

data class PostData(
    @SerializedName("media_type")
    val mediaType: String,
    @SerializedName("media_id")
    val movieId: Int,
    @SerializedName("favorite")
    val favorite: Boolean
)
