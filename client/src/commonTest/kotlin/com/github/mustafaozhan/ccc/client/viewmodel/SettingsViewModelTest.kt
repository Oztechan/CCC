/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ccc.client.viewmodel

import com.github.mustafaozhan.ccc.client.base.BaseViewModelTest
import com.github.mustafaozhan.ccc.common.di.getDependency
import com.github.mustafaozhan.ccc.common.runTest
import kotlinx.coroutines.flow.first
import kotlin.test.Test
import kotlin.test.assertEquals

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

    // Event
    @Test
    fun onBackClick() = runTest {
        viewModel.event.onBackClick().run {
            assertEquals(SettingsEffect.Back, viewModel.effect.first())
        }
    }

    @Test
    fun onCurrenciesClick() = runTest {
        viewModel.event.onCurrenciesClick().run {
            assertEquals(SettingsEffect.OpenCurrencies, viewModel.effect.first())
        }
    }

    @Test
    fun onFeedBackClick() = runTest {
        viewModel.event.onFeedBackClick().run {
            assertEquals(SettingsEffect.FeedBack, viewModel.effect.first())
        }
    }

    @Test
    fun onShareClick() = runTest {
        viewModel.event.onShareClick().run {
            assertEquals(SettingsEffect.Share, viewModel.effect.first())
        }
    }

    @Test
    fun onSupportUsClick() = runTest {
        viewModel.event.onSupportUsClick().run {
            assertEquals(SettingsEffect.SupportUs, viewModel.effect.first())
        }
    }

    @Test
    fun onOnGitHubClick() = runTest {
        viewModel.event.onOnGitHubClick().run {
            assertEquals(SettingsEffect.OnGitHub, viewModel.effect.first())
        }
    }

    @Test
    fun onRemoveAdsClick() = runTest {
        viewModel.event.onRemoveAdsClick().run {
            assertEquals(
                if (viewModel.isRewardExpired()) SettingsEffect.RemoveAds else SettingsEffect.AlreadyAdFree,
                viewModel.effect.first()
            )
        }
    }

    @Test
    fun onThemeClick() = runTest {
        viewModel.event.onThemeClick().run {
            assertEquals(SettingsEffect.ThemeDialog, viewModel.effect.first())
        }
    }
}
