package com.raaf.rickandmorty.dataModels

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Location(
    val id: Int?,
    val name: String,
) : Parcelable