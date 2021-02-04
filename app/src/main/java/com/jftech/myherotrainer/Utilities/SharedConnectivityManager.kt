package com.jftech.myherotrainer.Utilities

import android.net.ConnectivityManager

class SharedConnectivityManager
{
    private var connectivityManager: ConnectivityManager? = null
    val ConnectivityManager: ConnectivityManager
        get() { return  connectivityManager!! }

    fun Initialize(connectivityManager: ConnectivityManager)
    {
        this.connectivityManager = connectivityManager
    }

    companion object
    {
        private var sharedInstance: SharedConnectivityManager? = null
        val SharedInstance: SharedConnectivityManager
            get()
            {
                if (sharedInstance == null)
                    sharedInstance = SharedConnectivityManager()
                return sharedInstance!!
            }
    }
}