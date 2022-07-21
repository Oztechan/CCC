package com.oztechan.ccc.common.error

open class BaseNetworkException(cause: Throwable) : Throwable(cause.message, cause)
