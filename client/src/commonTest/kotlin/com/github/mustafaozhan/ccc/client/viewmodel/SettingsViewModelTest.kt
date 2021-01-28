/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ccc.client.viewmodel

import com.github.mustafaozhan.ccc.client.base.BaseViewModelTest
import com.github.mustafaozhan.ccc.client.model.AppTheme
import com.github.mustafaozhan.ccc.client.util.AD_EXPIRATION
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

            viewModel.event.onCurrenciesClick()
            assertEquals(SettingsEffect.ChangeTheme(appTheme.themeValue), viewModel.effect.single())
        }.cancel()
    }

    @Test
    fun updateAddFreeDate() = with(viewModel) {
        updateAddFreeDate()
        assertEquals(
            state.value.addFreeDate,
            Instant.fromEpochMilliseconds(
                Clock.System.now().toEpochMilliseconds() + AD_EXPIRATION
            ).formatToString()
        )
    }

    // Event
    @Test
    fun onBackClick() = runTest {
        it.launch {
            viewModel.event.onBackClick()
            assertEquals(SettingsEffect.Back, viewModel.effect.single())
        }.cancel()
    }

    @Test
    fun onCurrenciesClick() = runTest {
        it.launch {
            viewModel.event.onCurrenciesClick()
            assertEquals(SettingsEffect.OpenCurrencies, viewModel.effect.single())
        }.cancel()
    }

    @Test
    fun onFeedBackClick() = runTest {
        it.launch {
            viewModel.event.onFeedBackClick()
            assertEquals(SettingsEffect.FeedBack, viewModel.effect.single())
        }.cancel()
    }

    @Test
    fun onShareClick() = runTest {
        it.launch {
            viewModel.event.onShareClick()
            assertEquals(SettingsEffect.Share, viewModel.effect.single())
        }.cancel()
    }

    @Test
    fun onSupportUsClick() = runTest {
        it.launch {
            viewModel.event.onSupportUsClick()
            assertEquals(SettingsEffect.SupportUs, viewModel.effect.single())
        }.cancel()
    }

    @Test
    fun onOnGitHubClick() = runTest {
        it.launch {
            viewModel.event.onOnGitHubClick()
            assertEquals(SettingsEffect.OnGitHub, viewModel.effect.single())
        }.cancel()
    }

    @Test
    fun onRemoveAdsClick() = runTest {
        it.launch {
            viewModel.event.onRemoveAdsClick()
            assertEquals(SettingsEffect.RemoveAds, viewModel.effect.single())
        }.cancel()
    }

    @Test
    fun onThemeClick() = runTest {
        it.launch {
            viewModel.event.onThemeClick()
            assertEquals(SettingsEffect.ThemeDialog, viewModel.effect.single())
        }.cancel()
    }
}
