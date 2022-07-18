/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */
package com.oztechan.ccc.client.viewmodel.settings

import co.touchlab.kermit.Logger
import com.github.submob.logmob.e
import com.oztechan.ccc.client.base.BaseSEEDViewModel
import com.oztechan.ccc.client.manager.session.SessionManager
import com.oztechan.ccc.client.model.AppTheme
import com.oztechan.ccc.client.model.RemoveAdType
import com.oztechan.ccc.client.util.calculateAdRewardEnd
import com.oztechan.ccc.client.util.isRewardExpired
import com.oztechan.ccc.client.util.launchIgnored
import com.oztechan.ccc.client.util.toDateString
import com.oztechan.ccc.client.viewmodel.settings.SettingsData.Companion.SYNC_DELAY
import com.oztechan.ccc.common.db.currency.CurrencyRepository
import com.oztechan.ccc.common.db.offlinerates.OfflineRatesRepository
import com.oztechan.ccc.common.db.watcher.WatcherRepository
import com.oztechan.ccc.common.service.backend.BackendApiService
import com.oztechan.ccc.common.settings.SettingsRepository
import com.oztechan.ccc.common.util.nowAsLong
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
    private val backendApiService: BackendApiService,
    private val currencyRepository: CurrencyRepository,
    private val offlineRatesRepository: OfflineRatesRepository,
    watcherRepository: WatcherRepository,
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
            }.launchIn(viewModelScope)

        watcherRepository.collectWatchers()
            .onEach {
                _state.update(activeWatcherCount = it.size)
            }.launchIn(viewModelScope)
    }

    private suspend fun synchroniseRates() {
        _state.update(loading = true)

        _effect.emit(SettingsEffect.Synchronising)

        currencyRepository.getActiveCurrencies()
            .forEach { (name) ->
                delay(SYNC_DELAY)

                runCatching { backendApiService.getRates(name) }
                    .onFailure { error -> Logger.e(error) }
                    .onSuccess { offlineRatesRepository.insertOfflineRates(it) }
            }

        _effect.emit(SettingsEffect.Synchronised)

        _state.update(loading = false)
        data.synced = true
    }

    fun updateTheme(theme: AppTheme) = viewModelScope.launchIgnored {
        _state.update(appThemeType = theme)
        settingsRepository.appTheme = theme.themeValue
        _effect.emit(SettingsEffect.ChangeTheme(theme.themeValue))
    }

    fun shouldShowBannerAd() = sessionManager.shouldShowBannerAd()

    fun isRewardExpired() = settingsRepository.adFreeEndDate.isRewardExpired()

    fun isAdFreeNeverActivated() = settingsRepository.adFreeEndDate == 0.toLong()

    fun getAppTheme() = settingsRepository.appTheme

    @Suppress("unused") // used in iOS
    fun updateAddFreeDate() = RemoveAdType.VIDEO.calculateAdRewardEnd(nowAsLong()).let {
        settingsRepository.adFreeEndDate = it
        _state.update(addFreeEndDate = it.toDateString())
    }

    // region Event
    override fun onBackClick() = viewModelScope.launchIgnored {
        Logger.d { "SettingsViewModel onBackClick" }
        _effect.emit(SettingsEffect.Back)
    }

    override fun onCurrenciesClick() = viewModelScope.launchIgnored {
        Logger.d { "SettingsViewModel onCurrenciesClick" }
        _effect.emit(SettingsEffect.OpenCurrencies)
    }

    override fun onWatchersClicked() = viewModelScope.launchIgnored {
        Logger.d { "SettingsViewModel onWatchersClicked" }
        _effect.emit(SettingsEffect.OpenWatchers)
    }

    override fun onFeedBackClick() = viewModelScope.launchIgnored {
        Logger.d { "SettingsViewModel onFeedBackClick" }
        _effect.emit(SettingsEffect.FeedBack)
    }

    override fun onShareClick() = viewModelScope.launchIgnored {
        Logger.d { "SettingsViewModel onShareClick" }
        _effect.emit(SettingsEffect.Share)
    }

    override fun onSupportUsClick() = viewModelScope.launchIgnored {
        Logger.d { "SettingsViewModel onSupportUsClick" }
        _effect.emit(SettingsEffect.SupportUs)
    }

    override fun onOnGitHubClick() = viewModelScope.launchIgnored {
        Logger.d { "SettingsViewModel onOnGitHubClick" }
        _effect.emit(SettingsEffect.OnGitHub)
    }

    override fun onRemoveAdsClick() = viewModelScope.launchIgnored {
        Logger.d { "SettingsViewModel onRemoveAdsClick" }
        if (isRewardExpired()) {
            _effect.emit(SettingsEffect.RemoveAds)
        } else {
            _effect.emit(SettingsEffect.AlreadyAdFree)
        }
    }

    override fun onThemeClick() = viewModelScope.launchIgnored {
        Logger.d { "SettingsViewModel onThemeClick" }
        _effect.emit(SettingsEffect.ThemeDialog)
    }

    override fun onSyncClick() = viewModelScope.launchIgnored {
        Logger.d { "SettingsViewModel onSyncClick" }
        if (data.synced) {
            _effect.emit(SettingsEffect.OnlyOneTimeSync)
        } else {
            synchroniseRates()
        }
    }
    // endregion
}
