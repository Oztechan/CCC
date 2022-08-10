package com.oztechan.ccc.client.viewmodel.watchers

import co.touchlab.kermit.Logger
import com.oztechan.ccc.client.base.BaseSEEDViewModel
import com.oztechan.ccc.client.mapper.toUIModelList
import com.oztechan.ccc.client.model.Watcher
import com.oztechan.ccc.client.util.launchIgnored
import com.oztechan.ccc.client.util.toStandardDigits
import com.oztechan.ccc.client.util.toSupportedCharacters
import com.oztechan.ccc.client.viewmodel.watchers.WatchersData.Companion.MAXIMUM_INPUT
import com.oztechan.ccc.client.viewmodel.watchers.WatchersData.Companion.MAXIMUM_NUMBER_OF_WATCHER
import com.oztechan.ccc.common.datasource.currency.CurrencyDataSource
import com.oztechan.ccc.common.datasource.watcher.WatcherDataSource
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class WatchersViewModel(
    private val currencyDataSource: CurrencyDataSource,
    private val watcherDataSource: WatcherDataSource
) : BaseSEEDViewModel(), WatchersEvent {
    // region SEED
    private val _state = MutableStateFlow(WatchersState())
    override val state = _state.asStateFlow()

    override val event = this as WatchersEvent

    private val _effect = MutableSharedFlow<WatchersEffect>()
    override val effect = _effect.asSharedFlow()

    override val data = WatchersData()

    init {
        watcherDataSource.collectWatchers()
            .onEach {
                _state.update(watcherList = it.toUIModelList())
            }.launchIn(viewModelScope)
    }

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

    override fun onBaseChanged(watcher: Watcher?, newBase: String) {
        Logger.d { "WatcherViewModel onBaseChanged $watcher $newBase" }
        watcher?.id?.let {
            watcherDataSource.updateBaseById(newBase, it)
        }
    }

    override fun onTargetChanged(watcher: Watcher?, newTarget: String) {
        Logger.d { "WatcherViewModel onTargetChanged $watcher $newTarget" }
        watcher?.id?.let {
            watcherDataSource.updateTargetById(newTarget, it)
        }
    }

    override fun onAddClick() = viewModelScope.launchIgnored {
        Logger.d { "WatcherViewModel onAddClick" }

        if (watcherDataSource.getWatchers().size >= MAXIMUM_NUMBER_OF_WATCHER) {
            _effect.emit(WatchersEffect.MaximumNumberOfWatchers)
        } else {
            currencyDataSource.getActiveCurrencies().let { list ->
                watcherDataSource.addWatcher(
                    base = list.firstOrNull()?.name.orEmpty(),
                    target = list.lastOrNull()?.name.orEmpty()
                )
            }
        }
    }

    override fun onDeleteClick(watcher: Watcher) {
        Logger.d { "WatcherViewModel onDeleteClick $watcher" }
        watcherDataSource.deleteWatcher(watcher.id)
    }

    override fun onRelationChange(watcher: Watcher, isGreater: Boolean) {
        Logger.d { "WatcherViewModel onRelationChange $watcher $isGreater" }
        watcherDataSource.updateRelationById(isGreater, watcher.id)
    }

    override fun onRateChange(watcher: Watcher, rate: String): String {
        Logger.d { "WatcherViewModel onRateChange $watcher $rate" }

        return when {
            rate.length > MAXIMUM_INPUT -> {
                viewModelScope.launch { _effect.emit(WatchersEffect.MaximumInput) }
                rate.dropLast(1)
            }
            rate.toDoubleOrNull()?.isNaN() != false -> {
                viewModelScope.launch { _effect.emit(WatchersEffect.InvalidInput) }
                rate
            }
            else -> {
                watcherDataSource.updateRateById(
                    rate.toSupportedCharacters().toStandardDigits().toDoubleOrNull() ?: 0.0,
                    watcher.id
                )
                rate
            }
        }
    }
    // endregion
}
