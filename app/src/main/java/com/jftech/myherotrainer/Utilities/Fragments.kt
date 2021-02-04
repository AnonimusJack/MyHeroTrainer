package com.jftech.myherotrainer.Utilities

import androidx.fragment.app.Fragment
import com.jftech.myherotrainer.Views.HeroGridViewFragment
import com.jftech.myherotrainer.Views.HeroViewFragment
import com.jftech.myherotrainer.Views.LoginFragment
import com.jftech.myherotrainer.Views.RegistrationFragment

enum class Fragments
{
    Login,
    Register,
    GridView,
    DetailView;

    fun getFragment(): Fragment
    {
        return when(this)
        {
            Login -> LoginFragment()
            Register -> RegistrationFragment()
            GridView -> HeroGridViewFragment()
            DetailView -> HeroViewFragment()
        }
    }
}