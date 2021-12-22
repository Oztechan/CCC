/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ccc.common.repo

import com.github.mustafaozhan.ccc.common.settings.SettingsRepository
import com.github.mustafaozhan.ccc.common.settings.SettingsRepositoryImp
import com.github.mustafaozhan.ccc.common.settings.SettingsRepositoryImp.Companion.DEFAULT_AD_FREE_END_DATE
import com.github.mustafaozhan.ccc.common.settings.SettingsRepositoryImp.Companion.DEFAULT_APP_THEME
import com.github.mustafaozhan.ccc.common.settings.SettingsRepositoryImp.Companion.DEFAULT_CURRENT_BASE
import com.github.mustafaozhan.ccc.common.settings.SettingsRepositoryImp.Companion.DEFAULT_FIRST_RUN
import com.github.mustafaozhan.ccc.common.settings.SettingsRepositoryImp.Companion.DEFAULT_LAST_REVIEW_REQUEST
import com.github.mustafaozhan.ccc.common.settings.SettingsRepositoryImp.Companion.KEY_AD_FREE_END_DATE
import com.github.mustafaozhan.ccc.common.settings.SettingsRepositoryImp.Companion.KEY_APP_THEME
import com.github.mustafaozhan.ccc.common.settings.SettingsRepositoryImp.Companion.KEY_CURRENT_BASE
import com.github.mustafaozhan.ccc.common.settings.SettingsRepositoryImp.Companion.KEY_FIRST_RUN
import com.github.mustafaozhan.ccc.common.settings.SettingsRepositoryImp.Companion.KEY_LAST_REVIEW_REQUEST
import com.russhwolf.settings.Settings
import io.mockative.Mock
import io.mockative.any
import io.mockative.classOf
import io.mockative.given
import io.mockative.matching
import io.mockative.mock
import io.mockative.verify
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
            .function(settings::getBoolean)
            .whenInvokedWith(matching { it == KEY_FIRST_RUN }, any())
            .thenReturn(DEFAULT_FIRST_RUN)

        assertEquals(
            DEFAULT_FIRST_RUN,
            repository.firstRun
        )
    }

    @Test
    fun default_currentBase() {
        given(settings)
            .function(settings::getString)
            .whenInvokedWith(matching { it == KEY_CURRENT_BASE }, any())
            .thenReturn(DEFAULT_CURRENT_BASE)

        assertEquals(DEFAULT_CURRENT_BASE, repository.currentBase)
    }

    @Test
    fun default_appTheme() {
        given(settings)
            .function(settings::getInt)
            .whenInvokedWith(matching { it == KEY_APP_THEME }, any())
            .thenReturn(DEFAULT_APP_THEME)

        assertEquals(DEFAULT_APP_THEME, repository.appTheme)
    }

    @Test
    fun default_adFreeEndDate() {
        given(settings)
            .function(settings::getLong)
            .whenInvokedWith(matching { it == KEY_AD_FREE_END_DATE }, any())
            .thenReturn(DEFAULT_AD_FREE_END_DATE)

        assertEquals(DEFAULT_AD_FREE_END_DATE, repository.adFreeEndDate)
    }

    @Test
    fun default_lastReviewRequest() {
        given(settings)
            .function(settings::getLong)
            .whenInvokedWith(matching { it == KEY_LAST_REVIEW_REQUEST }, any())
            .thenReturn(DEFAULT_LAST_REVIEW_REQUEST)

        assertEquals(DEFAULT_LAST_REVIEW_REQUEST, repository.lastReviewRequest)
    }

    // setters
    @Test
    fun set_firstRun() {
        given(settings)
            .function(settings::putBoolean)
            .whenInvokedWith(matching { it == KEY_FIRST_RUN }, any())
            .thenReturn(Unit)

        repository.firstRun = true

        verify(settings)
            .function(settings::putBoolean)
            .with(matching { it == KEY_FIRST_RUN }, any())
            .wasInvoked()
    }

    @Test
    fun set_currentBase() {
        given(settings)
            .function(settings::putString)
            .whenInvokedWith(matching { it == KEY_CURRENT_BASE }, any())
            .thenReturn(Unit)

        repository.currentBase = "mock"

        verify(settings)
            .function(settings::putString)
            .with(matching { it == KEY_CURRENT_BASE }, any())
            .wasInvoked()
    }

    @Test
    fun set_appTheme() {
        given(settings)
            .function(settings::putInt)
            .whenInvokedWith(matching { it == KEY_APP_THEME }, any())
            .thenReturn(Unit)

        repository.appTheme = 3

        verify(settings)
            .function(settings::putInt)
            .with(matching { it == KEY_APP_THEME }, any())
            .wasInvoked()
    }

    @Test
    fun set_adFreeEndDate() {
        given(settings)
            .function(settings::putLong)
            .whenInvokedWith(matching { it == KEY_AD_FREE_END_DATE }, any())
            .thenReturn(Unit)

        repository.adFreeEndDate = 1L

        verify(settings)
            .function(settings::putLong)
            .with(matching { it == KEY_AD_FREE_END_DATE }, any())
            .wasInvoked()
    }

    @Test
    fun set_lastReviewRequest() {
        given(settings)
            .function(settings::putLong)
            .whenInvokedWith(matching { it == KEY_LAST_REVIEW_REQUEST }, any())
            .thenReturn(Unit)

        repository.lastReviewRequest = 1L

        verify(settings)
            .function(settings::putLong)
            .with(matching { it == KEY_LAST_REVIEW_REQUEST }, any())
            .wasInvoked()
    }
}
