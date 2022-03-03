package com.raaf.rickandmorty.dataModels

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Character(
    val id: Int,
    val name: String,
    val status: String,
    val species: String,
    val location: Location,
    @SerializedName("episode")
    val episodes: List<String>,
    val image: String
) : Parcelable
