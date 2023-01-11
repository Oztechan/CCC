/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */

package com.oztechan.ccc.client.viewmodel.premium

import co.touchlab.kermit.Logger
import com.github.submob.scopemob.whether
import com.oztechan.ccc.client.base.BaseData
import com.oztechan.ccc.client.base.BaseSEEDViewModel
import com.oztechan.ccc.client.model.OldPurchase
import com.oztechan.ccc.client.model.PremiumData
import com.oztechan.ccc.client.model.PremiumType
import com.oztechan.ccc.client.storage.app.AppStorage
import com.oztechan.ccc.client.util.calculateAdRewardEnd
import com.oztechan.ccc.client.util.isRewardExpired
import com.oztechan.ccc.client.util.launchIgnored
import com.oztechan.ccc.client.util.update
import com.oztechan.ccc.common.util.nowAsLong
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PremiumViewModel(
    private val appStorage: AppStorage
) : BaseSEEDViewModel<PremiumState, PremiumEffect, PremiumEvent, BaseData>(), PremiumEvent {
    // region SEED
    private val _state = MutableStateFlow(PremiumState())
    override val state = _state.asStateFlow()

    private val _effect = MutableSharedFlow<PremiumEffect>()
    override val effect = _effect.asSharedFlow()

    override val event = this as PremiumEvent

    override val data: BaseData? = null
    // endregion

    fun updateAddFreeDate(
        adType: PremiumType?,
        startDate: Long = nowAsLong(),
        isRestorePurchase: Boolean = false
    ) = adType?.let {
        viewModelScope.launch {
            appStorage.adFreeEndDate = it.calculateAdRewardEnd(startDate)
            _effect.emit(PremiumEffect.PremiumActivated(it, isRestorePurchase))
        }
    }

    fun restorePurchase(oldPurchaseList: List<OldPurchase>) = oldPurchaseList
        .maxByOrNull {
            it.type.calculateAdRewardEnd(it.date)
        }?.whether(
            { !type.calculateAdRewardEnd(date).isRewardExpired() },
            { date > appStorage.adFreeEndDate },
            { PremiumType.getPurchaseIds().any { it == type.data.id } }
        )?.apply {
            updateAddFreeDate(
                adType = PremiumType.getById(type.data.id),
                startDate = this.date,
                isRestorePurchase = true
            )
        }

    fun showLoadingView(shouldShow: Boolean) = _state.update {
        copy(loading = shouldShow)
    }

    fun addPurchaseMethods(premiumDataList: List<PremiumData>) = premiumDataList
        .forEach { premiumData ->
            val tempList = state.value.premiumTypes.toMutableList()
            PremiumType.getById(premiumData.id)
                ?.apply {
                    data.cost = premiumData.cost
                    data.reward = premiumData.reward
                }?.let {
                    tempList.add(it)
                }
            tempList.sortBy { it.ordinal }
            _state.update { copy(premiumTypes = tempList, loading = false) }
        }

    override fun onPremiumItemClick(type: PremiumType) = viewModelScope.launchIgnored {
        Logger.d { "PremiumViewModel onPremiumItemClick ${type.data.reward}" }
        _effect.emit(PremiumEffect.LaunchActivatePremiumFlow(type))
    }
}
