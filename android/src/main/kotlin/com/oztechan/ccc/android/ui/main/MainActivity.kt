/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */
package com.oztechan.ccc.android.ui.main

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import co.touchlab.kermit.Logger
import com.github.submob.basemob.activity.BaseActivity
import com.oztechan.ccc.ad.AdManager
import com.oztechan.ccc.android.util.getThemeMode
import com.oztechan.ccc.android.util.requestAppReview
import com.oztechan.ccc.android.util.showDialog
import com.oztechan.ccc.android.util.updateBaseContextLocale
import com.oztechan.ccc.client.viewmodel.main.MainEffect
import com.oztechan.ccc.client.viewmodel.main.MainViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import mustafaozhan.github.com.mycurrencies.R
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : BaseActivity() {

    private val adManager: AdManager by inject()
    private val mainViewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        Logger.i { "MainActivity onCreate" }
        AppCompatDelegate.setDefaultNightMode(getThemeMode(mainViewModel.getAppTheme()))
        setContentView(R.layout.activity_main)
        checkDestination()
        observeEffects()
    }

    private fun observeEffects() = mainViewModel.effect
        .flowWithLifecycle(lifecycle)
        .onEach { viewEffect ->
            Logger.i { "MainActivity observeEffects ${viewEffect::class.simpleName}" }
            when (viewEffect) {
                MainEffect.ShowInterstitialAd -> adManager.showInterstitialAd(
                    this@MainActivity,
                    getString(R.string.android_interstitial_ad_id)
                )
                MainEffect.RequestReview -> requestAppReview(this)
                is MainEffect.AppUpdateEffect -> showAppUpdateDialog(viewEffect.isCancelable, viewEffect.marketLink)
            }
        }.launchIn(lifecycleScope)

    private fun showAppUpdateDialog(isCancelable: Boolean, marketLink: String) = showDialog(
        title = R.string.txt_update_dialog_title,
        message = R.string.txt_update_dialog_description,
        positiveButton = R.string.update,
        cancelable = isCancelable
    ) {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(marketLink)))
    }

    private fun checkDestination() = with(getNavigationController()) {
        graph = navInflater.inflate(R.navigation.main_graph).apply {
            setStartDestination(
                if (mainViewModel.isFistRun()) {
                    R.id.sliderFragment
                } else {
                    R.id.calculatorFragment
                }
            )
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
