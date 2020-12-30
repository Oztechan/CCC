/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ccc.client.viewmodel

import com.github.mustafaozhan.ccc.client.base.BaseViewModelTest
import com.github.mustafaozhan.ccc.client.model.AppTheme
import com.github.mustafaozhan.ccc.client.util.DAY
import com.github.mustafaozhan.ccc.client.util.formatToString
import com.github.mustafaozhan.ccc.client.viewmodel.settings.BackEffect
import com.github.mustafaozhan.ccc.client.viewmodel.settings.ChangeThemeEffect
import com.github.mustafaozhan.ccc.client.viewmodel.settings.CurrenciesEffect
import com.github.mustafaozhan.ccc.client.viewmodel.settings.FeedBackEffect
import com.github.mustafaozhan.ccc.client.viewmodel.settings.OnGitHubEffect
import com.github.mustafaozhan.ccc.client.viewmodel.settings.RemoveAdsEffect
import com.github.mustafaozhan.ccc.client.viewmodel.settings.SettingsViewModel
import com.github.mustafaozhan.ccc.client.viewmodel.settings.ShareEffect
import com.github.mustafaozhan.ccc.client.viewmodel.settings.SupportUsEffect
import com.github.mustafaozhan.ccc.client.viewmodel.settings.ThemeDialogEffect
import com.github.mustafaozhan.ccc.common.di.getDependency
import com.github.mustafaozhan.ccc.common.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

@Suppress("TooManyFunctions")
class SettingsViewModelTest : BaseViewModelTest<SettingsViewModel>() {

    override val viewModel: SettingsViewModel by lazy {
        koin.getDependency(SettingsViewModel::class)
    }

    @Test
    fun updateTheme() = runTest {
        it.launch {
            val appTheme = AppTheme.DARK
            viewModel.updateTheme(appTheme)
            assertEquals(appTheme, viewModel.state.value.appThemeType)

            viewModel.getEvent().onCurrenciesClick()
            assertEquals(ChangeThemeEffect(appTheme.themeValue), viewModel.effect.single())
        }.cancel()
    }

    @Test
    fun updateAddFreeDate() = with(viewModel) {
        updateAddFreeDate()
        assertEquals(
            state.value.addFreeDate,
            Instant.fromEpochMilliseconds(
                Clock.System.now().toEpochMilliseconds() + DAY
            ).formatToString()
        )
    }

    // Event
    @Test
    fun onBackClick() = runTest {
        it.launch {
            viewModel.getEvent().onBackClick()
            assertEquals(BackEffect, viewModel.effect.single())
        }.cancel()
    }

    @Test
    fun onCurrenciesClick() = runTest {
        it.launch {
            viewModel.getEvent().onCurrenciesClick()
            assertEquals(CurrenciesEffect, viewModel.effect.single())
        }.cancel()
    }

    @Test
    fun onFeedBackClick() = runTest {
        it.launch {
            viewModel.getEvent().onFeedBackClick()
            assertEquals(FeedBackEffect, viewModel.effect.single())
        }.cancel()
    }

    @Test
    fun onShareClick() = runTest {
        it.launch {
            viewModel.getEvent().onShareClick()
            assertEquals(ShareEffect, viewModel.effect.single())
        }.cancel()
    }

    @Test
    fun onSupportUsClick() = runTest {
        it.launch {
            viewModel.getEvent().onSupportUsClick()
            assertEquals(SupportUsEffect, viewModel.effect.single())
        }.cancel()
    }

    @Test
    fun onOnGitHubClick() = runTest {
        it.launch {
            viewModel.getEvent().onOnGitHubClick()
            assertEquals(OnGitHubEffect, viewModel.effect.single())
        }.cancel()
    }

    @Test
    fun onRemoveAdsClick() = runTest {
        it.launch {
            viewModel.getEvent().onRemoveAdsClick()
            assertEquals(RemoveAdsEffect, viewModel.effect.single())
        }.cancel()
    }

    @Test
    fun onThemeClick() = runTest {
        it.launch {
            viewModel.getEvent().onThemeClick()
            assertEquals(ThemeDialogEffect, viewModel.effect.single())
        }.cancel()
    }
}
