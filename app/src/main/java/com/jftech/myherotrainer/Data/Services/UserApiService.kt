package com.jftech.myherotrainer.Data.Services

import com.google.gson.JsonObject
import com.jftech.myherotrainer.Data.Models.AuthToken
import com.jftech.myherotrainer.Data.Models.LoginForm
import com.jftech.myherotrainer.Data.Models.RegisterForm
import com.jftech.myherotrainer.Data.Models.ServerResponse
import org.json.JSONObject
import retrofit2.http.Body
import retrofit2.http.POST

interface UserApiService
{
    @POST("auth/register")
    suspend fun RegisterTrainer(@Body body: RegisterForm): ServerResponse
    @POST("auth/login")
    suspend fun LoginTrainer(@Body body: LoginForm): AuthToken
}