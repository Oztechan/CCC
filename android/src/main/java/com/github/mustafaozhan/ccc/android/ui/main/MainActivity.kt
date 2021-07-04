/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ccc.android.ui.main

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.github.mustafaozhan.basemob.activity.BaseActivity
import com.github.mustafaozhan.ccc.android.util.updateBaseContextLocale
import com.github.mustafaozhan.ccc.client.viewmodel.main.MainEffect
import com.github.mustafaozhan.ccc.client.viewmodel.main.MainViewModel
import com.github.mustafaozhan.logmob.kermit
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.play.core.review.ReviewManagerFactory
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import mustafaozhan.github.com.mycurrencies.R
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : BaseActivity() {

    private val mainViewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        kermit.d { "MainActivity onCreate" }
        AppCompatDelegate.setDefaultNightMode(mainViewModel.getAppTheme())
        setContentView(R.layout.activity_main)
        checkDestination()
        observeEffect()
        mainViewModel.checkReview()
    }

    private fun observeEffect() = mainViewModel.effect
        .flowWithLifecycle(lifecycle)
        .onEach { effect ->
            kermit.d { "MainActivity observeEffect ${effect::class.simpleName}" }
            when (effect) {
                is MainEffect.ShowInterstitialAd -> showInterstitialAd()
                is MainEffect.RequestReview -> requestReview()
            }
        }.launchIn(lifecycleScope)

    private fun requestReview() = ReviewManagerFactory.create(this@MainActivity)
        .apply {
            requestReviewFlow().addOnCompleteListener { request ->
                if (request.isSuccessful) {
                    launchReviewFlow(this@MainActivity, request.result)
                }
            }
        }

    private fun checkDestination() = with(getNavigationController()) {
        if (mainViewModel.isFistRun()) {
            graph = navInflater.inflate(R.navigation.main_graph)
                .apply {
                    startDestination = R.id.sliderFragment
                }
        }
    }

    private fun showInterstitialAd() = InterstitialAd.load(
        applicationContext,
        getString(R.string.android_interstitial_ad_id),
        AdRequest.Builder().build(),
        object : InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                kermit.d { "MainActivity onAdFailedToLoad ${adError.message}" }
            }

            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                kermit.d { "MainActivity onAdLoaded" }
                interstitialAd.show(this@MainActivity)
            }
        })

    override fun onResume() {
        super.onResume()
        kermit.d { "MainActivity onResume" }
        mainViewModel.event.onResume()
    }

    override fun onPause() {
        kermit.d { "MainActivity onPause" }
        mainViewModel.event.onPause()
        super.onPause()
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(updateBaseContextLocale(base))
    }
}
