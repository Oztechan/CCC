/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */

package com.oztechan.ccc.client.viewmodel.premium

import co.touchlab.kermit.Logger
import com.github.submob.scopemob.whether
import com.oztechan.ccc.client.core.shared.util.isNotPassed
import com.oztechan.ccc.client.core.viewmodel.BaseData
import com.oztechan.ccc.client.core.viewmodel.BaseSEEDViewModel
import com.oztechan.ccc.client.core.viewmodel.util.launchIgnored
import com.oztechan.ccc.client.core.viewmodel.util.update
import com.oztechan.ccc.client.storage.app.AppStorage
import com.oztechan.ccc.client.viewmodel.premium.model.OldPurchase
import com.oztechan.ccc.client.viewmodel.premium.model.PremiumData
import com.oztechan.ccc.client.viewmodel.premium.model.PremiumType
import com.oztechan.ccc.client.viewmodel.premium.util.calculatePremiumEnd
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow

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

    // region Event
    override fun onPremiumActivated(
        adType: PremiumType?,
        startDate: Long,
        isRestorePurchase: Boolean
    ) = viewModelScope.launchIgnored {
        Logger.d { "PremiumViewModel onPremiumActivated ${adType?.data?.duration.orEmpty()}" }
        adType?.let {
            appStorage.setPremiumEndDate(it.calculatePremiumEnd(startDate))
            _effect.emit(PremiumEffect.PremiumActivated(it, isRestorePurchase))
        }
    }

    override fun onRestorePurchase(oldPurchaseList: List<OldPurchase>) =
        viewModelScope.launchIgnored {
            Logger.d { "PremiumViewModel onRestorePurchase" }
            val premiumEndDate = appStorage.getPremiumEndDate()
            oldPurchaseList
                .maxByOrNull {
                    it.type.calculatePremiumEnd(it.date)
                }?.whether(
                    { type.calculatePremiumEnd(date).isNotPassed() },
                    { date > premiumEndDate },
                    { PremiumType.getPurchaseIds().any { id -> id == type.data.id } }
                )?.run {
                    onPremiumActivated(
                        adType = PremiumType.getById(type.data.id),
                        startDate = this.date,
                        isRestorePurchase = true
                    )
                    _state.update { copy(loading = false) }
                }
        }

    override fun onAddPurchaseMethods(premiumDataList: List<PremiumData>) {
        Logger.d { "PremiumViewModel onAddPurchaseMethods" }
        premiumDataList.forEach { premiumData ->
            val tempList = state.value.premiumTypes.toMutableList()
            PremiumType.getById(premiumData.id)
                ?.apply {
                    data.cost = premiumData.cost
                    data.duration = premiumData.duration
                }?.let {
                    tempList.add(it)
                }
            tempList.sortBy { it.ordinal }
            _state.update { copy(premiumTypes = tempList) }
        }.also {
            _state.update { copy(loading = false) } // in case list is empty, loading will be false
        }
    }

    override fun onPremiumItemClick(type: PremiumType) = viewModelScope.launchIgnored {
        Logger.d { "PremiumViewModel onPremiumItemClick ${type.data.duration}" }
        _state.update {
            copy(loading = type != PremiumType.VIDEO)
        }
        _effect.emit(PremiumEffect.LaunchActivatePremiumFlow(type))
    }

    override fun onPremiumActivationFailed() {
        Logger.d { "PremiumViewModel onPremiumActivationFailed" }
        _state.update { copy(loading = false) }
    }
    // endregion
}
