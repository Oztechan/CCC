package com.oztechan.ccc.common.error

class DatabaseException(cause: Throwable) : Throwable(cause.message, cause)
