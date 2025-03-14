/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */
package com.oztechan.ccc.android.ui.mobile.content.main

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.net.toUri
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import co.touchlab.kermit.Logger
import com.github.submob.basemob.activity.BaseActivity
import com.oztechan.ccc.android.core.ad.AdManager
import com.oztechan.ccc.android.ui.mobile.BuildConfig
import com.oztechan.ccc.android.ui.mobile.R
import com.oztechan.ccc.android.ui.mobile.util.getThemeMode
import com.oztechan.ccc.android.ui.mobile.util.isDeviceRooted
import com.oztechan.ccc.android.ui.mobile.util.requestAppReview
import com.oztechan.ccc.android.ui.mobile.util.resolveAndStartIntent
import com.oztechan.ccc.android.ui.mobile.util.showDialog
import com.oztechan.ccc.android.ui.mobile.util.updateBaseContextLocale
import com.oztechan.ccc.client.core.analytics.AnalyticsManager
import com.oztechan.ccc.client.core.analytics.model.UserProperty
import com.oztechan.ccc.client.viewmodel.main.MainEffect
import com.oztechan.ccc.client.viewmodel.main.MainViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : BaseActivity() {
    override var containerId: Int = R.id.content

    private val adManager: AdManager by inject()
    private val viewModel: MainViewModel by viewModel()
    private val analyticsManager by inject<AnalyticsManager>()

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

        setContentView(R.layout.activity_main)

        adManager.initAds(this)
        analyticsManager.setUserProperty(UserProperty.IsRooted(isDeviceRooted(this)))

        observeStates()
        observeEffects()
    }

    private fun observeStates() = viewModel.state
        .flowWithLifecycle(lifecycle)
        .onEach {
            with(it) {
                setDestination(if (shouldOnboardUser) R.id.sliderFragment else R.id.calculatorFragment)

                // if dark mode is supported use theming according to user preference
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    AppCompatDelegate.setDefaultNightMode(getThemeMode(it.appTheme))
                }
            }
        }.launchIn(lifecycleScope)

    private fun observeEffects() = viewModel.effect
        .flowWithLifecycle(lifecycle)
        .onEach { viewEffect ->
            Logger.i { "MainActivity observeEffects ${viewEffect::class.simpleName}" }
            when (viewEffect) {
                MainEffect.ShowInterstitialAd -> adManager.showInterstitialAd(
                    this@MainActivity,
                    if (BuildConfig.DEBUG) {
                        getString(R.string.interstitial_ad_id_debug)
                    } else {
                        getString(R.string.interstitial_ad_id_release)
                    }
                )

                MainEffect.RequestReview -> requestAppReview(this)
                is MainEffect.AppUpdateEffect -> showAppUpdateDialog(
                    viewEffect.isCancelable,
                    viewEffect.marketLink
                )
            }
        }.launchIn(lifecycleScope)

    private fun showAppUpdateDialog(isCancelable: Boolean, marketLink: String) = showDialog(
        title = R.string.txt_update_dialog_title,
        message = R.string.txt_update_dialog_description,
        positiveButton = R.string.update,
        cancelable = isCancelable
    ) {
        resolveAndStartIntent(Intent(Intent.ACTION_VIEW, marketLink.toUri()))
    }

    private fun setDestination(fragmentId: Int) = with(getNavigationController()) {
        graph = navInflater.inflate(R.navigation.main_graph).apply {
            setStartDestination(fragmentId)
        }
    }

    override fun onResume() {
        super.onResume()
        Logger.i { "MainActivity onResume" }
        viewModel.event.onAppForeground()
    }

    override fun onPause() {
        Logger.i { "MainActivity onPause" }
        viewModel.event.onAppBackground()
        super.onPause()
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(updateBaseContextLocale(base))
    }
}
