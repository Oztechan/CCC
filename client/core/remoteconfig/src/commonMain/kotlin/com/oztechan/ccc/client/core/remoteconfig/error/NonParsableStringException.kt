package com.oztechan.ccc.client.core.remoteconfig.error

class NonParsableStringException(string: String?, exception: Exception) :
    Exception("Non Parsable string: \"$string\"", cause = exception)
