package com.oztechan.ccc.client.viewmodel.watchers

import co.touchlab.kermit.Logger
import com.oztechan.ccc.client.core.analytics.AnalyticsManager
import com.oztechan.ccc.client.core.analytics.model.UserProperty
import com.oztechan.ccc.client.core.shared.util.toStandardDigits
import com.oztechan.ccc.client.core.shared.util.toSupportedCharacters
import com.oztechan.ccc.client.core.viewmodel.SEEDViewModel
import com.oztechan.ccc.client.datasource.currency.CurrencyDataSource
import com.oztechan.ccc.client.datasource.watcher.WatcherDataSource
import com.oztechan.ccc.client.repository.adcontrol.AdControlRepository
import com.oztechan.ccc.client.viewmodel.watchers.WatchersData.Companion.MAXIMUM_INPUT
import com.oztechan.ccc.client.viewmodel.watchers.WatchersData.Companion.MAXIMUM_NUMBER_OF_WATCHER
import com.oztechan.ccc.common.core.model.Watcher
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class WatchersViewModel(
    private val currencyDataSource: CurrencyDataSource,
    private val watcherDataSource: WatcherDataSource,
    adControlRepository: AdControlRepository,
    private val analyticsManager: AnalyticsManager
) : SEEDViewModel<WatchersState, WatchersEffect, WatchersEvent, WatchersData>(
    initialState = WatchersState(isBannerAdVisible = adControlRepository.shouldShowBannerAd()),
    initialData = WatchersData()
),
    WatchersEvent {

    init {
        watcherDataSource.getWatchersFlow()
            .onEach {
                setState { copy(watcherList = it) }
                analyticsManager.setUserProperty(UserProperty.WatcherCount(it.count().toString()))
            }.launchIn(viewModelScope)
    }

    // region Event
    override fun onBackClick() {
        Logger.d { "WatcherViewModel onBackClick" }
        sendEffect { WatchersEffect.Back }
    }

    override fun onBaseClick(watcher: Watcher) {
        Logger.d { "WatcherViewModel onBaseClick $watcher" }
        sendEffect { WatchersEffect.SelectBase(watcher) }
    }

    override fun onTargetClick(watcher: Watcher) {
        Logger.d { "WatcherViewModel onTargetClick $watcher" }
        sendEffect { WatchersEffect.SelectTarget(watcher) }
    }

    override fun onSourceChanged(watcher: Watcher, newBase: String) {
        Logger.d { "WatcherViewModel onSourceChanged $watcher $newBase" }
        viewModelScope.launch {
            watcherDataSource.updateWatcherBaseById(newBase, watcher.id)
        }
    }

    override fun onTargetChanged(watcher: Watcher, newTarget: String) {
        Logger.d { "WatcherViewModel onTargetChanged $watcher $newTarget" }
        viewModelScope.launch {
            watcherDataSource.updateWatcherTargetById(newTarget, watcher.id)
        }
    }

    override fun onAddClick() {
        Logger.d { "WatcherViewModel onAddClick" }

        viewModelScope.launch {
            if (watcherDataSource.getWatchers().size >= MAXIMUM_NUMBER_OF_WATCHER) {
                sendEffect { WatchersEffect.MaximumNumberOfWatchers }
            } else {
                currencyDataSource.getActiveCurrencies().let { list ->
                    watcherDataSource.addWatcher(
                        base = list.firstOrNull()?.code.orEmpty(),
                        target = list.lastOrNull()?.code.orEmpty()
                    )
                }
            }
        }
    }

    override fun onDeleteClick(watcher: Watcher) {
        Logger.d { "WatcherViewModel onDeleteClick $watcher" }
        viewModelScope.launch {
            watcherDataSource.deleteWatcher(watcher.id)
        }
    }

    override fun onRelationChange(watcher: Watcher, isGreater: Boolean) {
        Logger.d { "WatcherViewModel onRelationChange $watcher $isGreater" }
        viewModelScope.launch {
            watcherDataSource.updateWatcherRelationById(isGreater, watcher.id)
        }
    }

    override fun onRateChange(watcher: Watcher, rate: String): String {
        Logger.d { "WatcherViewModel onRateChange $watcher $rate" }

        return if (rate.length > MAXIMUM_INPUT) {
            sendEffect { WatchersEffect.TooBigInput }
            rate.dropLast(1)
        } else {
            rate.toSupportedCharacters().toStandardDigits().toDoubleOrNull()?.let {
                viewModelScope.launch {
                    watcherDataSource.updateWatcherRateById(it, watcher.id)
                }
            } ?: sendEffect {
                WatchersEffect.InvalidInput
            }
            rate
        }
    }
    // endregion
}
