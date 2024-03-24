package com.oztechan.ccc.client.storage.calculation

interface CalculationStorage {
    suspend fun getBase(): String
    suspend fun setBase(value: String)

    suspend fun getPrecision(): Int
    suspend fun setPrecision(value: Int)

    suspend fun getLastInput(): String
    suspend fun setLastInput(value: String)
}
