/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */

package com.oztechan.ccc.client.viewmodel.adremove

import co.touchlab.kermit.Logger
import com.github.submob.scopemob.whether
import com.oztechan.ccc.client.base.BaseData
import com.oztechan.ccc.client.base.BaseSEEDViewModel
import com.oztechan.ccc.client.model.OldPurchase
import com.oztechan.ccc.client.model.RemoveAdData
import com.oztechan.ccc.client.model.RemoveAdType
import com.oztechan.ccc.client.util.calculateAdRewardEnd
import com.oztechan.ccc.client.util.isRewardExpired
import com.oztechan.ccc.client.util.launchIgnored
import com.oztechan.ccc.client.util.update
import com.oztechan.ccc.common.storage.AppStorage
import com.oztechan.ccc.common.util.nowAsLong
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AdRemoveViewModel(
    private val appStorage: AppStorage
) : BaseSEEDViewModel<AdRemoveState, AdRemoveEffect, AdRemoveEvent, BaseData>(), AdRemoveEvent {
    // region SEED
    private val _state = MutableStateFlow(AdRemoveState())
    override val state = _state.asStateFlow()

    private val _effect = MutableSharedFlow<AdRemoveEffect>()
    override val effect = _effect.asSharedFlow()

    override val event = this as AdRemoveEvent

    override val data: BaseData? = null
    // endregion

    fun updateAddFreeDate(
        adType: RemoveAdType?,
        startDate: Long = nowAsLong(),
        isRestorePurchase: Boolean = false
    ) = adType?.let {
        viewModelScope.launch {
            appStorage.adFreeEndDate = it.calculateAdRewardEnd(startDate)
            _effect.emit(AdRemoveEffect.AdsRemoved(it, isRestorePurchase))
        }
    }

    fun restorePurchase(oldPurchaseList: List<OldPurchase>) = oldPurchaseList
        .maxByOrNull {
            it.type.calculateAdRewardEnd(it.date)
        }?.whether(
            { !type.calculateAdRewardEnd(date).isRewardExpired() },
            { date > appStorage.adFreeEndDate },
            { RemoveAdType.getPurchaseIds().any { it == type.data.id } }
        )?.apply {
            updateAddFreeDate(
                adType = RemoveAdType.getById(type.data.id),
                startDate = this.date,
                isRestorePurchase = true
            )
        }

    fun showLoadingView(shouldShow: Boolean) = _state.update {
        copy(loading = shouldShow)
    }

    fun addPurchaseMethods(removeAdDataList: List<RemoveAdData>) = removeAdDataList
        .forEach { removeAdData ->
            val tempList = state.value.adRemoveTypes.toMutableList()
            RemoveAdType.getById(removeAdData.id)
                ?.apply {
                    data.cost = removeAdData.cost
                    data.reward = removeAdData.reward
                }?.let {
                    tempList.add(it)
                }
            tempList.sortBy { it.ordinal }
            _state.update { copy(adRemoveTypes = tempList, loading = false) }
        }

    override fun onAdRemoveItemClick(type: RemoveAdType) = viewModelScope.launchIgnored {
        Logger.d { "AdRemoveViewModel onAdRemoveItemClick ${type.data.reward}" }
        _effect.emit(AdRemoveEffect.LaunchRemoveAdFlow(type))
    }
}
