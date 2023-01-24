package com.oztechan.ccc.client.core.persistence.error

class PersistenceException(
    message: String = "SharedPreferences does not support this type."
) : IllegalArgumentException(message)
