/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ccc.client.viewmodel

import com.github.mustafaozhan.ccc.client.fake.FakeApiRepository
import com.github.mustafaozhan.ccc.client.fake.FakeCurrencyDao
import com.github.mustafaozhan.ccc.client.fake.FakeOfflineRatesDao
import com.github.mustafaozhan.ccc.client.fake.FakeSettingsRepository
import com.github.mustafaozhan.ccc.client.model.AppTheme
import com.github.mustafaozhan.ccc.client.ui.settings.BackEffect
import com.github.mustafaozhan.ccc.client.ui.settings.ChangeThemeEffect
import com.github.mustafaozhan.ccc.client.ui.settings.CurrenciesEffect
import com.github.mustafaozhan.ccc.client.ui.settings.FeedBackEffect
import com.github.mustafaozhan.ccc.client.ui.settings.OnGitHubEffect
import com.github.mustafaozhan.ccc.client.ui.settings.RemoveAdsEffect
import com.github.mustafaozhan.ccc.client.ui.settings.SettingsViewModel
import com.github.mustafaozhan.ccc.client.ui.settings.ShareEffect
import com.github.mustafaozhan.ccc.client.ui.settings.SupportUsEffect
import com.github.mustafaozhan.ccc.client.ui.settings.ThemeDialogEffect
import com.github.mustafaozhan.ccc.client.util.DAY
import com.github.mustafaozhan.ccc.client.util.formatToString
import com.github.mustafaozhan.ccc.common.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

@Suppress("TooManyFunctions")
class SettingsViewModelTest {

    private lateinit var viewModel: SettingsViewModel

    @BeforeTest
    fun setup() {
        viewModel = SettingsViewModel(
            FakeSettingsRepository.getSettingsRepository(),
            FakeApiRepository.getApiRepository(),
            FakeCurrencyDao.getCurrencyDao(),
            FakeOfflineRatesDao.getOfflineRatesDao()
        )
    }

    @Test
    fun updateTheme() = runTest {
        it.launch {
            val appTheme = AppTheme.DARK
            viewModel.updateTheme(appTheme)
            assertEquals(appTheme, viewModel.state.appThemeType.value)

            viewModel.getEvent().onCurrenciesClick()
            assertEquals(ChangeThemeEffect(appTheme.themeValue), viewModel.effect.single())
        }.cancel()
    }

    @Test
    fun updateAddFreeDate() = with(viewModel) {
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
