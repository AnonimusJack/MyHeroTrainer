package com.jftech.myherotrainer.Data.Services

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitBuilder
{
    private const val BASE_URL = "http://192.168.56.1:5000/api/"
    private const val IMAGE_URL = "http://192.168.56.1:5000/images/"

    private fun getRetrofit(): Retrofit
    {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val HeroesApi: HeroesApiService = getRetrofit().create(HeroesApiService::class.java)
    val UserApi: UserApiService = getRetrofit().create(UserApiService::class.java)

    fun ImageUrlForHero(id: String): String
    {
        return "$IMAGE_URL$id.jpg"
    }
}