/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ccc.client.viewmodel

import com.github.mustafaozhan.ccc.client.base.BaseUseCaseTest
import com.github.mustafaozhan.ccc.client.model.AppTheme
import com.github.mustafaozhan.ccc.client.ui.settings.BackEffect
import com.github.mustafaozhan.ccc.client.ui.settings.ChangeThemeEffect
import com.github.mustafaozhan.ccc.client.ui.settings.CurrenciesEffect
import com.github.mustafaozhan.ccc.client.ui.settings.FeedBackEffect
import com.github.mustafaozhan.ccc.client.ui.settings.OnGitHubEffect
import com.github.mustafaozhan.ccc.client.ui.settings.RemoveAdsEffect
import com.github.mustafaozhan.ccc.client.ui.settings.SettingsUseCase
import com.github.mustafaozhan.ccc.client.ui.settings.ShareEffect
import com.github.mustafaozhan.ccc.client.ui.settings.SupportUsEffect
import com.github.mustafaozhan.ccc.client.ui.settings.ThemeDialogEffect
import com.github.mustafaozhan.ccc.client.util.DAY
import com.github.mustafaozhan.ccc.client.util.formatToString
import com.github.mustafaozhan.ccc.common.di.getDependency
import com.github.mustafaozhan.ccc.common.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

@Suppress("TooManyFunctions")
class SettingsUseCaseTest : BaseUseCaseTest<SettingsUseCase>() {

    override val useCase: SettingsUseCase by lazy {
        koin.getDependency(SettingsUseCase::class)
    }

    @Test
    fun updateTheme() = runTest {
        it.launch {
            val appTheme = AppTheme.DARK
            useCase.updateTheme(appTheme)
            assertEquals(appTheme, useCase.state.appThemeType.value)

            useCase.getEvent().onCurrenciesClick()
            assertEquals(ChangeThemeEffect(appTheme.themeValue), useCase.effect.single())
        }.cancel()
    }

    @Test
    fun updateAddFreeDate() = with(useCase) {
        updateAddFreeDate()
        assertEquals(
            state.addFreeDate.value,
            Instant.fromEpochMilliseconds(
                Clock.System.now().toEpochMilliseconds() + DAY
            ).formatToString()
        )
    }

    // Event
    @Test
    fun onBackClick() = runTest {
        it.launch {
            useCase.getEvent().onBackClick()
            assertEquals(BackEffect, useCase.effect.single())
        }.cancel()
    }

    @Test
    fun onCurrenciesClick() = runTest {
        it.launch {
            useCase.getEvent().onCurrenciesClick()
            assertEquals(CurrenciesEffect, useCase.effect.single())
        }.cancel()
    }

    @Test
    fun onFeedBackClick() = runTest {
        it.launch {
            useCase.getEvent().onFeedBackClick()
            assertEquals(FeedBackEffect, useCase.effect.single())
        }.cancel()
    }

    @Test
    fun onShareClick() = runTest {
        it.launch {
            useCase.getEvent().onShareClick()
            assertEquals(ShareEffect, useCase.effect.single())
        }.cancel()
    }

    @Test
    fun onSupportUsClick() = runTest {
        it.launch {
            useCase.getEvent().onSupportUsClick()
            assertEquals(SupportUsEffect, useCase.effect.single())
        }.cancel()
    }

    @Test
    fun onOnGitHubClick() = runTest {
        it.launch {
            useCase.getEvent().onOnGitHubClick()
            assertEquals(OnGitHubEffect, useCase.effect.single())
        }.cancel()
    }

    @Test
    fun onRemoveAdsClick() = runTest {
        it.launch {
            useCase.getEvent().onRemoveAdsClick()
            assertEquals(RemoveAdsEffect, useCase.effect.single())
        }.cancel()
    }

    @Test
    fun onThemeClick() = runTest {
        it.launch {
            useCase.getEvent().onThemeClick()
            assertEquals(ThemeDialogEffect, useCase.effect.single())
        }.cancel()
    }
}
