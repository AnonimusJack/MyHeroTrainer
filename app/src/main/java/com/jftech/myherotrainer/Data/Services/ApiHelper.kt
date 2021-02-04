package com.jftech.myherotrainer.Data.Services

import com.jftech.myherotrainer.Data.Models.LoginForm
import com.jftech.myherotrainer.Data.Models.RegisterForm

class ApiHelper(private val heroApi: HeroesApiService, private val userApi: UserApiService)
{
    suspend fun RegisterTrainer(body: RegisterForm) = userApi.RegisterTrainer(body)
    suspend fun LoginTrainer(body: LoginForm) = userApi.LoginTrainer(body)
    suspend fun SyncRequired(token: String, date: String) = heroApi.SyncRequired(token, date)
    suspend fun Sync(token: String) = heroApi.Sync(token)
    suspend fun TrainHero(token: String, id: String) = heroApi.TrainHero(token, id)
}