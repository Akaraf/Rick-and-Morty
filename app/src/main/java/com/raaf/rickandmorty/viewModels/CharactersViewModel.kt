package com.raaf.rickandmorty.viewModels

import androidx.lifecycle.*
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.raaf.rickandmorty.Paging
import com.raaf.rickandmorty.data.CharactersPagingSource
import com.raaf.rickandmorty.ui.adapters.CharactersAdapter
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class CharactersViewModel @AssistedInject constructor(
    private val pagingSource: CharactersPagingSource,
    @Assisted val savedStateHandle: SavedStateHandle
) : ViewModel() {

    companion object {
        private const val PAGING_STATE = "saved offset"
    }

    var savedPosition: Int? = null
    private set
    get() {
        val fieldVal = field
        field = null
        return fieldVal
    }

    init {
        setSavedOffset()
    }

    private val pagerConfig = PagingConfig(
        pageSize = Paging.PAGE_SIZE,
        initialLoadSize = Paging.INITIAL_LOAD_SIZE,
        enablePlaceholders = true
    )
    val characters = Pager(pagerConfig) {
        pagingSource
    }.flow.cachedIn(viewModelScope)

    private fun setSavedOffset() {
        val offset = savedStateHandle.get<Int?>(PAGING_STATE)
        pagingSource.savedPage = offset?.div(Paging.PAGE_SIZE)
        savedPosition = offset?.rem(Paging.PAGE_SIZE)
    }

    fun saveOffset(currentPosition: Int?) {
        savedStateHandle.set(PAGING_STATE, currentPosition)
    }

    @AssistedFactory
    interface Factory {
        fun create(savedStateHandle: SavedStateHandle) : CharactersViewModel
    }
}