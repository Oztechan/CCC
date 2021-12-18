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
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class SettingsRepositoryTest {

    @Mock
    private val settings = mock(classOf<Settings>())

    private lateinit var repository: SettingsRepository

    @BeforeTest
    fun setup() {
        repository = SettingsRepositoryImp(settings)
    }

    // defaults
    @Test
    fun firstRun() {
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
    fun currentBase() {
        given(settings)
            .function(settings::getString)
            .whenInvokedWith(matching { it == KEY_CURRENT_BASE }, any())
            .thenReturn(DEFAULT_CURRENT_BASE)

        assertEquals(DEFAULT_CURRENT_BASE, repository.currentBase)
    }

    @Test
    fun appTheme() {
        given(settings)
            .function(settings::getInt)
            .whenInvokedWith(matching { it == KEY_APP_THEME }, any())
            .thenReturn(DEFAULT_APP_THEME)

        assertEquals(DEFAULT_APP_THEME, repository.appTheme)
    }

    @Test
    fun adFreeEndDate() {
        given(settings)
            .function(settings::getLong)
            .whenInvokedWith(matching { it == KEY_AD_FREE_END_DATE }, any())
            .thenReturn(DEFAULT_AD_FREE_END_DATE)

        assertEquals(DEFAULT_AD_FREE_END_DATE, repository.adFreeEndDate)
    }

    @Test
    fun lastReviewRequest() {
        given(settings)
            .function(settings::getLong)
            .whenInvokedWith(matching { it == KEY_LAST_REVIEW_REQUEST }, any())
            .thenReturn(DEFAULT_LAST_REVIEW_REQUEST)

        assertEquals(DEFAULT_LAST_REVIEW_REQUEST, repository.lastReviewRequest)
    }
}
