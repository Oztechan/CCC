/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ccc.android.ui

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import com.github.mustafaozhan.basemob.activity.BaseActivity
import com.github.mustafaozhan.ccc.android.util.updateBaseContextLocale
import com.github.mustafaozhan.ccc.client.log.kermit
import com.github.mustafaozhan.ccc.client.viewmodel.MainEffect
import com.github.mustafaozhan.ccc.client.viewmodel.MainViewModel
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.play.core.review.ReviewManagerFactory
import kotlinx.coroutines.flow.collect
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

    private fun observeEffect() = lifecycleScope.launchWhenStarted {
        mainViewModel.effect.collect { viewEffect ->
            kermit.d { "MainActivity observeEffect ${viewEffect::class.simpleName}" }
            when (viewEffect) {
                is MainEffect.ShowInterstitialAd -> showInterstitialAd()
                is MainEffect.RequestReview -> requestReview()
            }
        }
    }

    private fun requestReview() {
        ReviewManagerFactory.create(this@MainActivity)
            .apply {
                requestReviewFlow().addOnCompleteListener { request ->
                    if (request.isSuccessful) {
                        launchReviewFlow(this@MainActivity, request.result)
                    }
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
        this,
        getString(R.string.interstitial_ad_id),
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
        mainViewModel.onResume()
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
