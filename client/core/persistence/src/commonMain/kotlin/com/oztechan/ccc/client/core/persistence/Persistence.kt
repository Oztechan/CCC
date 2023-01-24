package com.oztechan.ccc.client.core.persistence

interface Persistence {
    fun <T : Any> getValue(key: String, defaultValue: T): T
    fun <T : Any> setValue(key: String, value: T)
}
