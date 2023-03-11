package com.oztechan.ccc.client.core.persistence.error

class UnsupportedPersistenceException(
    message: String = "Persistence does not support this type."
) : IllegalArgumentException(message)
