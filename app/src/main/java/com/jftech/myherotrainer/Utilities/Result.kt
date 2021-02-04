package com.jftech.myherotrainer.Utilities

data class Result<out T>(val status: Status, val data: T?, val message: String?)
{
    companion object
    {
        fun <T> success(data: T): Result<T> = Result(status = Status.Success, data = data, message = null)
        fun <T> error(data: T?, message: String): Result<T> = Result(status = Status.Error, data = data, message = message)
        fun <T> loading(data: T?): Result<T> = Result(status = Status.Loading, data = data, message = null)
    }
}
