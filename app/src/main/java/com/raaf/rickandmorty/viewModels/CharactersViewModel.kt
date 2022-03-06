package com.raaf.rickandmorty.viewModels

import androidx.lifecycle.*
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.raaf.rickandmorty.Paging
import com.raaf.rickandmorty.data.CharactersPagingSource
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class CharactersViewModel @AssistedInject constructor(
    private val pagingSource: CharactersPagingSource,
    @Assisted val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val pagerConfig = PagingConfig(
        pageSize = Paging.PAGE_SIZE,
        initialLoadSize = Paging.INITIAL_LOAD_SIZE,
        enablePlaceholders = true
    )
    val characters = Pager(pagerConfig) {
        pagingSource
    }.flow.cachedIn(viewModelScope)

    @AssistedFactory
    interface Factory {
        fun create(savedStateHandle: SavedStateHandle): CharactersViewModel
    }
}