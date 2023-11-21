package com.oztechan.ccc.client.core.persistence

import com.oztechan.ccc.client.core.persistence.error.UnsupportedPersistenceException
import com.oztechan.ccc.client.core.persistence.fakes.Fakes.KEY
import com.oztechan.ccc.client.core.persistence.fakes.Fakes.mockBoolean
import com.oztechan.ccc.client.core.persistence.fakes.Fakes.mockFloat
import com.oztechan.ccc.client.core.persistence.fakes.Fakes.mockInt
import com.oztechan.ccc.client.core.persistence.fakes.Fakes.mockLong
import com.oztechan.ccc.client.core.persistence.fakes.Fakes.mockString
import com.russhwolf.settings.coroutines.SuspendSettings
import io.mockative.Mock
import io.mockative.classOf
import io.mockative.coEvery
import io.mockative.coVerify
import io.mockative.configure
import io.mockative.mock
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

@Suppress("OPT_IN_USAGE")
internal class SuspendPersistenceTest {
    private val suspendPersistence: SuspendPersistence by lazy {
        SuspendPersistenceImpl(suspendSettings)
    }

    @Mock
    private val suspendSettings = configure(mock(classOf<SuspendSettings>())) {
        stubsUnitByDefault = true
    }

    @Test
    fun `getSuspend returns the same type`() = runTest {
        coEvery { suspendSettings.getFloat(KEY, mockFloat) }
            .returns(mockFloat)
        coEvery { suspendSettings.getBoolean(KEY, mockBoolean) }
            .returns(mockBoolean)
        coEvery { suspendSettings.getInt(KEY, mockInt) }
            .returns(mockInt)
        coEvery { suspendSettings.getString(KEY, mockString) }
            .returns(mockString)
        coEvery { suspendSettings.getLong(KEY, mockLong) }
            .returns(mockLong)

        assertEquals(mockFloat, suspendPersistence.getSuspend(KEY, mockFloat))
        assertEquals(mockBoolean, suspendPersistence.getSuspend(KEY, mockBoolean))
        assertEquals(mockInt, suspendPersistence.getSuspend(KEY, mockInt))
        assertEquals(mockString, suspendPersistence.getSuspend(KEY, mockString))
        assertEquals(mockLong, suspendPersistence.getSuspend(KEY, mockLong))

        coVerify { suspendSettings.getFloat(KEY, mockFloat) }
            .wasInvoked()
        coVerify { suspendSettings.getBoolean(KEY, mockBoolean) }
            .wasInvoked()
        coVerify { suspendSettings.getInt(KEY, mockInt) }
            .wasInvoked()
        coVerify { suspendSettings.getString(KEY, mockString) }
            .wasInvoked()
        coVerify { suspendSettings.getLong(KEY, mockLong) }
            .wasInvoked()
    }

    @Test
    fun `setSuspend sets the same type`() = runTest {
        suspendPersistence.setSuspend(KEY, mockFloat)
        suspendPersistence.setSuspend(KEY, mockBoolean)
        suspendPersistence.setSuspend(KEY, mockInt)
        suspendPersistence.setSuspend(KEY, mockString)
        suspendPersistence.setSuspend(KEY, mockLong)

        coVerify { suspendSettings.putFloat(KEY, mockFloat) }
            .wasInvoked()
        coVerify { suspendSettings.putBoolean(KEY, mockBoolean) }
            .wasInvoked()
        coVerify { suspendSettings.putInt(KEY, mockInt) }
            .wasInvoked()
        coVerify { suspendSettings.putString(KEY, mockString) }
            .wasInvoked()
        coVerify { suspendSettings.putLong(KEY, mockLong) }
            .wasInvoked()
    }

    @Test
    fun `UnsupportedPersistenceException throw when unsupported type tried to saved or read`() =
        runTest {
            val mockObject = object {}

            assertFailsWith(UnsupportedPersistenceException::class) {
                suspendPersistence.setSuspend(KEY, mockObject)
            }
            assertFailsWith(UnsupportedPersistenceException::class) {
                suspendPersistence.getSuspend(KEY, mockObject)
            }
        }
}
