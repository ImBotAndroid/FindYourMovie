package com.dicky.findyourmovie.data.local

import android.os.Parcelable
import com.dicky.findyourmovie.data.response.ResultsItemRated
import kotlinx.parcelize.Parcelize

@Parcelize
data class MovieDataRating(
    val id: Int?,
    val rating: Int?
): Parcelable
