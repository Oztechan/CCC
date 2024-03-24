package com.oztechan.ccc.client.storage.calculation

interface CalculationStorage {
    var currentBase: String

    suspend fun getPrecision(): Int
    suspend fun setPrecision(value: Int)

    suspend fun getLastInput(): String
    suspend fun setLastInput(value: String)
}
