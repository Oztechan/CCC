/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */
package com.oztechan.ccc.android.ui.main

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import co.touchlab.kermit.Logger
import com.github.submob.basemob.activity.BaseActivity
import com.oztechan.ccc.ad.AdManager
import com.oztechan.ccc.analytics.AnalyticsManager
import com.oztechan.ccc.analytics.model.UserProperty
import com.oztechan.ccc.android.util.getMarketLink
import com.oztechan.ccc.android.util.requestAppReview
import com.oztechan.ccc.android.util.showDialog
import com.oztechan.ccc.android.util.updateAppTheme
import com.oztechan.ccc.android.util.updateBaseContextLocale
import com.oztechan.ccc.client.device
import com.oztechan.ccc.client.model.AppTheme
import com.oztechan.ccc.client.viewmodel.main.MainEffect
import com.oztechan.ccc.client.viewmodel.main.MainViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import mustafaozhan.github.com.mycurrencies.R
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : BaseActivity() {

    private val analyticsManager: AnalyticsManager by inject()
    private val adManager: AdManager by inject()
    private val mainViewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        Logger.i { "MainActivity onCreate" }
        updateAppTheme(mainViewModel.getAppTheme())
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
                is MainEffect.AppUpdateEffect -> showAppUpdateDialog(viewEffect.isCancelable)
            }
        }.launchIn(lifecycleScope)

    private fun showAppUpdateDialog(isCancelable: Boolean) = showDialog(
        activity = this,
        title = R.string.txt_update_dialog_title,
        message = R.string.txt_update_dialog_description,
        positiveButton = R.string.update,
        cancelable = isCancelable
    ) {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(getMarketLink())))
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

    private fun setUserProperties() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            analyticsManager.setUserProperty(UserProperty.APP_THEME, AppTheme.SYSTEM_DARK)
        } else {
            AppTheme.getThemeByValue(mainViewModel.getAppTheme())
                ?.typeName
                ?.let { analyticsManager.setUserProperty(UserProperty.APP_THEME, it) }
        }

        analyticsManager.setUserProperty(
            UserProperty.IS_AD_FREE,
            mainViewModel.isAdFree().toString()
        )

        analyticsManager.setUserProperty(
            UserProperty.SESSION_COUNT,
            mainViewModel.getSessionCount().toString()
        )
        analyticsManager.setUserProperty(UserProperty.DEVICE_PLATFORM, device.name)
    }

    override fun onResume() {
        super.onResume()
        Logger.i { "MainActivity onResume" }
        mainViewModel.event.onResume()
    }

    override fun onPause() {
        Logger.i { "MainActivity onPause" }
        setUserProperties()
        mainViewModel.event.onPause()
        super.onPause()
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(updateBaseContextLocale(base))
    }
}
