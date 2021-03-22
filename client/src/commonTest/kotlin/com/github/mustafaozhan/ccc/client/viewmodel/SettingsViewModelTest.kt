/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ccc.client.viewmodel

import com.github.mustafaozhan.ccc.client.base.BaseViewModelTest
import com.github.mustafaozhan.ccc.client.model.AppTheme
import com.github.mustafaozhan.ccc.client.viewmodel.settings.SettingsEffect
import com.github.mustafaozhan.ccc.client.viewmodel.settings.SettingsViewModel
import com.github.mustafaozhan.ccc.common.di.getDependency
import com.github.mustafaozhan.ccc.common.runTest
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@Suppress("TooManyFunctions")
class SettingsViewModelTest : BaseViewModelTest<SettingsViewModel>() {

    override val viewModel: SettingsViewModel by lazy {
        koin.getDependency(SettingsViewModel::class)
    }

    @Test
    fun updateTheme() = runTest {
        val appTheme = AppTheme.DARK
        viewModel.updateTheme(appTheme)
        delay(250)
        assertEquals(appTheme, viewModel.state.first().appThemeType)
        assertEquals(SettingsEffect.ChangeTheme(appTheme.themeValue), viewModel.effect.first())
    }

    // Event
    @Test
    fun onBackClick() = runTest {
        viewModel.event.onBackClick()
        assertEquals(SettingsEffect.Back, viewModel.effect.first())
    }

    @Test
    fun onCurrenciesClick() = runTest {
        viewModel.event.onCurrenciesClick()
        assertEquals(SettingsEffect.OpenCurrencies, viewModel.effect.first())
    }

    @Test
    fun onFeedBackClick() = runTest {
        viewModel.event.onFeedBackClick()
        assertEquals(SettingsEffect.FeedBack, viewModel.effect.first())
    }

    @Test
    fun onShareClick() = runTest {
        viewModel.event.onShareClick()
        assertEquals(SettingsEffect.Share, viewModel.effect.first())
    }

    @Test
    fun onSupportUsClick() = runTest {
        viewModel.event.onSupportUsClick()
        assertEquals(SettingsEffect.SupportUs, viewModel.effect.first())
    }

    @Test
    fun onOnGitHubClick() = runTest {
        viewModel.event.onOnGitHubClick()
        assertEquals(SettingsEffect.OnGitHub, viewModel.effect.first())
    }

    @Test
    fun onRemoveAdsClick() = runTest {
        viewModel.event.onRemoveAdsClick()
        assertEquals(
            if (viewModel.isRewardExpired()) SettingsEffect.RemoveAds else SettingsEffect.AlreadyAdFree,
            viewModel.effect.first()
        )
    }

    @Test
    fun onThemeClick() = runTest {
        viewModel.event.onThemeClick()
        assertEquals(SettingsEffect.ThemeDialog, viewModel.effect.first())
    }

    @Test
    fun onSyncClick() = runTest {
        viewModel.event.onSyncClick()
        delay(200)
        assertEquals(SettingsEffect.Synchronising, viewModel.effect.first())
        assertTrue { viewModel.data.synced }

        viewModel.event.onSyncClick()
        assertEquals(SettingsEffect.OnlyOneTimeSync, viewModel.effect.first())
    }
}
