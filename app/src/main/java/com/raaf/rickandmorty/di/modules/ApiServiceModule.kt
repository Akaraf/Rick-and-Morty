package com.raaf.rickandmorty.di.modules

import com.raaf.rickandmorty.data.webApi.ApiService
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ApiServiceModule {

    @Singleton
    @Provides
    fun provideApiService() : ApiService {
        return ApiService.create()
    }
}