package com.oztechan.ccc.client.viewmodel.premium

import com.oztechan.ccc.client.base.BaseEffect
import com.oztechan.ccc.client.base.BaseEvent
import com.oztechan.ccc.client.base.BaseState
import com.oztechan.ccc.client.model.PremiumType

// State
data class PremiumState(
    val premiumTypes: List<PremiumType> = listOf(PremiumType.VIDEO),
    val loading: Boolean = false
) : BaseState()

// Event
interface PremiumEvent : BaseEvent {
    fun onPremiumItemClick(type: PremiumType)
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
