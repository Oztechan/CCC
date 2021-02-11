/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.client.viewmodel

import com.github.mustafaozhan.ccc.client.base.BaseData
import com.github.mustafaozhan.ccc.client.base.BaseEffect
import com.github.mustafaozhan.ccc.client.base.BaseEvent
import com.github.mustafaozhan.ccc.client.base.BaseSEEDViewModel
import com.github.mustafaozhan.ccc.client.base.BaseState
import com.github.mustafaozhan.ccc.client.util.update
import com.github.mustafaozhan.ccc.common.data.settings.SettingsRepository
import com.github.mustafaozhan.ccc.common.log.kermit
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.datetime.Clock

class AdRemoveViewModel(
    private val settingsRepository: SettingsRepository
) : BaseSEEDViewModel(), AdRemoveEvent {
    // region SEED
    private val _state = MutableStateFlow(AdRemoveState())
    override val state: StateFlow<AdRemoveState> = _state

    private val _effect = Channel<AdRemoveEffect>(1)
    override val effect = _effect.receiveAsFlow().conflate()

    override val event = this as AdRemoveEvent

    override val data: AdRemoveData? = null
    // endregion

    init {
        kermit.d { "AdRemoveViewModel init" }
    }

    fun updateAddFreeDate() = Clock.System.now().toEpochMilliseconds().let {
        settingsRepository.adFreeActivatedDate = it
    }

    fun showLoadingView(shouldShow: Boolean) {
        _state.update(loading = shouldShow)
    }

    override fun onCleared() {
        kermit.d { "AdRemoveViewModel onCleared" }
        super.onCleared()
    }
}

// region SEED
data class AdRemoveState(
    val loading: Boolean = false
) : BaseState()

interface AdRemoveEvent : BaseEvent

sealed class AdRemoveEffect : BaseEffect()

class AdRemoveData : BaseData()
// todo create fake seed if missing
// endregion
