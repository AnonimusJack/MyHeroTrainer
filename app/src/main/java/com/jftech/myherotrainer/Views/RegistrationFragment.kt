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
import com.jftech.myherotrainer.Utilities.UserDataValidation

class RegistrationFragment: Fragment()
{
    private lateinit var viewModel: HeroTrainerViewModel
    private lateinit var usernameTextEdit: AppCompatEditText
    private lateinit var emailTextEdit: AppCompatEditText
    private lateinit var passwordTextEdit: AppCompatEditText
    private lateinit var confirmPasswordTextEdit: AppCompatEditText
    private lateinit var registerButton: AppCompatButton
    private lateinit var cancelButton: AppCompatButton


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        setupViewModel()
        view.post { setupUI() }
    }

    private fun setupUI()
    {
        wireViews()
        registerButton.setOnClickListener { onRegisterButtonClick() }
        cancelButton.setOnClickListener { onCancelButtonClick() }
    }

    private fun wireViews()
    {
        usernameTextEdit = view!!.findViewById(R.id.register_username_textedit)
        emailTextEdit = view!!.findViewById(R.id.register_email_textedit)
        passwordTextEdit = view!!.findViewById(R.id.register_password_textedit)
        confirmPasswordTextEdit = view!!.findViewById(R.id.register_confirm_password_textedit)
        registerButton = view!!.findViewById(R.id.register_register_button)
        cancelButton = view!!.findViewById(R.id.register_cancel_button)
    }

    private fun onRegisterButtonClick()
    {
        val username = usernameTextEdit.text.toString()
        val email = emailTextEdit.text.toString()
        val password = passwordTextEdit.text.toString()
        val confirmedPassword = confirmPasswordTextEdit.text.toString()
        val validator = UserDataValidation()
        val validInput = validator.ValidateUsername(username) && validator.ValidateEmail(email) && validator.ValidatePassword(password, confirmedPassword)
        if (validInput)
            viewModel.Register(username, email, password)
        else
            viewModel.RaiseException(MHTExceptions.InvalidInputException)
    }

    private fun onCancelButtonClick()
    {
        viewModel.SwitchFragment(Fragments.Login)
    }

    private fun setupViewModel()
    {
        viewModel = ViewModelProvider(requireActivity()).get(HeroTrainerViewModel::class.java)
    }
}