package com.oztechan.ccc.client.storage.calculation

import com.oztechan.ccc.client.core.persistence.Persistence
import com.oztechan.ccc.client.storage.calculation.CalculationStorageImpl.Companion.DEFAULT_CURRENT_BASE
import com.oztechan.ccc.client.storage.calculation.CalculationStorageImpl.Companion.DEFAULT_LAST_INPUT
import com.oztechan.ccc.client.storage.calculation.CalculationStorageImpl.Companion.DEFAULT_PRECISION
import com.oztechan.ccc.client.storage.calculation.CalculationStorageImpl.Companion.KEY_CURRENT_BASE
import com.oztechan.ccc.client.storage.calculation.CalculationStorageImpl.Companion.KEY_LAST_INPUT
import com.oztechan.ccc.client.storage.calculation.CalculationStorageImpl.Companion.KEY_PRECISION
import dev.mokkery.MockMode
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.mock
import dev.mokkery.verify
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals

internal class CalculationStorageTest {
    private val subject: CalculationStorage by lazy {
        CalculationStorageImpl(persistence)
    }

    private val persistence = mock<Persistence>(MockMode.autoUnit)

    // defaults
    @Test
    fun `default currentBase`() {
        every { persistence.getValue(KEY_CURRENT_BASE, DEFAULT_CURRENT_BASE) }
            .returns(DEFAULT_CURRENT_BASE)

        assertEquals(DEFAULT_CURRENT_BASE, subject.currentBase)

        verify { persistence.getValue(KEY_CURRENT_BASE, DEFAULT_CURRENT_BASE) }
    }

    @Test
    fun `default precision`() {
        every { persistence.getValue(KEY_PRECISION, DEFAULT_PRECISION) }
            .returns(DEFAULT_PRECISION)

        assertEquals(DEFAULT_PRECISION, subject.precision)

        verify { persistence.getValue(KEY_PRECISION, DEFAULT_PRECISION) }
    }

    @Test
    fun `default lastInput`() {
        every { persistence.getValue(KEY_LAST_INPUT, DEFAULT_LAST_INPUT) }
            .returns(DEFAULT_LAST_INPUT)

        assertEquals(DEFAULT_LAST_INPUT, subject.lastInput)

        verify { persistence.getValue(KEY_LAST_INPUT, DEFAULT_LAST_INPUT) }
    }

    // setters
    @Test
    fun `set currentBase`() {
        val mockValue = "mock"
        subject.currentBase = mockValue

        verify { persistence.setValue(KEY_CURRENT_BASE, mockValue) }
    }

    @Test
    fun `set precision`() {
        val mockValue = Random.nextInt()
        subject.precision = mockValue

        verify { persistence.setValue(KEY_PRECISION, mockValue) }
    }

    @Test
    fun `set lastInput`() {
        val mockValue = "mock"
        subject.lastInput = mockValue

        verify { persistence.setValue(KEY_LAST_INPUT, mockValue) }
    }
}
