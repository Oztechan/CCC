/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package com.oztechan.ccc.common.repo

import com.oztechan.ccc.common.settings.SettingsRepository
import com.oztechan.ccc.common.settings.SettingsRepositoryImp
import com.oztechan.ccc.common.settings.SettingsRepositoryImp.Companion.DEFAULT_AD_FREE_END_DATE
import com.oztechan.ccc.common.settings.SettingsRepositoryImp.Companion.DEFAULT_APP_THEME
import com.oztechan.ccc.common.settings.SettingsRepositoryImp.Companion.DEFAULT_CURRENT_BASE
import com.oztechan.ccc.common.settings.SettingsRepositoryImp.Companion.DEFAULT_FIRST_RUN
import com.oztechan.ccc.common.settings.SettingsRepositoryImp.Companion.DEFAULT_SESSION_COUNT
import com.oztechan.ccc.common.settings.SettingsRepositoryImp.Companion.KEY_AD_FREE_END_DATE
import com.oztechan.ccc.common.settings.SettingsRepositoryImp.Companion.KEY_APP_THEME
import com.oztechan.ccc.common.settings.SettingsRepositoryImp.Companion.KEY_CURRENT_BASE
import com.oztechan.ccc.common.settings.SettingsRepositoryImp.Companion.KEY_FIRST_RUN
import com.oztechan.ccc.common.settings.SettingsRepositoryImp.Companion.KEY_SESSION_COUNT
import com.russhwolf.settings.Settings
import io.mockative.Mock
import io.mockative.classOf
import io.mockative.given
import io.mockative.mock
import io.mockative.verify
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals

class SettingsRepositoryTest {

    @Mock
    private val settings = mock(classOf<Settings>())

    private val repository: SettingsRepository by lazy {
        SettingsRepositoryImp(settings)
    }

    // defaults
    @Test
    fun default_firstRun() {
        given(settings)
            .invocation { getBoolean(KEY_FIRST_RUN, DEFAULT_FIRST_RUN) }
            .thenReturn(DEFAULT_FIRST_RUN)

        assertEquals(DEFAULT_FIRST_RUN, repository.firstRun)

        verify(settings)
            .invocation { getBoolean(KEY_FIRST_RUN, DEFAULT_FIRST_RUN) }
            .wasInvoked()
    }

    @Test
    fun default_currentBase() {
        given(settings)
            .invocation { getString(KEY_CURRENT_BASE, DEFAULT_CURRENT_BASE) }
            .thenReturn(DEFAULT_CURRENT_BASE)

        assertEquals(DEFAULT_CURRENT_BASE, repository.currentBase)

        verify(settings)
            .invocation { getString(KEY_CURRENT_BASE, DEFAULT_CURRENT_BASE) }
            .wasInvoked()
    }

    @Test
    fun default_appTheme() {
        given(settings)
            .invocation { getInt(KEY_APP_THEME, DEFAULT_APP_THEME) }
            .thenReturn(DEFAULT_APP_THEME)

        assertEquals(DEFAULT_APP_THEME, repository.appTheme)

        verify(settings)
            .invocation { getInt(KEY_APP_THEME, DEFAULT_APP_THEME) }
            .wasInvoked()
    }

    @Test
    fun default_adFreeEndDate() {
        given(settings)
            .invocation { getLong(KEY_AD_FREE_END_DATE, DEFAULT_AD_FREE_END_DATE) }
            .thenReturn(DEFAULT_AD_FREE_END_DATE)

        assertEquals(DEFAULT_AD_FREE_END_DATE, repository.adFreeEndDate)

        verify(settings)
            .invocation { getLong(KEY_AD_FREE_END_DATE, DEFAULT_AD_FREE_END_DATE) }
            .wasInvoked()
    }

    @Test
    fun default_sessionCount() {
        given(settings)
            .invocation { getLong(KEY_SESSION_COUNT, DEFAULT_SESSION_COUNT) }
            .thenReturn(DEFAULT_SESSION_COUNT)

        assertEquals(DEFAULT_SESSION_COUNT, repository.sessionCount)

        verify(settings)
            .invocation { getLong(KEY_SESSION_COUNT, DEFAULT_SESSION_COUNT) }
            .wasInvoked()
    }

    // setters
    @Test
    fun set_firstRun() {
        val mockedValue = Random.nextBoolean()
        repository.firstRun = mockedValue

        verify(settings)
            .invocation { putBoolean(KEY_FIRST_RUN, mockedValue) }
            .wasInvoked()
    }

    @Test
    fun set_currentBase() {
        val mockValue = "mock"
        repository.currentBase = mockValue

        verify(settings)
            .invocation { putString(KEY_CURRENT_BASE, mockValue) }
            .wasInvoked()
    }

    @Test
    fun set_appTheme() {
        val mockValue = Random.nextInt()
        repository.appTheme = mockValue

        verify(settings)
            .invocation { putInt(KEY_APP_THEME, mockValue) }
            .wasInvoked()
    }

    @Test
    fun set_adFreeEndDate() {
        val mockValue = Random.nextLong()
        repository.adFreeEndDate = mockValue

        verify(settings)
            .invocation { putLong(KEY_AD_FREE_END_DATE, mockValue) }
            .wasInvoked()
    }

    @Test
    fun set_sessionCount() {
        val mockValue = Random.nextLong()
        repository.sessionCount = mockValue

        verify(settings)
            .invocation { putLong(KEY_SESSION_COUNT, mockValue) }
            .wasInvoked()
    }
}
