package com.github.mustafaozhan.ccc.client.viewmodel.main

import com.github.mustafaozhan.ccc.client.base.BaseData
import com.github.mustafaozhan.ccc.client.base.BaseEffect
import com.github.mustafaozhan.ccc.client.base.BaseEvent
import kotlinx.coroutines.Job

// State
sealed class MainEffect : BaseEffect() {
    object ShowInterstitialAd : MainEffect()
    object RequestReview : MainEffect()
    data class AppUpdateEffect(val isCancelable: Boolean) : MainEffect()
}

// Event
interface MainEvent : BaseEvent {
    fun onPause()
    fun onResume()
}

// Data
data class MainData(
    var adJob: Job = Job(),
    var adVisibility: Boolean = false,
    var isAppUpdateShown: Boolean = false,
    var isNewSession: Boolean = true
) : BaseData()
