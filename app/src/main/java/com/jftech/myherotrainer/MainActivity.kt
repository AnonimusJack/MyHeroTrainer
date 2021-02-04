package com.jftech.myherotrainer

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.jftech.myherotrainer.Data.Services.ApiHelper
import com.jftech.myherotrainer.Data.Services.HeroTrainerViewModel
import com.jftech.myherotrainer.Data.Services.RetrofitBuilder
import com.jftech.myherotrainer.Utilities.Fragments
import com.jftech.myherotrainer.Utilities.MHTExceptions
import com.jftech.myherotrainer.Utilities.SharedConnectivityManager
import com.jftech.myherotrainer.Utilities.SharedSharedPreferences
import io.realm.Realm

class MainActivity : AppCompatActivity()
{
    private lateinit var viewModel: HeroTrainerViewModel

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupSharedInstances()
        Realm.init(this)
        setupViewModel()
        setupObservers()
        viewModel.SwitchFragment(Fragments.Login)
    }


    private fun switchFragment(fragment: Fragment)
    {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
        transaction.commit()
    }

    private fun setupViewModel()
    {
        val viewModelFactory = HeroTrainerViewModel.HeroTrainerViewModelFactory(ApiHelper(RetrofitBuilder.HeroesApi, RetrofitBuilder.UserApi))
        viewModel = ViewModelProvider(this, viewModelFactory).get(HeroTrainerViewModel::class.java)
    }

    private fun setupObservers()
    {
        viewModel.CurrentFragment.observe(this) { if (it != null) switchFragment(it.getFragment()) }
        viewModel.RaisedException.observe(this) { if (it != null) handleRaisedException(it)}
        viewModel.HeroTrainedFlag.observe(this) {
            if (it)
            {
                Toast.makeText(this, "The Hero has been trained!", Toast.LENGTH_SHORT).show()
                viewModel.ResetHeroTrainFlag()
            }
        }
    }

    private fun setupSharedInstances()
    {
        SharedSharedPreferences.SharedInstance.Initialize(getSharedPreferences("HeroTrainer", MODE_PRIVATE))
        SharedConnectivityManager.SharedInstance.Initialize(getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager)
    }

    private fun handleRaisedException(exception: MHTExceptions)
    {
        AlertDialog.Builder(this)
                .setTitle(exception.GetErrorTitle())
                .setMessage(exception.GetErrorMessage())
                .setPositiveButton("Ok") { _: DialogInterface, _: Int -> exceptionFunction(exception) }
                .show()
    }

    private fun exceptionFunction(exception: MHTExceptions)
    {
        when(exception)
        {
            MHTExceptions.NoInternetConnectionException -> startActivity(Intent(Settings.ACTION_SETTINGS))
            MHTExceptions.EmptyTokenException -> {
                SharedSharedPreferences.SharedInstance.SharedPreferences.edit().remove("auth_token").commit()
                viewModel.SwitchFragment(Fragments.Login)
            }
            else -> return
        }
    }
}