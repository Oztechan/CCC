/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ccc.client.viewmodel

import com.github.mustafaozhan.ccc.client.base.BaseViewModelTest
import com.github.mustafaozhan.ccc.client.model.AppTheme
import com.github.mustafaozhan.ccc.client.util.after
import com.github.mustafaozhan.ccc.client.util.before
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
        effect.before {
            viewModel.updateTheme(appTheme)
        }.after {
            assertEquals(appTheme, viewModel.state.value.appThemeType)
            assertEquals(SettingsEffect.ChangeTheme(appTheme.themeValue), it)
        }
    }

    @Test
    fun updateAddFreeDate() = viewModel.state.before {
        viewModel.updateAddFreeDate()
    }.after {
        assertTrue { viewModel.state.value.addFreeEndDate.isNotEmpty() }
    }

    // Event
    @Test
    fun onBackClick() = viewModel.effect.before {
        viewModel.event.onBackClick()
    }.after {
        assertTrue { it is SettingsEffect.Back }
    }

    @Test
    fun onCurrenciesClick() = viewModel.effect.before {
        viewModel.event.onCurrenciesClick()
    }.after {
        assertTrue { it is SettingsEffect.OpenCurrencies }
    }

    @Test
    fun onFeedBackClick() = viewModel.effect.before {
        viewModel.event.onFeedBackClick()
    }.after {
        assertTrue { it is SettingsEffect.FeedBack }
    }

    @Test
    fun onShareClick() = viewModel.effect.before {
        viewModel.event.onShareClick()
    }.after {
        assertTrue { it is SettingsEffect.Share }
    }

    @Test
    fun onSupportUsClick() = viewModel.effect.before {
        viewModel.event.onSupportUsClick()
    }.after {
        assertTrue { it is SettingsEffect.SupportUs }
    }

    @Test
    fun onOnGitHubClick() = viewModel.effect.before {
        viewModel.event.onOnGitHubClick()
    }.after {
        assertTrue { it is SettingsEffect.OnGitHub }
    }

    @Test
    fun onRemoveAdsClick() = viewModel.effect.before {
        viewModel.event.onRemoveAdsClick()
    }.after {
        assertTrue {
            if (viewModel.isRewardExpired()) {
                it is SettingsEffect.RemoveAds
            } else {
                it is SettingsEffect.AlreadyAdFree
            }
        }
    }

    @Test
    fun onThemeClick() = viewModel.effect.before {
        viewModel.event.onThemeClick()
    }.after {
        assertTrue { it is SettingsEffect.ThemeDialog }
    }

    @Test
    fun onSyncClick() {
        viewModel.effect.before {
            viewModel.event.onSyncClick()
        }.after {
            assertTrue { viewModel.state.value.loading }
            assertTrue { it is SettingsEffect.Synchronising }
        }

        viewModel.effect.before {
            viewModel.event.onSyncClick()
        }.after {
            assertTrue { viewModel.data.synced }
            assertTrue { it is SettingsEffect.OnlyOneTimeSync }
        }
    }
}
