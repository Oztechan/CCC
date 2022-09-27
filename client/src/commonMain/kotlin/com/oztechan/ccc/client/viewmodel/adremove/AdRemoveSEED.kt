package com.oztechan.ccc.client.viewmodel.adremove

import com.oztechan.ccc.client.base.BaseEffect
import com.oztechan.ccc.client.base.BaseEvent
import com.oztechan.ccc.client.base.BaseState
import com.oztechan.ccc.client.model.RemoveAdType

// State
data class AdRemoveState(
    val adRemoveTypes: List<RemoveAdType> = listOf(RemoveAdType.VIDEO),
    val loading: Boolean = false
) : BaseState()

// Event
interface AdRemoveEvent : BaseEvent {
    fun onAdRemoveItemClick(type: RemoveAdType)
}

// Effect
sealed class AdRemoveEffect : BaseEffect() {
    data class LaunchRemoveAdFlow(
        val removeAdType: RemoveAdType
    ) : AdRemoveEffect()

    data class AdsRemoved(
        val removeAdType: RemoveAdType,
        val isRestorePurchase: Boolean
    ) : AdRemoveEffect()
}
