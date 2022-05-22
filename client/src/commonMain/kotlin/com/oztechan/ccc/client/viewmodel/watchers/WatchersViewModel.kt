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
import com.oztechan.ccc.common.db.currency.CurrencyRepository
import com.oztechan.ccc.common.db.watcher.WatcherRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class WatchersViewModel(
    private val currencyRepository: CurrencyRepository,
    private val watcherRepository: WatcherRepository
) : BaseSEEDViewModel(), WatchersEvent {
    // region SEED
    private val _state = MutableStateFlow(WatchersState())
    override val state = _state.asStateFlow()

    override val event = this as WatchersEvent

    private val _effect = MutableSharedFlow<WatchersEffect>()
    override val effect = _effect.asSharedFlow()

    override val data = WatchersData()

    init {
        watcherRepository.collectWatchers()
            .onEach {
                _state.update(watcherList = it.toUIModelList())
            }.launchIn(clientScope)
    }

    override fun onBackClick() = clientScope.launchIgnored {
        Logger.d { "WatcherViewModel onBackClick" }
        _effect.emit(WatchersEffect.Back)
    }

    override fun onBaseClick(watcher: Watcher) = clientScope.launchIgnored {
        Logger.d { "WatcherViewModel onBaseClick $watcher" }
        _effect.emit(WatchersEffect.SelectBase(watcher))
    }

    override fun onTargetClick(watcher: Watcher) = clientScope.launchIgnored {
        Logger.d { "WatcherViewModel onTargetClick $watcher" }
        _effect.emit(WatchersEffect.SelectTarget(watcher))
    }

    override fun onBaseChanged(watcher: Watcher?, newBase: String) {
        Logger.d { "WatcherViewModel onBaseChanged $watcher $newBase" }
        watcher?.id?.let {
            watcherRepository.updateBaseById(newBase, it)
        }
    }

    override fun onTargetChanged(watcher: Watcher?, newTarget: String) {
        Logger.d { "WatcherViewModel onTargetChanged $watcher $newTarget" }
        watcher?.id?.let {
            watcherRepository.updateTargetById(newTarget, it)
        }
    }

    override fun onAddClick() {
        Logger.d { "WatcherViewModel onAddClick" }
        if (watcherRepository.getWatchers().size >= MAXIMUM_NUMBER_OF_WATCHER) {
            clientScope.launch { _effect.emit(WatchersEffect.MaximumNumberOfWatchers) }
        } else {
            currencyRepository.getActiveCurrencies().let { list ->
                watcherRepository.addWatcher(
                    base = list.firstOrNull()?.name ?: "",
                    target = list.lastOrNull()?.name ?: ""
                )
            }
        }
    }

    override fun onDeleteClick(watcher: Watcher) {
        Logger.d { "WatcherViewModel onDeleteClick $watcher" }
        watcherRepository.deleteWatcher(watcher.id)
    }

    override fun onRelationChange(watcher: Watcher, isGreater: Boolean) {
        Logger.d { "WatcherViewModel onRelationChange $watcher $isGreater" }
        watcherRepository.updateRelationById(isGreater, watcher.id)
    }

    override fun onRateChange(watcher: Watcher, rate: String): String {
        Logger.d { "WatcherViewModel onRateChange $watcher $rate" }

        return when {
            rate.length > MAXIMUM_INPUT -> {
                clientScope.launch { _effect.emit(WatchersEffect.MaximumInput) }
                rate.dropLast(1)
            }
            rate.toDoubleOrNull()?.isNaN() != false -> {
                clientScope.launch { _effect.emit(WatchersEffect.InvalidInput) }
                rate
            }
            else -> {
                watcherRepository.updateRateById(
                    rate.toSupportedCharacters().toStandardDigits().toDoubleOrNull() ?: 0.0,
                    watcher.id
                )
                rate
            }
        }
    }
    // endregion
}
