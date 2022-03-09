package com.raaf.rickandmorty.viewModels

import android.os.Bundle
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.raaf.rickandmorty.Character.EXTRA_CHARACTER
import com.raaf.rickandmorty.Episodes.EXTRA_EPISODES_ID
import com.raaf.rickandmorty.data.dataModels.Character
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class CharacterDetailViewModel @AssistedInject constructor(
    @Assisted val savedStateHandle: SavedStateHandle
) : ViewModel() {

    var character: Character? = null
        private set
    private var episodeIdsList = mutableListOf<Int>()

    fun setCharacterFromBundle(bundle: Bundle?) {
        if (bundle == null) return
        character = bundle.getParcelable(EXTRA_CHARACTER)
        setEpisodesId()
    }

    private fun setEpisodesId() {
        if (character == null) return
        character!!.episodes.forEach {
            episodeIdsList.add(it.substringAfterLast("/").toInt())
        }
    }

    fun getEpisodesBundle(): Bundle {
        val bundle = Bundle()
        bundle.putIntegerArrayList(EXTRA_EPISODES_ID, ArrayList(episodeIdsList))
        return bundle
    }

    @AssistedFactory
    interface Factory {
        fun create(savedStateHandle: SavedStateHandle): CharacterDetailViewModel
    }
}