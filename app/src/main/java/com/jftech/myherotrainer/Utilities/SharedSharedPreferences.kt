package com.jftech.myherotrainer.Utilities

import android.content.SharedPreferences

class SharedSharedPreferences
{
    private var sharedPreferences: SharedPreferences? = null
    val SharedPreferences: SharedPreferences
        get()
        {
            return sharedPreferences as SharedPreferences
        }

    fun Initialize(sharedPreferences: SharedPreferences)
    {
        this.sharedPreferences = sharedPreferences
    }

    companion object
    {
        private var sharedInstance: SharedSharedPreferences? = null
        val SharedInstance: SharedSharedPreferences
            get()
            {
                if (sharedInstance == null)
                    sharedInstance = SharedSharedPreferences()
                return sharedInstance as SharedSharedPreferences
            }
    }
}