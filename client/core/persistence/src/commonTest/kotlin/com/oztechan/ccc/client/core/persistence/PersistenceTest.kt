package com.oztechan.ccc.client.core.persistence

import com.oztechan.ccc.client.core.persistence.error.UnsupportedPersistenceException
import com.russhwolf.settings.Settings
import io.mockative.Mock
import io.mockative.classOf
import io.mockative.configure
import io.mockative.given
import io.mockative.mock
import io.mockative.verify
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

internal class PersistenceTest {
    private val persistence: Persistence by lazy {
        PersistenceImpl(settings)
    }

    @Mock
    private val settings = configure(mock(classOf<Settings>())) { stubsUnitByDefault = true }

    private val key = "key"

    private val mockFloat = Random.nextFloat()
    private val mockBoolean = Random.nextBoolean()
    private val mockInt = Random.nextInt()
    private val mockString = Random.nextInt().toString()
    private val mockLong = Random.nextLong()

    @Test
    fun `getValue returns the same type`() {
        given(settings)
            .invocation { getFloat(key, mockFloat) }
            .thenReturn(mockFloat)
        given(settings)
            .invocation { getBoolean(key, mockBoolean) }
            .thenReturn(mockBoolean)
        given(settings)
            .invocation { getInt(key, mockInt) }
            .thenReturn(mockInt)
        given(settings)
            .invocation { getString(key, mockString) }
            .thenReturn(mockString)
        given(settings)
            .invocation { getLong(key, mockLong) }
            .thenReturn(mockLong)

        assertEquals(mockFloat, persistence.getValue(key, mockFloat))
        assertEquals(mockBoolean, persistence.getValue(key, mockBoolean))
        assertEquals(mockInt, persistence.getValue(key, mockInt))
        assertEquals(mockString, persistence.getValue(key, mockString))
        assertEquals(mockLong, persistence.getValue(key, mockLong))

        verify(settings)
            .invocation { settings.getFloat(key, mockFloat) }
            .wasInvoked()
        verify(settings)
            .invocation { settings.getBoolean(key, mockBoolean) }
            .wasInvoked()
        verify(settings)
            .invocation { settings.getInt(key, mockInt) }
            .wasInvoked()
        verify(settings)
            .invocation { settings.getString(key, mockString) }
            .wasInvoked()
        verify(settings)
            .invocation { settings.getLong(key, mockLong) }
            .wasInvoked()
    }

    @Test
    fun `setValue sets the same type`() {
        persistence.setValue(key, mockFloat)
        persistence.setValue(key, mockBoolean)
        persistence.setValue(key, mockInt)
        persistence.setValue(key, mockString)
        persistence.setValue(key, mockLong)

        verify(settings)
            .invocation { settings.putFloat(key, mockFloat) }
            .wasInvoked()
        verify(settings)
            .invocation { settings.putBoolean(key, mockBoolean) }
            .wasInvoked()
        verify(settings)
            .invocation { settings.putInt(key, mockInt) }
            .wasInvoked()
        verify(settings)
            .invocation { settings.putString(key, mockString) }
            .wasInvoked()
        verify(settings)
            .invocation { settings.putLong(key, mockLong) }
            .wasInvoked()
    }

    @Test
    fun `setValue throw UnsupportedPersistenceException when unsupported type tried to saved or read`() {
        val mockObject = object {}

        assertFailsWith(UnsupportedPersistenceException::class) {
            persistence.setValue(key, mockObject)
        }
        assertFailsWith(UnsupportedPersistenceException::class) {
            persistence.getValue(key, mockObject)
        }
    }
}
