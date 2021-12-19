package com.github.mustafaozhan.ccc.client.viewmodel.main

import com.github.mustafaozhan.ccc.client.base.BaseData
import com.github.mustafaozhan.ccc.client.base.BaseEffect
import com.github.mustafaozhan.ccc.client.base.BaseEvent
import kotlinx.coroutines.Job

// State
sealed class MainEffect : BaseEffect() {
    object ShowInterstitialAd : MainEffect()
    object RequestReview : MainEffect()
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
    var isInitialAd: Boolean = true
) : BaseData() {
    val adDelay: Long
        get() = if (isInitialAd) {
            AD_DELAY_INITIAL
        } else {
            AD_DELAY_NORMAL
        }

    companion object {
        internal const val AD_DELAY_INITIAL: Long = 60000
        internal const val AD_DELAY_NORMAL: Long = 180000
        internal const val REVIEW_DELAY: Long = 20000
    }
}
