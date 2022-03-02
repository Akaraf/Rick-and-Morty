package com.raaf.rickandmorty.di.modules

import com.raaf.rickandmorty.data.CharactersPagingSource
import com.raaf.rickandmorty.data.webApi.ApiService
import dagger.Module
import dagger.Provides

@Module//(includes = arrayOf(ApiServiceModule::class))
class CharactersPagingSourceModule {

    @Provides
    fun providePagingSource(apiService: ApiService) : CharactersPagingSource {
        return CharactersPagingSource(apiService)
    }
}