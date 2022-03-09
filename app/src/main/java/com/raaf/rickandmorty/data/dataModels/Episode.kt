package com.raaf.rickandmorty.data.dataModels

import com.google.gson.annotations.SerializedName

data class Episode(
    val id: Int,
    val name: String,
    @SerializedName("air_date") val date: String
)