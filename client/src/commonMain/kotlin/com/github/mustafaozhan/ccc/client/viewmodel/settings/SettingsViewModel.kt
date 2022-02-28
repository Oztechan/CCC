/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ccc.client.viewmodel.settings

import co.touchlab.kermit.Logger
import com.github.mustafaozhan.ccc.client.base.BaseSEEDViewModel
import com.github.mustafaozhan.ccc.client.helper.SessionManager
import com.github.mustafaozhan.ccc.client.model.AppTheme
import com.github.mustafaozhan.ccc.client.model.RemoveAdType
import com.github.mustafaozhan.ccc.client.util.calculateAdRewardEnd
import com.github.mustafaozhan.ccc.client.util.isRewardExpired
import com.github.mustafaozhan.ccc.client.util.launchIgnored
import com.github.mustafaozhan.ccc.client.util.toDateString
import com.github.mustafaozhan.ccc.client.viewmodel.settings.SettingsData.Companion.SYNC_DELAY
import com.github.mustafaozhan.ccc.common.api.repo.ApiRepository
import com.github.mustafaozhan.ccc.common.db.currency.CurrencyRepository
import com.github.mustafaozhan.ccc.common.db.offlinerates.OfflineRatesRepository
import com.github.mustafaozhan.ccc.common.settings.SettingsRepository
import com.github.mustafaozhan.ccc.common.util.nowAsLong
import com.github.mustafaozhan.logmob.e
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@Suppress("TooManyFunctions")
class SettingsViewModel(
    private val settingsRepository: SettingsRepository,
    private val apiRepository: ApiRepository,
    private val currencyRepository: CurrencyRepository,
    private val offlineRatesRepository: OfflineRatesRepository,
    private val sessionManager: SessionManager
) : BaseSEEDViewModel(), SettingsEvent {
    // region SEED
    private val _state = MutableStateFlow(SettingsState())
    override val state = _state.asStateFlow()

    override val event = this as SettingsEvent

    private val _effect = MutableSharedFlow<SettingsEffect>()
    override val effect = _effect.asSharedFlow()

    override val data = SettingsData()
    // endregion

    init {
        _state.update(
            appThemeType = AppTheme.getThemeByValueOrDefault(settingsRepository.appTheme),
            addFreeEndDate = settingsRepository.adFreeEndDate.toDateString()
        )

        currencyRepository.collectActiveCurrencies()
            .onEach {
                _state.update(activeCurrencyCount = it.size)
            }.launchIn(clientScope)
    }

    private suspend fun synchroniseRates() {
        _state.update(loading = true)

        _effect.emit(SettingsEffect.Synchronising)

        currencyRepository.getActiveCurrencies()
            .forEach { (name) ->
                delay(SYNC_DELAY)

                runCatching { apiRepository.getRatesByBackend(name) }
                    .onFailure { error -> Logger.e(error) }
                    .onSuccess { offlineRatesRepository.insertOfflineRates(it) }
            }

        _effect.emit(SettingsEffect.Synchronised)

        _state.update(loading = false)
        data.synced = true
    }

    fun updateTheme(theme: AppTheme) = clientScope.launchIgnored {
        _state.update(appThemeType = theme)
        settingsRepository.appTheme = theme.themeValue
        _effect.emit(SettingsEffect.ChangeTheme(theme.themeValue))
    }

    fun shouldShowBannerAd() = sessionManager.shouldShowBannerAd()

    fun isRewardExpired() = settingsRepository.adFreeEndDate.isRewardExpired()

    fun isAdFreeNeverActivated() = settingsRepository.adFreeEndDate == 0.toLong()

    fun getAppTheme() = settingsRepository.appTheme

    // used in ios
    @Suppress("unused")
    fun updateAddFreeDate() = RemoveAdType.VIDEO.calculateAdRewardEnd(nowAsLong()).let {
        settingsRepository.adFreeEndDate = it
        _state.update(addFreeEndDate = it.toDateString())
    }

    // region Event
    override fun onBackClick() = clientScope.launchIgnored {
        Logger.d { "SettingsViewModel onBackClick" }
        _effect.emit(SettingsEffect.Back)
    }

    override fun onCurrenciesClick() = clientScope.launchIgnored {
        Logger.d { "SettingsViewModel onCurrenciesClick" }
        _effect.emit(SettingsEffect.OpenCurrencies)
    }

    override fun onFeedBackClick() = clientScope.launchIgnored {
        Logger.d { "SettingsViewModel onFeedBackClick" }
        _effect.emit(SettingsEffect.FeedBack)
    }

    override fun onShareClick() = clientScope.launchIgnored {
        Logger.d { "SettingsViewModel onShareClick" }
        _effect.emit(SettingsEffect.Share)
    }

    override fun onSupportUsClick() = clientScope.launchIgnored {
        Logger.d { "SettingsViewModel onSupportUsClick" }
        _effect.emit(SettingsEffect.SupportUs)
    }

    override fun onOnGitHubClick() = clientScope.launchIgnored {
        Logger.d { "SettingsViewModel onOnGitHubClick" }
        _effect.emit(SettingsEffect.OnGitHub)
    }

    override fun onRemoveAdsClick() = clientScope.launchIgnored {
        Logger.d { "SettingsViewModel onRemoveAdsClick" }
        if (isRewardExpired()) {
            _effect.emit(SettingsEffect.RemoveAds)
        } else {
            _effect.emit(SettingsEffect.AlreadyAdFree)
        }
    }

    override fun onThemeClick() = clientScope.launchIgnored {
        Logger.d { "SettingsViewModel onThemeClick" }
        _effect.emit(SettingsEffect.ThemeDialog)
    }

    override fun onSyncClick() = clientScope.launchIgnored {
        Logger.d { "SettingsViewModel onSyncClick" }
        if (data.synced) {
            _effect.emit(SettingsEffect.OnlyOneTimeSync)
        } else {
            synchroniseRates()
        }
    }
    // endregion
}
