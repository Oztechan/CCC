/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ccc.android.ui.main

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.coroutineScope
import com.github.mustafaozhan.basemob.activity.BaseActivity
import com.github.mustafaozhan.ccc.android.util.showSnack
import com.github.mustafaozhan.ccc.android.util.updateBaseContextLocale
import com.github.mustafaozhan.scopemob.whether
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.google.android.play.core.review.ReviewManagerFactory
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import mustafaozhan.github.com.mycurrencies.R
import org.koin.androidx.viewmodel.ext.android.viewModel

open class MainActivity : BaseActivity() {

    companion object {
        private const val BACK_DELAY: Long = 2000
        private const val AD_INITIAL_DELAY: Long = 60000
        private const val REVIEW_DELAY: Long = 10000
        private const val AD_PERIOD: Long = 180000
    }

    private val mainViewModel: MainViewModel by viewModel()

    private lateinit var interstitialAd: InterstitialAd
    private lateinit var adJob: Job
    private var adVisibility = false
    private var doubleBackToExitPressedOnce = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(mainViewModel.getAppTheme())
        setContentView(R.layout.activity_main)
        checkDestination()
        checkReview()
        prepareInterstitialAd()
    }

    private fun checkDestination() = with(getNavigationController()) {
        if (mainViewModel.isFistRun()) {
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
                    { mainViewModel.isRewardExpired() }
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
        if (mainViewModel.shouldShowReview()) {
            lifecycle.coroutineScope.launch {
                delay(REVIEW_DELAY)

                ReviewManagerFactory.create(this@MainActivity).apply {
                    requestReviewFlow().addOnCompleteListener { request ->
                        if (request.isSuccessful) {
                            launchReviewFlow(this@MainActivity, request.result)
                            mainViewModel.setLastReview()
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
