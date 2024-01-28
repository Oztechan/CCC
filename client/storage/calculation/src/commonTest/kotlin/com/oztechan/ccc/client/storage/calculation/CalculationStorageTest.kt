package com.oztechan.ccc.client.storage.calculation

import com.oztechan.ccc.client.core.persistence.Persistence
import com.oztechan.ccc.client.core.persistence.SuspendPersistence
import com.oztechan.ccc.client.storage.calculation.CalculationStorageImpl.Companion.DEFAULT_CURRENT_BASE
import com.oztechan.ccc.client.storage.calculation.CalculationStorageImpl.Companion.DEFAULT_LAST_INPUT
import com.oztechan.ccc.client.storage.calculation.CalculationStorageImpl.Companion.DEFAULT_PRECISION
import com.oztechan.ccc.client.storage.calculation.CalculationStorageImpl.Companion.KEY_CURRENT_BASE
import com.oztechan.ccc.client.storage.calculation.CalculationStorageImpl.Companion.KEY_LAST_INPUT
import com.oztechan.ccc.client.storage.calculation.CalculationStorageImpl.Companion.KEY_PRECISION
import io.mockative.Mock
import io.mockative.classOf
import io.mockative.coEvery
import io.mockative.coVerify
import io.mockative.configure
import io.mockative.every
import io.mockative.mock
import io.mockative.verify
import kotlinx.coroutines.test.runTest
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals

internal class CalculationStorageTest {
    private val subject: CalculationStorage by lazy {
        CalculationStorageImpl(persistence, suspendPersistence)
    }

    @Mock
    private val persistence = configure(mock(classOf<Persistence>())) { stubsUnitByDefault = true }

    @Mock
    private val suspendPersistence = mock(classOf<SuspendPersistence>())

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
    fun `get default lastInput`() = runTest {
        coEvery { suspendPersistence.getSuspend(KEY_LAST_INPUT, DEFAULT_LAST_INPUT) }
            .returns(DEFAULT_LAST_INPUT)

        assertEquals(DEFAULT_LAST_INPUT, subject.getLastInput())

        coVerify { suspendPersistence.getSuspend(KEY_LAST_INPUT, DEFAULT_LAST_INPUT) }
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
    fun `set lastInput`() = runTest {
        val mockValue = "mock"
        subject.setLastInput(mockValue)

        coVerify { suspendPersistence.setSuspend(KEY_LAST_INPUT, mockValue) }
            .wasInvoked()
    }
}
