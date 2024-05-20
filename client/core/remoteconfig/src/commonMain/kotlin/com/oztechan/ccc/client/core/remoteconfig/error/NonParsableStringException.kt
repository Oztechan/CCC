package com.oztechan.ccc.client.core.remoteconfig.error

class NonParsableStringException(
    string: String?,
    exception: Exception
) : Throwable(
    message = "Non Parsable string: \"$string\"",
    cause = exception
)
