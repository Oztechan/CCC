package com.oztechan.ccc.common.error

internal open class BaseNetworkException(cause: Throwable) : Throwable(cause.message, cause)
