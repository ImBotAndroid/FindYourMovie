package com.dicky.findyourmovie.data.local

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MovieDataFavorite(
    val id: Int,
    var isFavorite: Boolean
): Parcelable
