/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ccc.android.viewmodel

import com.github.mustafaozhan.ccc.android.model.AppTheme
import com.github.mustafaozhan.ccc.android.ui.settings.BackEffect
import com.github.mustafaozhan.ccc.android.ui.settings.ChangeThemeEffect
import com.github.mustafaozhan.ccc.android.ui.settings.CurrenciesEffect
import com.github.mustafaozhan.ccc.android.ui.settings.FeedBackEffect
import com.github.mustafaozhan.ccc.android.ui.settings.OnGitHubEffect
import com.github.mustafaozhan.ccc.android.ui.settings.RemoveAdsEffect
import com.github.mustafaozhan.ccc.android.ui.settings.SettingsViewModel
import com.github.mustafaozhan.ccc.android.ui.settings.ShareEffect
import com.github.mustafaozhan.ccc.android.ui.settings.SupportUsEffect
import com.github.mustafaozhan.ccc.android.ui.settings.ThemeDialogEffect
import com.github.mustafaozhan.ccc.android.util.DAY
import com.github.mustafaozhan.ccc.android.util.dateStringToFormattedString
import com.github.mustafaozhan.ccc.client.repo.SettingsRepository
import com.github.mustafaozhan.ccc.common.api.ApiRepository
import com.github.mustafaozhan.ccc.common.db.CurrencyDao
import com.github.mustafaozhan.ccc.common.db.OfflineRatesDao
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import java.util.Date
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.datetime.Clock
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@Suppress("TooManyFunctions")
class SettingsViewModelTest : BaseViewModelTest<SettingsViewModel>() {

    override lateinit var viewModel: SettingsViewModel

    @MockK
    lateinit var apiRepository: ApiRepository

    @RelaxedMockK
    lateinit var settingsRepository: SettingsRepository

    @RelaxedMockK
    lateinit var currencyDao: CurrencyDao

    @MockK
    lateinit var offlineRatesDao: OfflineRatesDao

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = SettingsViewModel(
            settingsRepository,
            apiRepository,
            currencyDao,
            offlineRatesDao
        )
    }

    @Test
    fun `update theme`() = runBlockingTest {
        launch {
            val appTheme = AppTheme.DARK
            viewModel.updateTheme(appTheme)
            assertEquals(appTheme, viewModel.state.appThemeType.value)

            viewModel.getEvent().onCurrenciesClick()
            assertEquals(ChangeThemeEffect(appTheme.themeValue), viewModel.effect.single())
        }.cancel()
    }

    @Test
    fun `update ad expiration date`() = with(viewModel) {
        updateAddFreeDate()
        assertEquals(
            state.addFreeDate.value,
            Date(Clock.System.now().toEpochMilliseconds() + DAY).dateStringToFormattedString()
        )
    }

    // Event
    @Test
    fun `back click`() = runBlockingTest {
        launch {
            viewModel.getEvent().onBackClick()
            assertEquals(BackEffect, viewModel.effect.single())
        }.cancel()
    }

    @Test
    fun `currencies click`() = runBlockingTest {
        launch {
            viewModel.getEvent().onCurrenciesClick()
            assertEquals(CurrenciesEffect, viewModel.effect.single())
        }.cancel()
    }

    @Test
    fun `feedback click`() = runBlockingTest {
        launch {
            viewModel.getEvent().onFeedBackClick()
            assertEquals(FeedBackEffect, viewModel.effect.single())
        }.cancel()
    }

    @Test
    fun `share click`() = runBlockingTest {
        launch {
            viewModel.getEvent().onShareClick()
            assertEquals(ShareEffect, viewModel.effect.single())
        }.cancel()
    }

    @Test
    fun `support us click`() = runBlockingTest {
        launch {
            viewModel.getEvent().onSupportUsClick()
            assertEquals(SupportUsEffect, viewModel.effect.single())
        }.cancel()
    }

    @Test
    fun `on github click`() = runBlockingTest {
        launch {
            viewModel.getEvent().onOnGitHubClick()
            assertEquals(OnGitHubEffect, viewModel.effect.single())
        }.cancel()
    }

    @Test
    fun `on remove ad click`() = runBlockingTest {
        launch {
            viewModel.getEvent().onRemoveAdsClick()
            assertEquals(RemoveAdsEffect, viewModel.effect.single())
        }.cancel()
    }

    @Test
    fun `on theme click`() = runBlockingTest {
        launch {
            viewModel.getEvent().onThemeClick()
            assertEquals(ThemeDialogEffect, viewModel.effect.single())
        }.cancel()
    }
}
