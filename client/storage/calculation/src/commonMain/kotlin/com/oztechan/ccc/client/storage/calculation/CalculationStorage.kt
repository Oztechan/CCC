package com.oztechan.ccc.client.storage.calculation

interface CalculationStorage {
    var currentBase: String

    var precision: Int

    suspend fun getLastInput(): String
    suspend fun setLastInput(value: String)
}
