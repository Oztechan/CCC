/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ccc.android.ui

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.coroutineScope
import com.github.mustafaozhan.basemob.activity.BaseActivity
import com.github.mustafaozhan.ccc.android.util.updateBaseContextLocale
import com.github.mustafaozhan.ccc.client.log.kermit
import com.github.mustafaozhan.ccc.client.viewmodel.MainViewModel
import com.github.mustafaozhan.scopemob.whether
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.play.core.review.ReviewManagerFactory
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import mustafaozhan.github.com.mycurrencies.R
import org.koin.androidx.viewmodel.ext.android.viewModel

open class MainActivity : BaseActivity() {

    companion object {
        private const val AD_INITIAL_DELAY: Long = 60000
        private const val REVIEW_DELAY: Long = 10000
        private const val AD_PERIOD: Long = 180000
    }

    private val mainViewModel: MainViewModel by viewModel()

    private lateinit var interstitialAd: InterstitialAd
    private lateinit var adJob: Job
    private var adVisibility = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        kermit.d { "MainActivity onCreate" }
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

    private fun prepareInterstitialAd() = InterstitialAd.load(
        this,
        getString(R.string.interstitial_ad_id),
        AdRequest.Builder().build(),
        object : InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                kermit.d { "MainActivity onAdFailedToLoad ${adError.message}" }
            }

            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                kermit.d { "MainActivity onAdLoaded" }
                this@MainActivity.interstitialAd = interstitialAd
            }
        })

    private fun setupInterstitialAd() {
        adVisibility = true

        adJob = lifecycle.coroutineScope.launch {
            delay(AD_INITIAL_DELAY)

            while (isActive) {
                interstitialAd.whether(
                    { adVisibility },
                    { mainViewModel.isRewardExpired() }
                )?.apply { show(this@MainActivity) }
                    ?: prepareInterstitialAd()
                delay(AD_PERIOD)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        kermit.d { "MainActivity onResume" }
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
        kermit.d { "MainActivity onPause" }
        adJob.cancel()
        adVisibility = false
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(updateBaseContextLocale(base))
    }
}
