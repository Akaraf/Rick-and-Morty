package com.raaf.rickandmorty.dataModels

import com.google.gson.annotations.SerializedName

data class Location(
    val id: Int,
    val name: String,
    @SerializedName("air_date")
    val date: String
)