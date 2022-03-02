package com.raaf.rickandmorty.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.raaf.rickandmorty.Paging
import com.raaf.rickandmorty.data.webApi.ApiService
import com.raaf.rickandmorty.dataModels.Character
import java.lang.Exception
import javax.inject.Inject

class CharactersPagingSource @Inject constructor(
    private val apiService: ApiService
) : PagingSource<Int, Character>() {

    var savedPage: Int? = null

    override fun getRefreshKey(state: PagingState<Int, Character>): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        val page = state.closestPageToPosition(anchorPosition) ?: return null
        return page.prevKey?.plus(1) ?: page.nextKey?.minus(1)
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Character> {
        return try {
            val page = setPage(params.key)
            val charactersResponse = apiService.getCharacters(page)
            if (charactersResponse.results.isEmpty()) throw Exception("response error")
            val previousKey = if (page != Paging.INITIAL_PAGE) page - 1 else null
            val nextKey = if (page != charactersResponse.info.pages) page + 1 else null
            LoadResult.Page(charactersResponse.results, previousKey, nextKey)
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    private fun setPage(paramsPage: Int?) : Int {
        val result = if (savedPage != null) savedPage!!
        else paramsPage ?: Paging.INITIAL_PAGE
        savedPage = null
        return result
    }
}