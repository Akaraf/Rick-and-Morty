package com.raaf.rickandmorty.viewModels

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.raaf.rickandmorty.Character.EXTRA_CHARACTER
import com.raaf.rickandmorty.dataModels.Character
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class CharacterDetailViewModel @AssistedInject constructor(
    @Assisted val savedStateHandle: SavedStateHandle
) : ViewModel() {

    var character: Character? = null
    private set

    fun setCharacterFromBundle(bundle: Bundle?) {
        if (bundle == null) return
        character = bundle.getParcelable(EXTRA_CHARACTER)
    }

    @AssistedFactory
    interface Factory {
        fun create(savedStateHandle: SavedStateHandle) : CharacterDetailViewModel
    }
}