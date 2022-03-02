package com.raaf.rickandmorty.di.components

import com.raaf.rickandmorty.di.modules.ApiServiceModule
import com.raaf.rickandmorty.di.modules.CharactersPagingSourceModule
import com.raaf.rickandmorty.viewModels.CharactersViewModel
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Component(modules = arrayOf(
    ApiServiceModule::class,
    CharactersPagingSourceModule::class))
@Singleton
interface AppComponent {

    @Component.Builder
    interface Builder {
        fun build() : AppComponent
    }

    fun charactersViewModel() : CharactersViewModel.Factory
}