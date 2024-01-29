package com.oztechan.ccc.client.storage.calculation

import com.oztechan.ccc.client.core.persistence.SuspendPersistence

class CalculationStorageImpl(
    private val suspendPersistence: SuspendPersistence
) : CalculationStorage {

    override suspend fun getBase(): String =
        suspendPersistence.getSuspend(KEY_CURRENT_BASE, DEFAULT_CURRENT_BASE)

    override suspend fun setBase(value: String) =
        suspendPersistence.setSuspend(KEY_CURRENT_BASE, value)

    override suspend fun getPrecision(): Int =
        suspendPersistence.getSuspend(KEY_PRECISION, DEFAULT_PRECISION)

    override suspend fun setPrecision(value: Int) =
        suspendPersistence.setSuspend(KEY_PRECISION, value)

    override suspend fun getLastInput(): String =
        suspendPersistence.getSuspend(KEY_LAST_INPUT, DEFAULT_LAST_INPUT)

    override suspend fun setLastInput(value: String) =
        suspendPersistence.setSuspend(KEY_LAST_INPUT, value)

    companion object {
        internal const val DEFAULT_CURRENT_BASE = ""
        internal const val DEFAULT_PRECISION = 3
        internal const val DEFAULT_LAST_INPUT = ""

        internal const val KEY_CURRENT_BASE = "current_base"
        internal const val KEY_PRECISION = "precision"
        internal const val KEY_LAST_INPUT = "last_input"
    }
}
