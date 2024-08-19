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
import kotlinx.coroutines.launch

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

    private fun synchroniseConversions() {
        viewModelScope.launch {
            sendEffect { SettingsEffect.Synchronising }

            currencyDataSource.getActiveCurrencies()
                .forEach { (name) ->
                    runCatching { backendApiService.getConversion(name) }
                        .onFailure { error -> Logger.w(error) { error.message.toString() } }
                        .onSuccess { conversionDataSource.insertConversion(it) }

                    delay(SYNC_DELAY)
                }

            sendEffect { SettingsEffect.Synchronised }

            data.synced = true
        }
    }

    // region Event
    override fun onBackClick() {
        Logger.d { "SettingsViewModel onBackClick" }
        sendEffect { SettingsEffect.Back }
    }

    override fun onCurrenciesClick() {
        Logger.d { "SettingsViewModel onCurrenciesClick" }
        sendEffect { SettingsEffect.OpenCurrencies }
    }

    override fun onWatchersClick() {
        Logger.d { "SettingsViewModel onWatchersClick" }
        sendEffect { SettingsEffect.OpenWatchers }
    }

    override fun onFeedBackClick() {
        Logger.d { "SettingsViewModel onFeedBackClick" }
        sendEffect { SettingsEffect.FeedBack }
    }

    override fun onShareClick() {
        Logger.d { "SettingsViewModel onShareClick" }
        sendEffect { SettingsEffect.Share(appConfigRepository.getMarketLink()) }
    }

    override fun onSupportUsClick() {
        Logger.d { "SettingsViewModel onSupportUsClick" }
        sendEffect { SettingsEffect.SupportUs(appConfigRepository.getMarketLink()) }
    }

    override fun onPrivacySettingsClick() {
        Logger.d { "SettingsViewModel onPrivacySettingsClick" }
        sendEffect { SettingsEffect.PrivacySettings }
    }

    override fun onOnGitHubClick() {
        Logger.d { "SettingsViewModel onOnGitHubClick" }
        sendEffect { SettingsEffect.OnGitHub }
    }

    override fun onPremiumClick() {
        Logger.d { "SettingsViewModel onPremiumClick" }
        sendEffect {
            if (appStorage.premiumEndDate.isPassed()) {
                SettingsEffect.Premium
            } else {
                SettingsEffect.AlreadyPremium
            }
        }
    }

    override fun onThemeClick() {
        Logger.d { "SettingsViewModel onThemeClick" }
        sendEffect { SettingsEffect.ThemeDialog }
    }

    override fun onSyncClick() {
        Logger.d { "SettingsViewModel onSyncClick" }

        analyticsManager.trackEvent(Event.OfflineSync)

        if (data.synced) {
            sendEffect { SettingsEffect.OnlyOneTimeSync }
        } else {
            synchroniseConversions()
        }
    }

    override fun onPrecisionClick() {
        Logger.d { "SettingsViewModel onPrecisionClick" }
        sendEffect { SettingsEffect.SelectPrecision }
    }

    override fun onPrecisionSelect(index: Int) {
        Logger.d { "SettingsViewModel onPrecisionSelect $index" }
        calculationStorage.precision = index.indexToNumber()
        setState { copy(precision = index.indexToNumber()) }
    }

    override fun onThemeChange(theme: AppTheme) {
        Logger.d { "SettingsViewModel onThemeChange $theme" }
        setState { copy(appThemeType = theme) }
        appStorage.appTheme = theme.themeValue
        sendEffect { SettingsEffect.ChangeTheme(theme.themeValue) }
    }
    // endregion
}
