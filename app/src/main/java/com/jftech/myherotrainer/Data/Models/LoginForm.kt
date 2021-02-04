package com.jftech.myherotrainer.Data.Models

import com.google.gson.annotations.SerializedName

data class LoginForm(@SerializedName("username") val username: String, @SerializedName("password") val password: String)
{
    constructor(): this("", "")
}