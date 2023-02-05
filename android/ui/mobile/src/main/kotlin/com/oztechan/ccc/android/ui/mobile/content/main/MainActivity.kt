/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */
package com.oztechan.ccc.android.ui.mobile.content.main

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import co.touchlab.kermit.Logger
import com.github.submob.basemob.activity.BaseActivity
import com.oztechan.ccc.android.core.ad.AdManager
import com.oztechan.ccc.android.ui.mobile.R
import com.oztechan.ccc.android.ui.mobile.util.getThemeMode
import com.oztechan.ccc.android.ui.mobile.util.requestAppReview
import com.oztechan.ccc.android.ui.mobile.util.showDialog
import com.oztechan.ccc.android.ui.mobile.util.updateBaseContextLocale
import com.oztechan.ccc.client.viewmodel.main.MainEffect
import com.oztechan.ccc.client.viewmodel.main.MainViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : BaseActivity() {

    private val adManager: AdManager by inject()
    private val mainViewModel: MainViewModel by viewModel()

    init {
        // use dark mode default for old devices
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } // else is in on create since viewModel needs to be injected
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        Logger.i { "MainActivity onCreate" }

        // if dark mode is supported use theming according to user preference
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            AppCompatDelegate.setDefaultNightMode(getThemeMode(mainViewModel.getAppTheme()))
        }

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
