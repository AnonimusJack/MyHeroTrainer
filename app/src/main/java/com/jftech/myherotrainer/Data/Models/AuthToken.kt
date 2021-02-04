package com.jftech.myherotrainer.Data.Models

import com.google.gson.annotations.SerializedName

data class AuthToken(@SerializedName("token") val Token: String)
{
    constructor(): this("")
}