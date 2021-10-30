/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ccc.android.ui.main

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import co.touchlab.kermit.Logger
import com.github.mustafaozhan.ad.AdManager
import com.github.mustafaozhan.basemob.activity.BaseActivity
import com.github.mustafaozhan.ccc.android.util.updateBaseContextLocale
import com.github.mustafaozhan.ccc.client.viewmodel.main.MainEffect
import com.github.mustafaozhan.ccc.client.viewmodel.main.MainViewModel
import com.google.android.play.core.review.ReviewManagerFactory
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import mustafaozhan.github.com.mycurrencies.R
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : BaseActivity() {

    private val adManager: AdManager by inject()
    private val mainViewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Logger.i { "MainActivity onCreate" }
        installSplashScreen()
        AppCompatDelegate.setDefaultNightMode(mainViewModel.getAppTheme())
        setContentView(R.layout.activity_main)
        checkDestination()
        observeEffects()
        mainViewModel.checkReview()
    }

    private fun observeEffects() = mainViewModel.effect
        .flowWithLifecycle(lifecycle)
        .onEach { viewEffect ->
            Logger.i { "MainActivity observeEffects ${viewEffect::class.simpleName}" }
            when (viewEffect) {
                is MainEffect.ShowInterstitialAd -> adManager.showInterstitialAd(
                    this@MainActivity,
                    getString(R.string.android_interstitial_ad_id)
                )
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

    override fun onResume() {
        super.onResume()
        Logger.i { "MainActivity onResume" }
        mainViewModel.event.onResume()
    }

    override fun onPause() {
        Logger.i { "MainActivity onPause" }
        mainViewModel.event.onPause()
        super.onPause()
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(updateBaseContextLocale(base))
    }
}
