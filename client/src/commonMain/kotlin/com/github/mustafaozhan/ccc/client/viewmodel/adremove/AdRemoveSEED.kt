package com.github.mustafaozhan.ccc.client.viewmodel.adremove

import com.github.mustafaozhan.ccc.client.base.BaseEffect
import com.github.mustafaozhan.ccc.client.base.BaseEvent
import com.github.mustafaozhan.ccc.client.base.BaseState
import com.github.mustafaozhan.ccc.client.model.RemoveAdType
import kotlinx.coroutines.flow.MutableStateFlow

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
    data class RemoveAd(val removeAdType: RemoveAdType) : AdRemoveEffect()
    object RestartActivity : AdRemoveEffect()
    object AlreadyAdFree : AdRemoveEffect()
}

// Extension
fun MutableStateFlow<AdRemoveState>.update(
    adRemoveTypes: List<RemoveAdType> = value.adRemoveTypes,
    loading: Boolean = value.loading
) {
    value = value.copy(
        adRemoveTypes = adRemoveTypes,
        loading = loading
    )
}
