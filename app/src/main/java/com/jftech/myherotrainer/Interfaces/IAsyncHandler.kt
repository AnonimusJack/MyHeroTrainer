package com.jftech.myherotrainer.Interfaces

import com.jftech.myherotrainer.Utilities.MHTExceptions
import com.jftech.myherotrainer.Utilities.PayloadType

interface IAsyncHandler
{
    fun HandleError(error: MHTExceptions)
    fun HandleSuccess(data: Any, payloadType: PayloadType)
}