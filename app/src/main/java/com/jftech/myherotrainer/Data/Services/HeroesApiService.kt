package com.jftech.myherotrainer.Data.Services

import com.google.gson.JsonArray
import com.jftech.myherotrainer.Data.Models.Hero
import com.jftech.myherotrainer.Data.Models.ServerResponse
import retrofit2.http.*

interface HeroesApiService
{
    @GET("heroes/sync")
    suspend fun Sync(@Header("Authorization") token: String): Array<Hero>
    @GET("heroes/sync/{date}")
    suspend fun SyncRequired(@Header("Authorization") token: String, @Path("date") date: String): ServerResponse
    @PATCH("heroes/train/{id}")
    suspend fun TrainHero(@Header("Authorization") token: String, @Path("id") id: String): ServerResponse
}