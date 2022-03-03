package com.raaf.rickandmorty.viewModels

import android.os.Bundle
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.raaf.rickandmorty.Episodes.EXTRA_EPISODES_ID
import com.raaf.rickandmorty.data.webApi.ApiService
import com.raaf.rickandmorty.dataModels.Episode
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import java.util.ArrayList

class EpisodesViewModel @AssistedInject constructor(
    private val apiService: ApiService,
    @Assisted savedStateHandle: SavedStateHandle
) : ViewModel() {

    var episodesIdString: String = ""
    var isOnlyOneEpisode = false
        private set

    fun setEpisodesIdFromBundle(bundle: Bundle?): Boolean {
        if (bundle == null) return false
        val ids: ArrayList<Int> = bundle.getIntegerArrayList(EXTRA_EPISODES_ID) ?: return false
        if (ids.count() == 1) isOnlyOneEpisode = true
        episodesIdString = ids.toString()
        episodesIdString = episodesIdString.substring(1 until episodesIdString.lastIndex)
        return episodesIdString.isNotEmpty()
    }

    suspend fun getEpisodes(): List<Episode> {
        return apiService.getEpisodesById(episodesIdString)
    }

    suspend fun getEpisode(): Episode {
        return apiService.getEpisodeById(episodesIdString)
    }

    @AssistedFactory
    interface Factory {
        fun create(savedStateHandle: SavedStateHandle): EpisodesViewModel
    }
}