package com.oztechan.ccc.client.storage.calculator

import com.russhwolf.settings.Settings

class CalculatorStorageImpl(
    private val settings: Settings
) : CalculatorStorage {

    override var currentBase
        get() = settings.getString(KEY_CURRENT_BASE, DEFAULT_CURRENT_BASE)
        set(value) = settings.putString(KEY_CURRENT_BASE, value)

    override var precision: Int
        get() = settings.getInt(KEY_PRECISION, DEFAULT_PRECISION)
        set(value) = settings.putInt(KEY_PRECISION, value)

    companion object {
        internal const val DEFAULT_CURRENT_BASE = ""
        internal const val DEFAULT_PRECISION = 3

        internal const val KEY_CURRENT_BASE = "current_base"
        internal const val KEY_PRECISION = "precision"
    }
}
