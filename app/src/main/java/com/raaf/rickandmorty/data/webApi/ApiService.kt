package com.raaf.rickandmorty.data.webApi

import com.raaf.rickandmorty.data.webApi.responseModels.CharactersResponse
import com.raaf.rickandmorty.data.dataModels.Episode
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("character/")
    suspend fun getCharacters(@Query("page") page: Int) : CharactersResponse

    @GET("episode/{ids}")
    suspend fun getEpisodesById(@Path("ids") ids: String) : List<Episode>

    @GET("episode/{id}")
    suspend fun getEpisodeById(@Path("id") id: String) : Episode

    companion object {
        private const val BASE_URL = "https://rickandmortyapi.com/api/"

        fun create() : ApiService {
            val okHttpClient = OkHttpClient.Builder()
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()

            return retrofit.create(ApiService::class.java)
        }
    }
}