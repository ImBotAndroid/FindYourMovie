package com.dicky.findyourmovie.data.local

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DataId(
    val id: Int?
): Parcelable
