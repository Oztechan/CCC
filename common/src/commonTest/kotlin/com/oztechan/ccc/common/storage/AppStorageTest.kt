/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package com.oztechan.ccc.common.storage

import com.oztechan.ccc.common.storage.AppStorageImpl.Companion.DEFAULT_AD_FREE_END_DATE
import com.oztechan.ccc.common.storage.AppStorageImpl.Companion.DEFAULT_APP_THEME
import com.oztechan.ccc.common.storage.AppStorageImpl.Companion.DEFAULT_CURRENT_BASE
import com.oztechan.ccc.common.storage.AppStorageImpl.Companion.DEFAULT_FIRST_RUN
import com.oztechan.ccc.common.storage.AppStorageImpl.Companion.DEFAULT_PRECISION
import com.oztechan.ccc.common.storage.AppStorageImpl.Companion.DEFAULT_SESSION_COUNT
import com.oztechan.ccc.common.storage.AppStorageImpl.Companion.KEY_AD_FREE_END_DATE
import com.oztechan.ccc.common.storage.AppStorageImpl.Companion.KEY_APP_THEME
import com.oztechan.ccc.common.storage.AppStorageImpl.Companion.KEY_CURRENT_BASE
import com.oztechan.ccc.common.storage.AppStorageImpl.Companion.KEY_FIRST_RUN
import com.oztechan.ccc.common.storage.AppStorageImpl.Companion.KEY_PRECISION
import com.oztechan.ccc.common.storage.AppStorageImpl.Companion.KEY_SESSION_COUNT
import com.oztechan.ccc.test.BaseSubjectTest
import com.russhwolf.settings.Settings
import io.mockative.Mock
import io.mockative.classOf
import io.mockative.given
import io.mockative.mock
import io.mockative.verify
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals

@Suppress("TooManyFunctions")
internal class AppStorageTest : BaseSubjectTest<AppStorage>() {

    override val subject: AppStorage by lazy {
        AppStorageImpl(settings)
    }

    @Mock
    private val settings = mock(classOf<Settings>())

    // defaults
    @Test
    fun default_firstRun() {
        given(settings)
            .invocation { getBoolean(KEY_FIRST_RUN, DEFAULT_FIRST_RUN) }
            .thenReturn(DEFAULT_FIRST_RUN)

        assertEquals(DEFAULT_FIRST_RUN, subject.firstRun)

        verify(settings)
            .invocation { getBoolean(KEY_FIRST_RUN, DEFAULT_FIRST_RUN) }
            .wasInvoked()
    }

    @Test
    fun default_currentBase() {
        given(settings)
            .invocation { getString(KEY_CURRENT_BASE, DEFAULT_CURRENT_BASE) }
            .thenReturn(DEFAULT_CURRENT_BASE)

        assertEquals(DEFAULT_CURRENT_BASE, subject.currentBase)

        verify(settings)
            .invocation { getString(KEY_CURRENT_BASE, DEFAULT_CURRENT_BASE) }
            .wasInvoked()
    }

    @Test
    fun default_appTheme() {
        given(settings)
            .invocation { getInt(KEY_APP_THEME, DEFAULT_APP_THEME) }
            .thenReturn(DEFAULT_APP_THEME)

        assertEquals(DEFAULT_APP_THEME, subject.appTheme)

        verify(settings)
            .invocation { getInt(KEY_APP_THEME, DEFAULT_APP_THEME) }
            .wasInvoked()
    }

    @Test
    fun default_adFreeEndDate() {
        given(settings)
            .invocation { getLong(KEY_AD_FREE_END_DATE, DEFAULT_AD_FREE_END_DATE) }
            .thenReturn(DEFAULT_AD_FREE_END_DATE)

        assertEquals(DEFAULT_AD_FREE_END_DATE, subject.adFreeEndDate)

        verify(settings)
            .invocation { getLong(KEY_AD_FREE_END_DATE, DEFAULT_AD_FREE_END_DATE) }
            .wasInvoked()
    }

    @Test
    fun default_sessionCount() {
        given(settings)
            .invocation { getLong(KEY_SESSION_COUNT, DEFAULT_SESSION_COUNT) }
            .thenReturn(DEFAULT_SESSION_COUNT)

        assertEquals(DEFAULT_SESSION_COUNT, subject.sessionCount)

        verify(settings)
            .invocation { getLong(KEY_SESSION_COUNT, DEFAULT_SESSION_COUNT) }
            .wasInvoked()
    }

    @Test
    fun default_precision() {
        given(settings)
            .invocation { getInt(KEY_PRECISION, DEFAULT_PRECISION) }
            .thenReturn(DEFAULT_PRECISION)

        assertEquals(DEFAULT_PRECISION, subject.precision)

        verify(settings)
            .invocation { getInt(KEY_PRECISION, DEFAULT_PRECISION) }
            .wasInvoked()
    }

    // setters
    @Test
    fun set_firstRun() {
        val mockedValue = Random.nextBoolean()
        subject.firstRun = mockedValue

        verify(settings)
            .invocation { putBoolean(KEY_FIRST_RUN, mockedValue) }
            .wasInvoked()
    }

    @Test
    fun set_currentBase() {
        val mockValue = "mock"
        subject.currentBase = mockValue

        verify(settings)
            .invocation { putString(KEY_CURRENT_BASE, mockValue) }
            .wasInvoked()
    }

    @Test
    fun set_appTheme() {
        val mockValue = Random.nextInt()
        subject.appTheme = mockValue

        verify(settings)
            .invocation { putInt(KEY_APP_THEME, mockValue) }
            .wasInvoked()
    }

    @Test
    fun set_adFreeEndDate() {
        val mockValue = Random.nextLong()
        subject.adFreeEndDate = mockValue

        verify(settings)
            .invocation { putLong(KEY_AD_FREE_END_DATE, mockValue) }
            .wasInvoked()
    }

    @Test
    fun set_sessionCount() {
        val mockValue = Random.nextLong()
        subject.sessionCount = mockValue

        verify(settings)
            .invocation { putLong(KEY_SESSION_COUNT, mockValue) }
            .wasInvoked()
    }

    @Test
    fun set_precision() {
        val mockValue = Random.nextInt()
        subject.precision = mockValue

        verify(settings)
            .invocation { putInt(KEY_PRECISION, mockValue) }
            .wasInvoked()
    }
}
