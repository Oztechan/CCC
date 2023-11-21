package com.oztechan.ccc.client.core.persistence

import com.oztechan.ccc.client.core.persistence.error.UnsupportedPersistenceException
import com.oztechan.ccc.client.core.persistence.fakes.Fakes.KEY
import com.oztechan.ccc.client.core.persistence.fakes.Fakes.mockBoolean
import com.oztechan.ccc.client.core.persistence.fakes.Fakes.mockFloat
import com.oztechan.ccc.client.core.persistence.fakes.Fakes.mockInt
import com.oztechan.ccc.client.core.persistence.fakes.Fakes.mockLong
import com.oztechan.ccc.client.core.persistence.fakes.Fakes.mockString
import com.russhwolf.settings.Settings
import io.mockative.Mock
import io.mockative.classOf
import io.mockative.configure
import io.mockative.every
import io.mockative.mock
import io.mockative.verify
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

internal class PersistenceTest {
    private val persistence: Persistence by lazy {
        PersistenceImpl(settings)
    }

    @Mock
    private val settings = configure(mock(classOf<Settings>())) {
        stubsUnitByDefault = true
    }

    @Test
    fun `getValue returns the same type`() {
        every { settings.getFloat(KEY, mockFloat) }
            .returns(mockFloat)
        every { settings.getBoolean(KEY, mockBoolean) }
            .returns(mockBoolean)
        every { settings.getInt(KEY, mockInt) }
            .returns(mockInt)
        every { settings.getString(KEY, mockString) }
            .returns(mockString)
        every { settings.getLong(KEY, mockLong) }
            .returns(mockLong)

        assertEquals(mockFloat, persistence.getValue(KEY, mockFloat))
        assertEquals(mockBoolean, persistence.getValue(KEY, mockBoolean))
        assertEquals(mockInt, persistence.getValue(KEY, mockInt))
        assertEquals(mockString, persistence.getValue(KEY, mockString))
        assertEquals(mockLong, persistence.getValue(KEY, mockLong))

        verify { settings.getFloat(KEY, mockFloat) }
            .wasInvoked()
        verify { settings.getBoolean(KEY, mockBoolean) }
            .wasInvoked()
        verify { settings.getInt(KEY, mockInt) }
            .wasInvoked()
        verify { settings.getString(KEY, mockString) }
            .wasInvoked()
        verify { settings.getLong(KEY, mockLong) }
            .wasInvoked()
    }

    @Test
    fun `setValue sets the same type`() {
        persistence.setValue(KEY, mockFloat)
        persistence.setValue(KEY, mockBoolean)
        persistence.setValue(KEY, mockInt)
        persistence.setValue(KEY, mockString)
        persistence.setValue(KEY, mockLong)

        verify { settings.putFloat(KEY, mockFloat) }
            .wasInvoked()
        verify { settings.putBoolean(KEY, mockBoolean) }
            .wasInvoked()
        verify { settings.putInt(KEY, mockInt) }
            .wasInvoked()
        verify { settings.putString(KEY, mockString) }
            .wasInvoked()
        verify { settings.putLong(KEY, mockLong) }
            .wasInvoked()
    }

    @Test
    fun `UnsupportedPersistenceException throw when unsupported type tried to saved or read`() {
        val mockObject = object {}

        assertFailsWith(UnsupportedPersistenceException::class) {
            persistence.setValue(KEY, mockObject)
        }
        assertFailsWith(UnsupportedPersistenceException::class) {
            persistence.getValue(KEY, mockObject)
        }
    }
}
