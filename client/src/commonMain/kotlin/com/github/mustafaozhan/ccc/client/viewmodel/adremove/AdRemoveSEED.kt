package com.github.mustafaozhan.ccc.client.viewmodel.adremove

import com.github.mustafaozhan.ccc.client.base.BaseEffect
import com.github.mustafaozhan.ccc.client.base.BaseEvent
import com.github.mustafaozhan.ccc.client.base.BaseState
import com.github.mustafaozhan.ccc.client.model.RemoveAdType

// State
data class AdRemoveState(
    val adRemoveTypes: List<RemoveAdType> = listOf(),
    val loading: Boolean = false
) : BaseState() {
    // for ios
    constructor() : this(listOf<RemoveAdType>(), false)
}

// Event
interface AdRemoveEvent : BaseEvent {
    fun onAdRemoveItemClick(type: RemoveAdType)
}

// Effect
sealed class AdRemoveEffect : BaseEffect() {
    data class RemoveAd(val removeAdType: RemoveAdType) : AdRemoveEffect()
    object RestartActivity : AdRemoveEffect()
    object AlreadyAdFree : AdRemoveEffect()
}
