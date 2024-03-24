package com.oztechan.ccc.client.storage.calculation

import com.oztechan.ccc.client.core.persistence.Persistence
import com.oztechan.ccc.client.storage.calculation.CalculationStorageImpl.Companion.DEFAULT_CURRENT_BASE
import com.oztechan.ccc.client.storage.calculation.CalculationStorageImpl.Companion.DEFAULT_LAST_INPUT
import com.oztechan.ccc.client.storage.calculation.CalculationStorageImpl.Companion.DEFAULT_PRECISION
import com.oztechan.ccc.client.storage.calculation.CalculationStorageImpl.Companion.KEY_CURRENT_BASE
import com.oztechan.ccc.client.storage.calculation.CalculationStorageImpl.Companion.KEY_LAST_INPUT
import com.oztechan.ccc.client.storage.calculation.CalculationStorageImpl.Companion.KEY_PRECISION
import io.mockative.Mock
import io.mockative.classOf
import io.mockative.configure
import io.mockative.every
import io.mockative.mock
import io.mockative.verify
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals

internal class CalculationStorageTest {
    private val subject: CalculationStorage by lazy {
        CalculationStorageImpl(persistence)
    }

    @Mock
    private val persistence = configure(mock(classOf<Persistence>())) { stubsUnitByDefault = true }

    // defaults
    @Test
    fun `default currentBase`() {
        every { persistence.getValue(KEY_CURRENT_BASE, DEFAULT_CURRENT_BASE) }
            .returns(DEFAULT_CURRENT_BASE)

        assertEquals(DEFAULT_CURRENT_BASE, subject.currentBase)

        verify { persistence.getValue(KEY_CURRENT_BASE, DEFAULT_CURRENT_BASE) }
            .wasInvoked()
    }

    @Test
    fun `default precision`() {
        every { persistence.getValue(KEY_PRECISION, DEFAULT_PRECISION) }
            .returns(DEFAULT_PRECISION)

        assertEquals(DEFAULT_PRECISION, subject.precision)

        verify { persistence.getValue(KEY_PRECISION, DEFAULT_PRECISION) }
            .wasInvoked()
    }

    @Test
    fun `default lastInput`() {
        every { persistence.getValue(KEY_LAST_INPUT, DEFAULT_LAST_INPUT) }
            .returns(DEFAULT_LAST_INPUT)

        assertEquals(DEFAULT_LAST_INPUT, subject.lastInput)

        verify { persistence.getValue(KEY_LAST_INPUT, DEFAULT_LAST_INPUT) }
            .wasInvoked()
    }

    // setters
    @Test
    fun `set currentBase`() {
        val mockValue = "mock"
        subject.currentBase = mockValue

        verify { persistence.setValue(KEY_CURRENT_BASE, mockValue) }
            .wasInvoked()
    }

    @Test
    fun `set precision`() {
        val mockValue = Random.nextInt()
        subject.precision = mockValue

        verify { persistence.setValue(KEY_PRECISION, mockValue) }
            .wasInvoked()
    }

    @Test
    fun `set lastInput`() {
        val mockValue = "mock"
        subject.lastInput = mockValue

        verify { persistence.setValue(KEY_LAST_INPUT, mockValue) }
            .wasInvoked()
    }
}
