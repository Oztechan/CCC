package com.oztechan.ccc.client.viewmodel.premium

import com.oztechan.ccc.client.core.shared.util.nowAsLong
import com.oztechan.ccc.client.core.viewmodel.BaseEffect
import com.oztechan.ccc.client.core.viewmodel.BaseEvent
import com.oztechan.ccc.client.core.viewmodel.BaseState
import com.oztechan.ccc.client.viewmodel.premium.model.OldPurchase
import com.oztechan.ccc.client.viewmodel.premium.model.PremiumData
import com.oztechan.ccc.client.viewmodel.premium.model.PremiumType

// State
data class PremiumState(
    val premiumTypes: List<PremiumType> = listOf(PremiumType.VIDEO),
    val loading: Boolean = true
) : BaseState()

// Event
interface PremiumEvent : BaseEvent {
    fun onPremiumActivated(
        adType: PremiumType?,
        startDate: Long = nowAsLong(),
        isRestorePurchase: Boolean = false
    )

    fun onRestorePurchase(oldPurchaseList: List<OldPurchase>)
    fun onAddPurchaseMethods(premiumDataList: List<PremiumData>)
    fun onPremiumItemClick(type: PremiumType)
    fun onPremiumActivationFailed()
}

// Effect
sealed class PremiumEffect : BaseEffect() {
    data class LaunchActivatePremiumFlow(
        val premiumType: PremiumType
    ) : PremiumEffect()

    data class PremiumActivated(
        val premiumType: PremiumType,
        val isRestorePurchase: Boolean
    ) : PremiumEffect()
}
