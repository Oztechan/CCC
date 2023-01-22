/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */
package com.oztechan.ccc.client.viewmodel.settings

import co.touchlab.kermit.Logger
import com.github.submob.logmob.e
import com.oztechan.ccc.analytics.AnalyticsManager
import com.oztechan.ccc.analytics.model.Event
import com.oztechan.ccc.client.base.BaseSEEDViewModel
import com.oztechan.ccc.client.model.AppTheme
import com.oztechan.ccc.client.model.PremiumType
import com.oztechan.ccc.client.repository.ad.AdRepository
import com.oztechan.ccc.client.repository.appconfig.AppConfigRepository
import com.oztechan.ccc.client.storage.app.AppStorage
import com.oztechan.ccc.client.storage.calculator.CalculatorStorage
import com.oztechan.ccc.client.util.calculatePremiumEnd
import com.oztechan.ccc.client.util.indexToNumber
import com.oztechan.ccc.client.util.isPremiumExpired
import com.oztechan.ccc.client.util.launchIgnored
import com.oztechan.ccc.client.util.toDateString
import com.oztechan.ccc.client.util.update
import com.oztechan.ccc.client.viewmodel.settings.SettingsData.Companion.SYNC_DELAY
import com.oztechan.ccc.common.core.infrastructure.util.nowAsLong
import com.oztechan.ccc.common.data.datasource.currency.CurrencyDataSource
import com.oztechan.ccc.common.data.datasource.watcher.WatcherDataSource
import com.oztechan.ccc.common.datasource.conversion.ConversionDataSource
import com.oztechan.ccc.common.service.backend.BackendApiService
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@Suppress("TooManyFunctions", "LongParameterList")
class SettingsViewModel(
    private val appStorage: AppStorage,
    private val calculatorStorage: CalculatorStorage,
    private val backendApiService: BackendApiService,
    private val currencyDataSource: CurrencyDataSource,
    private val conversionDataSource: ConversionDataSource,
    watcherDataSource: WatcherDataSource,
    private val adRepository: AdRepository,
    private val appConfigRepository: AppConfigRepository,
    private val analyticsManager: AnalyticsManager
) : BaseSEEDViewModel<SettingsState, SettingsEffect, SettingsEvent, SettingsData>(), SettingsEvent {
    // region SEED
    private val _state = MutableStateFlow(SettingsState())
    override val state = _state.asStateFlow()

    private val _effect = MutableSharedFlow<SettingsEffect>()
    override val effect = _effect.asSharedFlow()

    override val event = this as SettingsEvent

    override val data = SettingsData()
    // endregion

    init {
        _state.update {
            copy(
                appThemeType = AppTheme.getThemeByValueOrDefault(appStorage.appTheme),
                premiumEndDate = appStorage.premiumEndDate.toDateString(),
                precision = calculatorStorage.precision,
                version = appConfigRepository.getVersion()
            )
        }

        currencyDataSource.getActiveCurrenciesFlow()
            .onEach {
                _state.update { copy(activeCurrencyCount = it.size) }
            }.launchIn(viewModelScope)

        watcherDataSource.getWatchersFlow()
            .onEach {
                _state.update { copy(activeWatcherCount = it.size) }
            }.launchIn(viewModelScope)
    }

    private suspend fun synchroniseConversions() {
        _state.update { copy(loading = true) }

        _effect.emit(SettingsEffect.Synchronising)

        currencyDataSource.getActiveCurrencies()
            .forEach { (name) ->
                delay(SYNC_DELAY)

                runCatching { backendApiService.getConversion(name) }
                    .onFailure { error -> Logger.e(error) }
                    .onSuccess { conversionDataSource.insertConversion(it) }
            }

        _effect.emit(SettingsEffect.Synchronised)

        _state.update { copy(loading = false) }
        data.synced = true
    }

    fun updateTheme(theme: AppTheme) = viewModelScope.launchIgnored {
        _state.update { copy(appThemeType = theme) }
        appStorage.appTheme = theme.themeValue
        _effect.emit(SettingsEffect.ChangeTheme(theme.themeValue))
    }

    fun shouldShowBannerAd() = adRepository.shouldShowBannerAd()

    fun isPremiumExpired() = appStorage.premiumEndDate.isPremiumExpired()

    fun isPremiumEverActivated() = appStorage.premiumEndDate == 0.toLong()

    fun getAppTheme() = appStorage.appTheme

    @Suppress("unused") // used in iOS
    fun updatePremiumEndDate() = PremiumType.VIDEO.calculatePremiumEnd(nowAsLong()).let {
        appStorage.premiumEndDate = it
        _state.update { copy(premiumEndDate = it.toDateString()) }
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

    override fun onWatchersClick() = viewModelScope.launchIgnored {
        Logger.d { "SettingsViewModel onWatchersClick" }
        _effect.emit(SettingsEffect.OpenWatchers)
    }

    override fun onFeedBackClick() = viewModelScope.launchIgnored {
        Logger.d { "SettingsViewModel onFeedBackClick" }
        _effect.emit(SettingsEffect.FeedBack)
    }

    override fun onShareClick() = viewModelScope.launchIgnored {
        Logger.d { "SettingsViewModel onShareClick" }
        _effect.emit(SettingsEffect.Share(appConfigRepository.getMarketLink()))
    }

    override fun onSupportUsClick() = viewModelScope.launchIgnored {
        Logger.d { "SettingsViewModel onSupportUsClick" }
        _effect.emit(SettingsEffect.SupportUs(appConfigRepository.getMarketLink()))
    }

    override fun onOnGitHubClick() = viewModelScope.launchIgnored {
        Logger.d { "SettingsViewModel onOnGitHubClick" }
        _effect.emit(SettingsEffect.OnGitHub)
    }

    override fun onPremiumClick() = viewModelScope.launchIgnored {
        Logger.d { "SettingsViewModel onPremiumClick" }
        if (isPremiumExpired()) {
            _effect.emit(SettingsEffect.Premium)
        } else {
            _effect.emit(SettingsEffect.AlreadyPremium)
        }
    }

    override fun onThemeClick() = viewModelScope.launchIgnored {
        Logger.d { "SettingsViewModel onThemeClick" }
        _effect.emit(SettingsEffect.ThemeDialog)
    }

    override fun onSyncClick() = viewModelScope.launchIgnored {
        Logger.d { "SettingsViewModel onSyncClick" }

        analyticsManager.trackEvent(Event.OfflineSync)

        if (data.synced) {
            _effect.emit(SettingsEffect.OnlyOneTimeSync)
        } else {
            synchroniseConversions()
        }
    }

    override fun onPrecisionClick() = viewModelScope.launchIgnored {
        Logger.d { "SettingsViewModel onPrecisionClick" }
        _effect.emit(SettingsEffect.SelectPrecision)
    }

    override fun onPrecisionSelect(index: Int) {
        Logger.d { "SettingsViewModel onPrecisionSelect $index" }
        calculatorStorage.precision = index.indexToNumber()
        _state.update { copy(precision = index.indexToNumber()) }
    }
    // endregion
}
