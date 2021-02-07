/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ccc.client.viewmodel

import com.github.mustafaozhan.ccc.client.base.BaseViewModelTest
import com.github.mustafaozhan.ccc.client.util.AD_EXPIRATION
import com.github.mustafaozhan.ccc.client.util.formatToString
import com.github.mustafaozhan.ccc.common.di.getDependency
import com.github.mustafaozhan.ccc.common.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

@Suppress("TooManyFunctions")
class SettingsViewModelTest : BaseViewModelTest<SettingsViewModel>() {

    override val viewModel: SettingsViewModel by lazy {
        koin.getDependency(SettingsViewModel::class)
    }

//    @Test
//    fun updateTheme() = runTest {
//        val appTheme = AppTheme.DARK
//        viewModel.updateTheme(appTheme)
//        assertEquals(appTheme, viewModel.state.value.appThemeType)
//
//        viewModel.event.onCurrenciesClick()
//        assertEquals(SettingsEffect.ChangeTheme(appTheme.themeValue), viewModel.effect.single())
//    }

    // todo
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
        assertEquals(SettingsEffect.RemoveAds, viewModel.effect.first())
    }

    @Test
    fun onThemeClick() = runTest {
        viewModel.event.onThemeClick()
        assertEquals(SettingsEffect.ThemeDialog, viewModel.effect.first())
    }
}
