/*
 Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ui.main

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.coroutineScope
import com.github.mustafaozhan.basemob.view.activity.BaseActivity
import com.github.mustafaozhan.scopemob.whether
import com.github.mustafaozhan.ui.R
import com.github.mustafaozhan.ui.main.MainData.Companion.AD_INITIAL_DELAY
import com.github.mustafaozhan.ui.main.MainData.Companion.AD_PERIOD
import com.github.mustafaozhan.ui.main.MainData.Companion.BACK_DELAY
import com.github.mustafaozhan.ui.main.MainData.Companion.REVIEW_DELAY
import com.github.mustafaozhan.ui.util.showSnack
import com.github.mustafaozhan.ui.util.updateBaseContextLocale
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.google.android.play.core.review.ReviewManagerFactory
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

open class MainActivity : BaseActivity() {

    @Inject
    lateinit var mainViewModel: MainViewModel

    private lateinit var interstitialAd: InterstitialAd
    private lateinit var adJob: Job
    private var adVisibility = false
    private var doubleBackToExitPressedOnce = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(mainViewModel.data.appTheme)
        setContentView(R.layout.activity_main)
        checkDestination()
        checkReview()
        prepareInterstitialAd()
    }

    private fun checkDestination() = with(getNavigationController()) {
        if (mainViewModel.data.firstRun) {
            graph = navInflater.inflate(R.navigation.main_graph)
                .apply {
                    startDestination = R.id.currenciesFragment
                }
        }
    }

    private fun prepareInterstitialAd() {
        interstitialAd = InterstitialAd(this)
        interstitialAd.adUnitId = getString(R.string.interstitial_ad_id)
        interstitialAd.loadAd(AdRequest.Builder().build())
    }

    private fun setupInterstitialAd() {
        adVisibility = true

        adJob = lifecycle.coroutineScope.launch {
            delay(AD_INITIAL_DELAY)

            while (isActive) {
                interstitialAd.whether(
                    { isLoaded },
                    { adVisibility },
                    { mainViewModel.data.isRewardExpired }
                )?.apply { show() }
                    ?: prepareInterstitialAd()
                delay(AD_PERIOD)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        setupInterstitialAd()
    }

    private fun checkReview() {
        if (mainViewModel.data.shouldRequestReview) {
            lifecycle.coroutineScope.launch {
                delay(REVIEW_DELAY)

                ReviewManagerFactory.create(this@MainActivity).apply {
                    requestReviewFlow().addOnCompleteListener { request ->
                        if (request.isSuccessful) {
                            launchReviewFlow(this@MainActivity, request.result)
                        }
                    }
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        adJob.cancel()
        adVisibility = false
    }

    override fun onBackPressed() {
        if (getNavigationController().currentDestination?.id == R.id.calculatorFragment) {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed()
                return
            }

            doubleBackToExitPressedOnce = true
            showSnack(findViewById(containerId), R.string.click_back_again_to_exit)

            lifecycle.coroutineScope.launch {
                delay(BACK_DELAY)
                doubleBackToExitPressedOnce = false
            }
        } else {
            super.onBackPressed()
        }
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(updateBaseContextLocale(base))
    }
}
