package com.oztechan.ccc.client.viewmodel.notifications

import co.touchlab.kermit.Logger
import com.oztechan.ccc.client.base.BaseData
import com.oztechan.ccc.client.base.BaseSEEDViewModel
import com.oztechan.ccc.client.util.launchIgnored
import com.oztechan.ccc.common.db.currency.CurrencyRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class NotificationsViewModel(
    currencyRepository: CurrencyRepository
) : BaseSEEDViewModel(), NotificationsEvent {
    // region SEED
    private val _state = MutableStateFlow(NotificationsState())
    override val state = _state.asStateFlow()

    override val event = this as NotificationsEvent

    private val _effect = MutableSharedFlow<NotificationsEffect>()
    override val effect = _effect.asSharedFlow()

    override val data: BaseData? = null

    init {
        currencyRepository.collectActiveCurrencies()
            .onEach {
                Logger.d { "NotificationsViewModel currencyList changed\n${it.joinToString("\n")}" }
                _state.update(
                    base = it.firstOrNull()?.name ?: "",
                    target = it.lastOrNull()?.name ?: ""
                )
            }
            .launchIn(clientScope)
    }

    override fun onBackClick() = clientScope.launchIgnored {
        Logger.d { "NotificationsViewModel onBackClick" }
        _effect.emit(NotificationsEffect.Back)
    }

    override fun onBaseChange(base: String) {
        Logger.d { "NotificationsViewModel onBaseClick $base" }
        _state.update(base = base)
    }

    override fun onTargetChange(target: String) {
        Logger.d { "NotificationsViewModel onTargetChange $target" }
        _state.update(target = target)
    }

    override fun onBaseClick() = clientScope.launchIgnored {
        Logger.d { "NotificationsViewModel onBaseClick" }
        _effect.emit(NotificationsEffect.SelectBase)
    }

    override fun onTargetClick() = clientScope.launchIgnored {
        Logger.d { "NotificationsViewModel onTargetClick" }
        _effect.emit(NotificationsEffect.SelectTarget)
    }

    override fun onStateClick() {
        Logger.d { "NotificationsViewModel onStateClick ${!state.value.isEnabled}" }
        _state.update(isEnabled = !state.value.isEnabled)
    }
    // endregion
}
