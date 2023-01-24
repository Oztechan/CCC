package com.oztechan.ccc.client.storage.calculator

import com.oztechan.ccc.client.core.persistence.Persistence

class CalculatorStorageImpl(
    private val persistence: Persistence
) : CalculatorStorage {

    override var currentBase
        get() = persistence.getValue(KEY_CURRENT_BASE, DEFAULT_CURRENT_BASE)
        set(value) = persistence.setValue(KEY_CURRENT_BASE, value)

    override var precision: Int
        get() = persistence.getValue(KEY_PRECISION, DEFAULT_PRECISION)
        set(value) = persistence.setValue(KEY_PRECISION, value)

    override var lastInput: String
        get() = persistence.getValue(KEY_LAST_INPUT, DEFAULT_LAST_INPUT)
        set(value) = persistence.setValue(KEY_LAST_INPUT, value)

    companion object {
        internal const val DEFAULT_CURRENT_BASE = ""
        internal const val DEFAULT_PRECISION = 3
        internal const val DEFAULT_LAST_INPUT = ""

        internal const val KEY_CURRENT_BASE = "current_base"
        internal const val KEY_PRECISION = "precision"
        internal const val KEY_LAST_INPUT = "last_input"
    }
}
