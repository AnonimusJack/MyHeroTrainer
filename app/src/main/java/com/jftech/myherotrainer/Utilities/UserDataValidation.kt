package com.jftech.myherotrainer.Utilities

class UserDataValidation
{
    fun ValidateEmail(email: String): Boolean
    {
        val emailRegex = "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})"
        return emailRegex.toRegex().matches(email)
    }

    fun ValidateUsername(username: String): Boolean
    {
        val usernameRegex = "^[A-Za-z]\\w{5,29}$"
        return usernameRegex.toRegex().matches(username)
    }

    fun ValidatePassword(password: String, confirmedPassword: String): Boolean
    {
        val matchingPasswords = password == confirmedPassword
        return validatePasswordRegex(password) && matchingPasswords
    }

    fun ValidatePassword(password: String): Boolean
    {
        return validatePasswordRegex(password)
    }

    private fun validatePasswordRegex(password: String): Boolean
    {
        val passwordRegex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%!\\-_?&])(?=\\S+$).{8,}"
        return passwordRegex.toRegex().matches(password)
    }
}