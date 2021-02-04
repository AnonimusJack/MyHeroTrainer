package com.jftech.myherotrainer.Data.Models

import com.google.gson.annotations.SerializedName

data class ServerResponse(@SerializedName("status") val Status: String, @SerializedName("message") val Message: String)
{
    constructor(): this("", "")
}