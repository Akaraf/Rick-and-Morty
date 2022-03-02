package com.raaf.rickandmorty.data.webApi.responseModels

import com.raaf.rickandmorty.dataModels.Character

data class CharactersResponse(
    val info: Info,
    val results: List<Character>
)
