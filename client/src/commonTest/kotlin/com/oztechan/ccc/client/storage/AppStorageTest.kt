/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package com.oztechan.ccc.client.storage

import com.oztechan.ccc.client.storage.app.AppStorage
import com.oztechan.ccc.client.storage.app.AppStorageImpl
import com.oztechan.ccc.client.storage.app.AppStorageImpl.Companion.DEFAULT_APP_THEME
import com.oztechan.ccc.client.storage.app.AppStorageImpl.Companion.DEFAULT_FIRST_RUN
import com.oztechan.ccc.client.storage.app.AppStorageImpl.Companion.DEFAULT_PREMIUM_END_DATE
import com.oztechan.ccc.client.storage.app.AppStorageImpl.Companion.DEFAULT_SESSION_COUNT
import com.oztechan.ccc.client.storage.app.AppStorageImpl.Companion.KEY_APP_THEME
import com.oztechan.ccc.client.storage.app.AppStorageImpl.Companion.KEY_FIRST_RUN
import com.oztechan.ccc.client.storage.app.AppStorageImpl.Companion.KEY_PREMIUM_END_DATE
import com.oztechan.ccc.client.storage.app.AppStorageImpl.Companion.KEY_SESSION_COUNT
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
    fun `default firstRun`() {
        given(settings)
            .invocation { getBoolean(KEY_FIRST_RUN, DEFAULT_FIRST_RUN) }
            .thenReturn(DEFAULT_FIRST_RUN)

        assertEquals(DEFAULT_FIRST_RUN, subject.firstRun)

        verify(settings)
            .invocation { getBoolean(KEY_FIRST_RUN, DEFAULT_FIRST_RUN) }
            .wasInvoked()
    }

    @Test
    fun `default appTheme`() {
        given(settings)
            .invocation { getInt(KEY_APP_THEME, DEFAULT_APP_THEME) }
            .thenReturn(DEFAULT_APP_THEME)

        assertEquals(DEFAULT_APP_THEME, subject.appTheme)

        verify(settings)
            .invocation { getInt(KEY_APP_THEME, DEFAULT_APP_THEME) }
            .wasInvoked()
    }

    @Test
    fun `default premiumEndDate`() {
        given(settings)
            .invocation { getLong(KEY_PREMIUM_END_DATE, DEFAULT_PREMIUM_END_DATE) }
            .thenReturn(DEFAULT_PREMIUM_END_DATE)

        assertEquals(DEFAULT_PREMIUM_END_DATE, subject.premiumEndDate)

        verify(settings)
            .invocation { getLong(KEY_PREMIUM_END_DATE, DEFAULT_PREMIUM_END_DATE) }
            .wasInvoked()
    }

    @Test
    fun `default sessionCount`() {
        given(settings)
            .invocation { getLong(KEY_SESSION_COUNT, DEFAULT_SESSION_COUNT) }
            .thenReturn(DEFAULT_SESSION_COUNT)

        assertEquals(DEFAULT_SESSION_COUNT, subject.sessionCount)

        verify(settings)
            .invocation { getLong(KEY_SESSION_COUNT, DEFAULT_SESSION_COUNT) }
            .wasInvoked()
    }

    // setters
    @Test
    fun `set firstRun`() {
        val mockedValue = Random.nextBoolean()
        subject.firstRun = mockedValue

        verify(settings)
            .invocation { putBoolean(KEY_FIRST_RUN, mockedValue) }
            .wasInvoked()
    }

    @Test
    fun `set appTheme`() {
        val mockValue = Random.nextInt()
        subject.appTheme = mockValue

        verify(settings)
            .invocation { putInt(KEY_APP_THEME, mockValue) }
            .wasInvoked()
    }

    @Test
    fun `set premiumEndDate`() {
        val mockValue = Random.nextLong()
        subject.premiumEndDate = mockValue

        verify(settings)
            .invocation { putLong(KEY_PREMIUM_END_DATE, mockValue) }
            .wasInvoked()
    }

    @Test
    fun `set sessionCount`() {
        val mockValue = Random.nextLong()
        subject.sessionCount = mockValue

        verify(settings)
            .invocation { putLong(KEY_SESSION_COUNT, mockValue) }
            .wasInvoked()
    }
}
