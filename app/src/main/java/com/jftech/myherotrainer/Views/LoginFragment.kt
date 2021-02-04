package com.jftech.myherotrainer.Views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.jftech.myherotrainer.Data.Services.HeroTrainerViewModel
import com.jftech.myherotrainer.R
import com.jftech.myherotrainer.Utilities.Fragments
import com.jftech.myherotrainer.Utilities.MHTExceptions
import com.jftech.myherotrainer.Utilities.SharedSharedPreferences
import com.jftech.myherotrainer.Utilities.UserDataValidation

class LoginFragment: Fragment()
{
    private lateinit var viewModel: HeroTrainerViewModel
    private lateinit var usernameTextEdit: AppCompatEditText
    private lateinit var passwordTextEdit: AppCompatEditText
    private lateinit var loginButton: AppCompatButton
    private lateinit var registerButton: AppCompatButton

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        setupViewModel()
        loginOnTokenExistance()
        view.post { setupUI() }
    }

    override fun onPause()
    {
        super.onPause()
        saveDataToCache()
    }

    private fun setupUI()
    {
        wireViews()
        loadCachedData()
        loginButton.setOnClickListener { onLoginButtonClick() }
        registerButton.setOnClickListener { onRegisterButtonClick() }
    }

    private fun wireViews()
    {
        usernameTextEdit = view!!.findViewById(R.id.login_username_textedit)
        passwordTextEdit = view!!.findViewById(R.id.login_password_textedit)
        loginButton = view!!.findViewById(R.id.login_login_button)
        registerButton = view!!.findViewById(R.id.login_register_button)
    }

    private fun onLoginButtonClick()
    {
        val username = usernameTextEdit.text.toString()
        val password = passwordTextEdit.text.toString()
        val validator = UserDataValidation()
        val validInput = validator.ValidateUsername(username) && validator.ValidatePassword(password)
        if (validInput)
            viewModel.Login(username, password)
        else
            viewModel.RaiseException(MHTExceptions.InvalidInputException)
    }

    private fun onRegisterButtonClick()
    {
        viewModel.SwitchFragment(Fragments.Register)
    }

    private fun setupViewModel()
    {
        viewModel = ViewModelProvider(requireActivity()).get(HeroTrainerViewModel::class.java)
    }

    private fun loginOnTokenExistance()
    {
        val token = SharedSharedPreferences.SharedInstance.SharedPreferences.getString("auth_token", "")!!
        if (token.isNotBlank())
        {
            viewModel.SwitchFragment(Fragments.GridView)
            viewModel.OnUserAuthenticated()
        }
    }

    private fun loadCachedData()
    {
        usernameTextEdit.setText(SharedSharedPreferences.SharedInstance.SharedPreferences.getString("cache_username", "")!!)
        passwordTextEdit.setText(viewModel.PasswordCache)
    }

    private fun saveDataToCache()
    {
        SharedSharedPreferences.SharedInstance.SharedPreferences.edit().putString("cache_username", usernameTextEdit.text.toString()).apply()
        viewModel.PasswordCache = passwordTextEdit.text.toString()
    }
}