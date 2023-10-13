package com.oztechan.ccc.client.core.persistence

import com.oztechan.ccc.client.core.persistence.error.UnsupportedPersistenceException
import com.russhwolf.settings.ObservableSettings
import io.mockative.Mock
import io.mockative.classOf
import io.mockative.configure
import io.mockative.every
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
    private val settings = configure(mock(classOf<ObservableSettings>())) {
        stubsUnitByDefault = true
    }

    private val key = "key"

    private val mockFloat = Random.nextFloat()
    private val mockBoolean = Random.nextBoolean()
    private val mockInt = Random.nextInt()
    private val mockString = Random.nextInt().toString()
    private val mockLong = Random.nextLong()

    @Test
    fun `getValue returns the same type`() {
        every { settings.getFloat(key, mockFloat) }
            .returns(mockFloat)
        every { settings.getBoolean(key, mockBoolean) }
            .returns(mockBoolean)
        every { settings.getInt(key, mockInt) }
            .returns(mockInt)
        every { settings.getString(key, mockString) }
            .returns(mockString)
        every { settings.getLong(key, mockLong) }
            .returns(mockLong)

        assertEquals(mockFloat, persistence.getValue(key, mockFloat))
        assertEquals(mockBoolean, persistence.getValue(key, mockBoolean))
        assertEquals(mockInt, persistence.getValue(key, mockInt))
        assertEquals(mockString, persistence.getValue(key, mockString))
        assertEquals(mockLong, persistence.getValue(key, mockLong))

        verify { settings.getFloat(key, mockFloat) }
            .wasInvoked()
        verify { settings.getBoolean(key, mockBoolean) }
            .wasInvoked()
        verify { settings.getInt(key, mockInt) }
            .wasInvoked()
        verify { settings.getString(key, mockString) }
            .wasInvoked()
        verify { settings.getLong(key, mockLong) }
            .wasInvoked()
    }

    @Test
    fun `setValue sets the same type`() {
        persistence.setValue(key, mockFloat)
        persistence.setValue(key, mockBoolean)
        persistence.setValue(key, mockInt)
        persistence.setValue(key, mockString)
        persistence.setValue(key, mockLong)

        verify { settings.putFloat(key, mockFloat) }
            .wasInvoked()
        verify { settings.putBoolean(key, mockBoolean) }
            .wasInvoked()
        verify { settings.putInt(key, mockInt) }
            .wasInvoked()
        verify { settings.putString(key, mockString) }
            .wasInvoked()
        verify { settings.putLong(key, mockLong) }
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
