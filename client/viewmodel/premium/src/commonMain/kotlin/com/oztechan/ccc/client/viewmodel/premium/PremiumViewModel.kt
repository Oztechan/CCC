/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */

package com.oztechan.ccc.client.viewmodel.premium

import co.touchlab.kermit.Logger
import com.github.submob.scopemob.whether
import com.oztechan.ccc.client.core.shared.util.isNotPassed
import com.oztechan.ccc.client.core.shared.util.isPassed
import com.oztechan.ccc.client.core.viewmodel.BaseData
import com.oztechan.ccc.client.core.viewmodel.SEEDViewModel
import com.oztechan.ccc.client.storage.app.AppStorage
import com.oztechan.ccc.client.viewmodel.premium.model.OldPurchase
import com.oztechan.ccc.client.viewmodel.premium.model.PremiumData
import com.oztechan.ccc.client.viewmodel.premium.model.PremiumType
import com.oztechan.ccc.client.viewmodel.premium.util.calculatePremiumEnd

class PremiumViewModel(
    private val appStorage: AppStorage
) : SEEDViewModel<PremiumState, PremiumEffect, PremiumEvent, BaseData>(
    initialState = PremiumState()
),
    PremiumEvent {

    // region Event
    override fun onPremiumActivated(
        adType: PremiumType?,
        startDate: Long,
        isRestorePurchase: Boolean
    ) {
        Logger.d { "PremiumViewModel onPremiumActivated ${adType?.data?.duration.orEmpty()}" }
        adType?.let {
            appStorage.premiumEndDate = it.calculatePremiumEnd(startDate)
            sendEffect { PremiumEffect.PremiumActivated(it, isRestorePurchase) }
        }
    }

    override fun onRestoreOrConsumePurchase(oldPurchaseList: List<OldPurchase>) {
        Logger.d { "PremiumViewModel onRestorePurchase" }

        // Consume old purchases
        oldPurchaseList.filter {
            it.type.calculatePremiumEnd(it.date).isPassed()
        }.forEach {
            sendEffect { PremiumEffect.ConsumePurchase(it.purchaseToken) }
        }

        // Restore purchase if not already activated
        oldPurchaseList.filter {
            it.type.calculatePremiumEnd(it.date).isNotPassed()
        }.maxByOrNull {
            it.type.calculatePremiumEnd(it.date)
        }?.whether(
            { it.date > appStorage.premiumEndDate },
            { PremiumType.getPurchaseIds().any { id -> id == it.type.data.id } }
        )?.let {
            onPremiumActivated(
                adType = PremiumType.getById(it.type.data.id),
                startDate = it.date,
                isRestorePurchase = true
            )
        }

        setState { copy(loading = false) }
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
            setState { copy(premiumTypes = tempList) }
        }.also {
            setState { copy(loading = false) } // in case list is empty, loading will be false
        }
    }

    override fun onPremiumItemClick(type: PremiumType) {
        Logger.d { "PremiumViewModel onPremiumItemClick ${type.data.duration}" }
        setState {
            copy(loading = type != PremiumType.VIDEO)
        }
        sendEffect { PremiumEffect.LaunchActivatePremiumFlow(type) }
    }

    override fun onPremiumActivationFailed() {
        Logger.d { "PremiumViewModel onPremiumActivationFailed" }
        setState { copy(loading = false) }
    }
    // endregion
}
