package com.dicky.findyourmovie.data.local

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RatedValue(
    val value: Float
): Parcelable
