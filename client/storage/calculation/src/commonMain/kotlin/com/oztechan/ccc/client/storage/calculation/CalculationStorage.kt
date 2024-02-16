package com.oztechan.ccc.client.storage.calculation

import kotlinx.coroutines.flow.Flow

interface CalculationStorage {
    fun getBaseFlow(): Flow<String>
    suspend fun getBase(): String
    suspend fun setBase(value: String)

    fun getLastInputFlow(): Flow<String>
    suspend fun getLastInput(): String
    suspend fun setLastInput(value: String)

    suspend fun getPrecision(): Int
    suspend fun setPrecision(value: Int)
}
