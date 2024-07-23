/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */
package com.oztechan.ccc.client.viewmodel.settings

import co.touchlab.kermit.Logger
import com.oztechan.ccc.client.core.analytics.AnalyticsManager
import com.oztechan.ccc.client.core.analytics.model.Event
import com.oztechan.ccc.client.core.shared.model.AppTheme
import com.oztechan.ccc.client.core.shared.util.isPassed
import com.oztechan.ccc.client.core.shared.util.toDateString
import com.oztechan.ccc.client.core.viewmodel.SEEDViewModel
import com.oztechan.ccc.client.core.viewmodel.util.launchIgnored
import com.oztechan.ccc.client.datasource.currency.CurrencyDataSource
import com.oztechan.ccc.client.datasource.watcher.WatcherDataSource
import com.oztechan.ccc.client.repository.adcontrol.AdControlRepository
import com.oztechan.ccc.client.repository.appconfig.AppConfigRepository
import com.oztechan.ccc.client.service.backend.BackendApiService
import com.oztechan.ccc.client.storage.app.AppStorage
import com.oztechan.ccc.client.storage.calculation.CalculationStorage
import com.oztechan.ccc.client.viewmodel.settings.SettingsData.Companion.SYNC_DELAY
import com.oztechan.ccc.client.viewmodel.settings.model.PremiumStatus
import com.oztechan.ccc.client.viewmodel.settings.util.indexToNumber
import com.oztechan.ccc.common.datasource.conversion.ConversionDataSource
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@Suppress("TooManyFunctions", "LongParameterList")
class SettingsViewModel(
    private val appStorage: AppStorage,
    private val calculationStorage: CalculationStorage,
    private val backendApiService: BackendApiService,
    private val currencyDataSource: CurrencyDataSource,
    private val conversionDataSource: ConversionDataSource,
    watcherDataSource: WatcherDataSource,
    private val adControlRepository: AdControlRepository,
    private val appConfigRepository: AppConfigRepository,
    private val analyticsManager: AnalyticsManager
) : SEEDViewModel<SettingsState, SettingsEffect, SettingsEvent, SettingsData>(
    initialState = SettingsState(isBannerAdVisible = adControlRepository.shouldShowBannerAd()),
    initialData = SettingsData()
),
    SettingsEvent {

    init {
        setState {
            copy(
                appThemeType = AppTheme.getThemeByValueOrDefault(appStorage.appTheme),
                premiumStatus = appStorage.premiumEndDate.toPremiumStatus(),
                precision = calculationStorage.precision,
                version = appConfigRepository.getVersion()
            )
        }

        currencyDataSource.getActiveCurrenciesFlow()
            .onEach {
                setState { copy(activeCurrencyCount = it.size) }
            }.launchIn(viewModelScope)

        watcherDataSource.getWatchersFlow()
            .onEach {
                setState { copy(activeWatcherCount = it.size) }
            }.launchIn(viewModelScope)
    }

    private fun Long.toPremiumStatus(): PremiumStatus = when {
        this == 0.toLong() -> PremiumStatus.NeverActivated
        isPassed() -> PremiumStatus.Expired(toDateString())
        else -> PremiumStatus.Active(toDateString())
    }

    private suspend fun synchroniseConversions() {
        setEffect { SettingsEffect.Synchronising }

        currencyDataSource.getActiveCurrencies()
            .forEach { (name) ->
                runCatching { backendApiService.getConversion(name) }
                    .onFailure { error -> Logger.w(error) { error.message.toString() } }
                    .onSuccess { conversionDataSource.insertConversion(it) }

                delay(SYNC_DELAY)
            }

        setEffect { SettingsEffect.Synchronised }

        data.synced = true
    }

    // region Event
    override fun onBackClick() = viewModelScope.launchIgnored {
        Logger.d { "SettingsViewModel onBackClick" }
        setEffect { SettingsEffect.Back }
    }

    override fun onCurrenciesClick() = viewModelScope.launchIgnored {
        Logger.d { "SettingsViewModel onCurrenciesClick" }
        setEffect { SettingsEffect.OpenCurrencies }
    }

    override fun onWatchersClick() = viewModelScope.launchIgnored {
        Logger.d { "SettingsViewModel onWatchersClick" }
        setEffect { SettingsEffect.OpenWatchers }
    }

    override fun onFeedBackClick() = viewModelScope.launchIgnored {
        Logger.d { "SettingsViewModel onFeedBackClick" }
        setEffect { SettingsEffect.FeedBack }
    }

    override fun onShareClick() = viewModelScope.launchIgnored {
        Logger.d { "SettingsViewModel onShareClick" }
        setEffect { SettingsEffect.Share(appConfigRepository.getMarketLink()) }
    }

    override fun onSupportUsClick() = viewModelScope.launchIgnored {
        Logger.d { "SettingsViewModel onSupportUsClick" }
        setEffect { SettingsEffect.SupportUs(appConfigRepository.getMarketLink()) }
    }

    override fun onPrivacySettingsClick() = viewModelScope.launchIgnored {
        Logger.d { "SettingsViewModel onPrivacySettingsClick" }
        setEffect { SettingsEffect.PrivacySettings }
    }

    override fun onOnGitHubClick() = viewModelScope.launchIgnored {
        Logger.d { "SettingsViewModel onOnGitHubClick" }
        setEffect { SettingsEffect.OnGitHub }
    }

    override fun onPremiumClick() = viewModelScope.launchIgnored {
        Logger.d { "SettingsViewModel onPremiumClick" }
        setEffect {
            if (appStorage.premiumEndDate.isPassed()) {
                SettingsEffect.Premium
            } else {
                SettingsEffect.AlreadyPremium
            }
        }
    }

    override fun onThemeClick() = viewModelScope.launchIgnored {
        Logger.d { "SettingsViewModel onThemeClick" }
        setEffect { SettingsEffect.ThemeDialog }
    }

    override fun onSyncClick() = viewModelScope.launchIgnored {
        Logger.d { "SettingsViewModel onSyncClick" }

        analyticsManager.trackEvent(Event.OfflineSync)

        if (data.synced) {
            setEffect { SettingsEffect.OnlyOneTimeSync }
        } else {
            synchroniseConversions()
        }
    }

    override fun onPrecisionClick() = viewModelScope.launchIgnored {
        Logger.d { "SettingsViewModel onPrecisionClick" }
        setEffect { SettingsEffect.SelectPrecision }
    }

    override fun onPrecisionSelect(index: Int) {
        Logger.d { "SettingsViewModel onPrecisionSelect $index" }
        calculationStorage.precision = index.indexToNumber()
        setState { copy(precision = index.indexToNumber()) }
    }

    override fun onThemeChange(theme: AppTheme) = viewModelScope.launchIgnored {
        Logger.d { "SettingsViewModel onThemeChange $theme" }
        setState { copy(appThemeType = theme) }
        appStorage.appTheme = theme.themeValue
        setEffect { SettingsEffect.ChangeTheme(theme.themeValue) }
    }
    // endregion
}
