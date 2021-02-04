package com.jftech.myherotrainer.Utilities

enum class MHTExceptions
{
    ServerConnectionTimedOutException,
    NoInternetConnectionException,
    RealmInitializationException,
    InvalidCredentialsException,
    UserAlreadyExistsException,
    HeroOverTrainingException,
    InternalServerException,
    InvalidInputException,
    EmptyTokenException;
    fun GetErrorTitle(): String
    {
        return when(this)
        {
            ServerConnectionTimedOutException -> "Server Connection Time Out"
            NoInternetConnectionException -> "No Internet Connection Detected"
            RealmInitializationException -> "Realm Initialization Error"
            InvalidCredentialsException -> "Invalid Credentials Error"
            UserAlreadyExistsException -> "User Already Exists"
            HeroOverTrainingException -> " Hero is Over Trained"
            InternalServerException -> "Internal Server Error"
            InvalidInputException -> "Invalid Input Error"
            EmptyTokenException -> "Empty Token Error"
        }
    }

    fun GetErrorMessage(): String
    {
        return when(this)
        {
            ServerConnectionTimedOutException -> "Server is offline or in maintenance please try again later"
            NoInternetConnectionException -> "You have no active internet connection, please check your settings and try again."
            RealmInitializationException -> "Internal Error, please restart the app."
            InvalidCredentialsException -> "Username or Password are invalid please try again."
            UserAlreadyExistsException -> "This username is already exists please try again."
            HeroOverTrainingException -> "This hero is over trained, please try again after 6am."
            InternalServerException -> "A server error has occurred please try again later."
            InvalidInputException -> "One of your inputs is invalid, please check them and try again."
            EmptyTokenException -> "Your authentication token is empty, please re-login."
        }
    }
}