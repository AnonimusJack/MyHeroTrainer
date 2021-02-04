package com.jftech.myherotrainer.Data.Models

import com.google.gson.annotations.SerializedName

class RegisterForm(@SerializedName("username") val username: String, @SerializedName("email") val email: String, @SerializedName("password") val password: String)
{
    constructor(): this("", "", "")
}