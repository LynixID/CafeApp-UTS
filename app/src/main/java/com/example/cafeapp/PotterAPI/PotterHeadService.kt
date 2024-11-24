package com.example.cafeapp.PotterAPI

import retrofit2.http.GET
import retrofit2.http.Path

interface PotterHeadService {
    @GET("{lang}/books")
    suspend fun getBooks(@Path ("lang")language: String = "en")

    @GET("{lang}/characters")
    suspend fun getChars(@Path ("lang")language: String = "en")

    @GET("{lang}/houses")
    suspend fun getHouses(@Path ("lang")language: String = "en")

    @GET("{lang}/spells")
    suspend fun getSpells(@Path ("lang")language: String = "en")
}