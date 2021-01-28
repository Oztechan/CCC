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
        private const val AD_INITIAL_DELAY: Long = 60000
        private const val AD_PERIOD: Long = 180000
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

    private fun setupInterstitialAd() {
        data.adVisibility = true

        data.adJob = clientScope.launch {
            delay(AD_INITIAL_DELAY)

            while (isActive) {
                if (data.adVisibility && settingsRepository.adFreeActivatedDate.isRewardExpired()) {
                    _effect.send(MainEffect.ShowInterstitialAd)
                } else {
                    _effect.send(MainEffect.PrepareInterstitialAd)
                }

                delay(AD_PERIOD)
            }
        }
    }

    fun setLastReview() {
        settingsRepository.lastReviewRequest = Clock.System.now().toEpochMilliseconds()
    }

    fun isFistRun() = settingsRepository.firstRun

    fun getAppTheme() = settingsRepository.appTheme

    fun checkReview() {
        if (settingsRepository.lastReviewRequest.isWeekPassed()) {
            clientScope.launch {
                delay(REVIEW_DELAY)
                _effect.send(MainEffect.RequestReview)
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
        setupInterstitialAd()
    }
    // endregion
}

// Effect
sealed class MainEffect : BaseEffect() {
    object ShowInterstitialAd : MainEffect()
    object PrepareInterstitialAd : MainEffect()
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
    var adVisibility: Boolean = false
) : BaseData()
