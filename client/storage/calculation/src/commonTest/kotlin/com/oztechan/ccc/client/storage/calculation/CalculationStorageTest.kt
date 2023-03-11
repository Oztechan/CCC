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
import io.mockative.given
import io.mockative.mock
import io.mockative.verify
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals

@Suppress("TooManyFunctions")
internal class CalculationStorageTest {
    private val subject: CalculationStorage by lazy {
        CalculationStorageImpl(persistence)
    }

    @Mock
    private val persistence = configure(mock(classOf<Persistence>())) { stubsUnitByDefault = true }

    // defaults
    @Test
    fun `default currentBase`() {
        given(persistence)
            .invocation { getValue(KEY_CURRENT_BASE, DEFAULT_CURRENT_BASE) }
            .thenReturn(DEFAULT_CURRENT_BASE)

        assertEquals(DEFAULT_CURRENT_BASE, subject.currentBase)

        verify(persistence)
            .invocation { getValue(KEY_CURRENT_BASE, DEFAULT_CURRENT_BASE) }
            .wasInvoked()
    }

    @Test
    fun `default precision`() {
        given(persistence)
            .invocation { getValue(KEY_PRECISION, DEFAULT_PRECISION) }
            .thenReturn(DEFAULT_PRECISION)

        assertEquals(DEFAULT_PRECISION, subject.precision)

        verify(persistence)
            .invocation { getValue(KEY_PRECISION, DEFAULT_PRECISION) }
            .wasInvoked()
    }

    @Test
    fun `default lastInput`() {
        given(persistence)
            .invocation { getValue(KEY_LAST_INPUT, DEFAULT_LAST_INPUT) }
            .thenReturn(DEFAULT_LAST_INPUT)

        assertEquals(DEFAULT_LAST_INPUT, subject.lastInput)

        verify(persistence)
            .invocation { getValue(KEY_LAST_INPUT, DEFAULT_LAST_INPUT) }
            .wasInvoked()
    }

    // setters
    @Test
    fun `set currentBase`() {
        val mockValue = "mock"
        subject.currentBase = mockValue

        verify(persistence)
            .invocation { setValue(KEY_CURRENT_BASE, mockValue) }
            .wasInvoked()
    }

    @Test
    fun `set precision`() {
        val mockValue = Random.nextInt()
        subject.precision = mockValue

        verify(persistence)
            .invocation { setValue(KEY_PRECISION, mockValue) }
            .wasInvoked()
    }

    @Test
    fun `set lastInput`() {
        val mockValue = "mock"
        subject.lastInput = mockValue

        verify(persistence)
            .invocation { setValue(KEY_LAST_INPUT, mockValue) }
            .wasInvoked()
    }
}
