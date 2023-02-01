package com.oztechan.ccc.client.viewmodel.premium

import com.oztechan.ccc.client.core.shared.model.PremiumType
import com.oztechan.ccc.client.core.viewmodel.BaseEffect
import com.oztechan.ccc.client.core.viewmodel.BaseEvent
import com.oztechan.ccc.client.core.viewmodel.BaseState

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
