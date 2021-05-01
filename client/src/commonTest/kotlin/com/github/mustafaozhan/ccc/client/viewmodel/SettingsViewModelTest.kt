/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ccc.client.viewmodel

import com.github.mustafaozhan.ccc.client.base.BaseViewModelTest
import com.github.mustafaozhan.ccc.client.model.AppTheme
import com.github.mustafaozhan.ccc.client.util.test
import com.github.mustafaozhan.ccc.client.viewmodel.settings.SettingsEffect
import com.github.mustafaozhan.ccc.client.viewmodel.settings.SettingsViewModel
import com.github.mustafaozhan.ccc.common.di.getDependency
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@Suppress("TooManyFunctions")
class SettingsViewModelTest : BaseViewModelTest<SettingsViewModel>() {

    override val viewModel: SettingsViewModel by lazy {
        koin.getDependency(SettingsViewModel::class)
    }

    @Test
    fun updateTheme() = with(viewModel) {
        val appTheme = AppTheme.DARK
        effect.test({
            viewModel.updateTheme(appTheme)
        }, {
            assertEquals(appTheme, viewModel.state.value.appThemeType)
            assertEquals(SettingsEffect.ChangeTheme(appTheme.themeValue), it)
        })
    }

    // Event
    @Test
    fun onBackClick() = viewModel.effect.test({
        viewModel.event.onBackClick()
    }, {
        assertTrue { it is SettingsEffect.Back }
    })

    @Test
    fun onCurrenciesClick() = viewModel.effect.test({
        viewModel.event.onCurrenciesClick()
    }, {
        assertTrue { it is SettingsEffect.OpenCurrencies }
    })

    @Test
    fun onFeedBackClick() = viewModel.effect.test({
        viewModel.event.onFeedBackClick()
    }, {
        assertTrue { it is SettingsEffect.FeedBack }
    })

    @Test
    fun onShareClick() = viewModel.effect.test({
        viewModel.event.onShareClick()
    }, {
        assertTrue { it is SettingsEffect.Share }
    })

    @Test
    fun onSupportUsClick() = viewModel.effect.test({
        viewModel.event.onSupportUsClick()
    }, {
        assertTrue { it is SettingsEffect.SupportUs }
    })

    @Test
    fun onOnGitHubClick() = viewModel.effect.test({
        viewModel.event.onOnGitHubClick()
    }, {
        assertTrue { it is SettingsEffect.OnGitHub }
    })

    @Test
    fun onRemoveAdsClick() = viewModel.effect.test({
        viewModel.event.onRemoveAdsClick()
    }, {
        assertTrue {
            if (viewModel.isRewardExpired()) {
                it is SettingsEffect.RemoveAds
            } else {
                it is SettingsEffect.AlreadyAdFree
            }
        }
    })

    @Test
    fun onThemeClick() = viewModel.effect.test({
        viewModel.event.onThemeClick()
    }, {
        assertTrue { it is SettingsEffect.ThemeDialog }
    })

    @Test
    fun onSyncClick() = viewModel.effect.test({
        viewModel.event.onSyncClick()
    }, {
        assertTrue { it is SettingsEffect.Synchronising }
    })
}
