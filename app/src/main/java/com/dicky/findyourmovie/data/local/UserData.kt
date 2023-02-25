package com.dicky.findyourmovie.data.local

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserData(
    val id: Int,
    val username: String?,
    val name: String?,
    val image: String?
): Parcelable
