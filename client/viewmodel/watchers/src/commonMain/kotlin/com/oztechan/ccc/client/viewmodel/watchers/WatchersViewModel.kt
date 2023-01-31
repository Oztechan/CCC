package com.oztechan.ccc.client.viewmodel.watchers

import co.touchlab.kermit.Logger
import com.oztechan.ccc.client.core.viewmodel.BaseSEEDViewModel
import com.oztechan.ccc.client.core.viewmodel.util.launchIgnored
import com.oztechan.ccc.client.core.viewmodel.util.toStandardDigits
import com.oztechan.ccc.client.core.viewmodel.util.toSupportedCharacters
import com.oztechan.ccc.client.core.viewmodel.util.update
import com.oztechan.ccc.client.datasource.currency.CurrencyDataSource
import com.oztechan.ccc.client.datasource.watcher.WatcherDataSource
import com.oztechan.ccc.client.repository.adcontrol.AdControlRepository
import com.oztechan.ccc.client.viewmodel.watchers.WatchersData.Companion.MAXIMUM_INPUT
import com.oztechan.ccc.client.viewmodel.watchers.WatchersData.Companion.MAXIMUM_NUMBER_OF_WATCHER
import com.oztechan.ccc.common.core.model.Watcher
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class WatchersViewModel(
    private val currencyDataSource: CurrencyDataSource,
    private val watcherDataSource: WatcherDataSource,
    private val adControlRepository: AdControlRepository
) : BaseSEEDViewModel<WatchersState, WatchersEffect, WatchersEvent, WatchersData>(), WatchersEvent {
    // region SEED
    private val _state = MutableStateFlow(WatchersState())
    override val state = _state.asStateFlow()

    private val _effect = MutableSharedFlow<WatchersEffect>()
    override val effect = _effect.asSharedFlow()

    override val event = this as WatchersEvent

    override val data = WatchersData()

    init {
        watcherDataSource.getWatchersFlow()
            .onEach {
                _state.update { copy(watcherList = it) }
            }.launchIn(viewModelScope)
    }

    fun shouldShowBannerAd() = adControlRepository.shouldShowBannerAd()

    override fun onBackClick() = viewModelScope.launchIgnored {
        Logger.d { "WatcherViewModel onBackClick" }
        _effect.emit(WatchersEffect.Back)
    }

    override fun onBaseClick(watcher: Watcher) = viewModelScope.launchIgnored {
        Logger.d { "WatcherViewModel onBaseClick $watcher" }
        _effect.emit(WatchersEffect.SelectBase(watcher))
    }

    override fun onTargetClick(watcher: Watcher) = viewModelScope.launchIgnored {
        Logger.d { "WatcherViewModel onTargetClick $watcher" }
        _effect.emit(WatchersEffect.SelectTarget(watcher))
    }

    override fun onBaseChanged(watcher: Watcher, newBase: String) = viewModelScope.launchIgnored {
        Logger.d { "WatcherViewModel onBaseChanged $watcher $newBase" }
        watcherDataSource.updateWatcherBaseById(newBase, watcher.id)
    }

    override fun onTargetChanged(watcher: Watcher, newTarget: String) = viewModelScope.launchIgnored {
        Logger.d { "WatcherViewModel onTargetChanged $watcher $newTarget" }
        watcherDataSource.updateWatcherTargetById(newTarget, watcher.id)
    }

    override fun onAddClick() = viewModelScope.launchIgnored {
        Logger.d { "WatcherViewModel onAddClick" }

        if (watcherDataSource.getWatchers().size >= MAXIMUM_NUMBER_OF_WATCHER) {
            _effect.emit(WatchersEffect.MaximumNumberOfWatchers)
        } else {
            currencyDataSource.getActiveCurrencies().let { list ->
                watcherDataSource.addWatcher(
                    base = list.firstOrNull()?.code.orEmpty(),
                    target = list.lastOrNull()?.code.orEmpty()
                )
            }
        }
    }

    override fun onDeleteClick(watcher: Watcher) = viewModelScope.launchIgnored {
        Logger.d { "WatcherViewModel onDeleteClick $watcher" }
        watcherDataSource.deleteWatcher(watcher.id)
    }

    override fun onRelationChange(watcher: Watcher, isGreater: Boolean) = viewModelScope.launchIgnored {
        Logger.d { "WatcherViewModel onRelationChange $watcher $isGreater" }
        watcherDataSource.updateWatcherRelationById(isGreater, watcher.id)
    }

    override fun onRateChange(watcher: Watcher, rate: String): String {
        Logger.d { "WatcherViewModel onRateChange $watcher $rate" }

        return if (rate.length > MAXIMUM_INPUT) {
            viewModelScope.launch { _effect.emit(WatchersEffect.TooBigNumber) }
            rate.dropLast(1)
        } else {
            rate.toSupportedCharacters().toStandardDigits().toDoubleOrNull()?.let {
                viewModelScope.launch {
                    watcherDataSource.updateWatcherRateById(it, watcher.id)
                }
            } ?: viewModelScope.launch {
                _effect.emit(WatchersEffect.InvalidInput)
            }
            rate
        }
    }
    // endregion
}
