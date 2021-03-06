/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.client.viewmodel

import com.github.mustafaozhan.ccc.client.base.BaseData
import com.github.mustafaozhan.ccc.client.base.BaseEffect
import com.github.mustafaozhan.ccc.client.base.BaseEvent
import com.github.mustafaozhan.ccc.client.base.BaseSEEDViewModel
import com.github.mustafaozhan.ccc.client.base.BaseState
import com.github.mustafaozhan.ccc.client.model.RemoveAdType
import com.github.mustafaozhan.ccc.client.util.toUnit
import com.github.mustafaozhan.ccc.client.util.update
import com.github.mustafaozhan.ccc.common.settings.SettingsRepository
import com.github.mustafaozhan.logmob.kermit
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus

class AdRemoveViewModel(
    private val settingsRepository: SettingsRepository
) : BaseSEEDViewModel(), AdRemoveEvent {
    // region SEED
    private val _state = MutableStateFlow(AdRemoveState())
    override val state: StateFlow<AdRemoveState> = _state

    private val _effect = Channel<AdRemoveEffect>(1)
    override val effect = _effect.receiveAsFlow().conflate()

    override val event = this as AdRemoveEvent

    override val data: BaseData? = null
    // endregion

    init {
        kermit.d { "AdRemoveViewModel init" }
        _state.update(adRemoveTypes = mutableListOf(RemoveAdType.VIDEO))
    }

    @Suppress("MagicNumber")
    fun updateAddFreeDate(adType: RemoveAdType, startDate: Instant = Clock.System.now()) {
        settingsRepository.adFreeEndDate = when (adType) {
            RemoveAdType.VIDEO -> startDate.plus(
                3,
                DateTimeUnit.DAY,
                TimeZone.currentSystemDefault()
            ).toEpochMilliseconds()
            RemoveAdType.MONTH -> startDate.plus(
                1,
                DateTimeUnit.MONTH,
                TimeZone.currentSystemDefault()
            ).toEpochMilliseconds()
            RemoveAdType.QUARTER -> startDate.plus(
                3,
                DateTimeUnit.MONTH,
                TimeZone.currentSystemDefault()
            ).toEpochMilliseconds()
            RemoveAdType.HALF_YEAR -> startDate.plus(
                6,
                DateTimeUnit.MONTH,
                TimeZone.currentSystemDefault()
            ).toEpochMilliseconds()
            RemoveAdType.YEAR -> startDate.plus(
                1,
                DateTimeUnit.YEAR,
                TimeZone.currentSystemDefault()
            ).toEpochMilliseconds()
        }
        clientScope.launch {
            _effect.send(AdRemoveEffect.RestartActivity)
        }
    }

    fun validatePurchaseHistory(pair: Pair<String, Long>) = RemoveAdType.values()
        .firstOrNull { it.skuId == pair.first }
        ?.let { updateAddFreeDate(it, Instant.fromEpochMilliseconds(pair.second)) }

    fun showLoadingView(shouldShow: Boolean) {
        _state.update(loading = shouldShow)
    }

    fun addInAppBillingMethods(billingMethods: List<Triple<String, String, String>>) =
        billingMethods.forEach { billingMethod ->
            val tempList = state.value.adRemoveTypes.toMutableList()
            RemoveAdType.values().firstOrNull { it.skuId == billingMethod.first }?.let {
                val adType = it
                adType.cost = billingMethod.second
                adType.reward = billingMethod.third
                tempList.add(adType)
            }
            tempList.sortBy { it.ordinal }
            _state.update(adRemoveTypes = tempList, loading = false)
        }

    override fun onCleared() {
        kermit.d { "AdRemoveViewModel onCleared" }
        super.onCleared()
    }

    override fun onAdRemoveItemClick(type: RemoveAdType) = clientScope.launch {
        _effect.send(AdRemoveEffect.RemoveAd(type))
    }.toUnit()
}

// region SEED
data class AdRemoveState(
    val adRemoveTypes: List<RemoveAdType> = listOf(),
    val loading: Boolean = false
) : BaseState() {
    // for ios
    constructor() : this(listOf<RemoveAdType>(), false)
}

interface AdRemoveEvent : BaseEvent {
    fun onAdRemoveItemClick(type: RemoveAdType)
}

sealed class AdRemoveEffect : BaseEffect() {
    data class RemoveAd(val removeAdType: RemoveAdType) : AdRemoveEffect()
    object RestartActivity : AdRemoveEffect()
}
// endregion
