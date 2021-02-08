/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ccc.client.viewmodel

import com.github.mustafaozhan.ccc.client.base.BaseData
import com.github.mustafaozhan.ccc.client.base.BaseEffect
import com.github.mustafaozhan.ccc.client.base.BaseEvent
import com.github.mustafaozhan.ccc.client.base.BaseSEEDViewModel
import com.github.mustafaozhan.ccc.client.base.BaseState
import com.github.mustafaozhan.ccc.client.util.isRewardExpired
import com.github.mustafaozhan.ccc.client.util.isWeekPassed
import com.github.mustafaozhan.ccc.common.data.settings.SettingsRepository
import com.github.mustafaozhan.ccc.common.log.kermit
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock

class MainViewModel(private val settingsRepository: SettingsRepository) : BaseSEEDViewModel(),
    MainEvent {

    companion object {
        private const val AD_DELAY_INITIAL: Long = 60000
        private const val AD_DELAY_NORMAL: Long = 180000
        private const val REVIEW_DELAY: Long = 10000
    }

    // region SEED
    override val state: StateFlow<BaseState>? = null

    private val _effect = Channel<MainEffect>(1)
    override val effect = _effect.receiveAsFlow().conflate()

    override val event = this as MainEvent

    override val data = MainData()
    // endregion

    init {
        kermit.d { "MainViewModel init" }
    }

    private fun setupInterstitialAdTimer() {
        data.adVisibility = true

        data.adJob = clientScope.launch {
            delay(getAdDelay())

            while (isActive && !isFistRun()) {
                if (data.adVisibility && settingsRepository.adFreeActivatedDate.isRewardExpired()) {
                    _effect.send(MainEffect.ShowInterstitialAd)
                    data.isInitialAd = false
                }
                delay(getAdDelay())
            }
        }
    }

    private fun getAdDelay() = if (data.isInitialAd) AD_DELAY_INITIAL else AD_DELAY_NORMAL

    fun isFistRun() = settingsRepository.firstRun

    fun getAppTheme() = settingsRepository.appTheme

    fun checkReview() {
        if (settingsRepository.lastReviewRequest.isWeekPassed()) {
            clientScope.launch {
                delay(REVIEW_DELAY)
                _effect.send(MainEffect.RequestReview)
                settingsRepository.lastReviewRequest = Clock.System.now().toEpochMilliseconds()
            }
        }
    }

    override fun onCleared() {
        kermit.d { "MainViewModel onCleared" }
        super.onCleared()
    }

    // region Event
    override fun onPause() = with(data) {
        kermit.d { "MainViewModel onPause" }
        adJob.cancel()
        adVisibility = false
    }

    override fun onResume() {
        kermit.d { "MainViewModel onResume" }
        setupInterstitialAdTimer()
    }
    // endregion
}

// region SEED
sealed class MainEffect : BaseEffect() {
    object ShowInterstitialAd : MainEffect()
    object RequestReview : MainEffect()
}

interface MainEvent : BaseEvent {
    fun onPause()
    fun onResume()
}

data class MainData(
    var adJob: Job = Job(),
    var adVisibility: Boolean = false,
    var isInitialAd: Boolean = true
) : BaseData()
// endregion
