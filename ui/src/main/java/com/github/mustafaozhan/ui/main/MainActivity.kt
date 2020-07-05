/*
 Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ui.main

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.annotation.NonNull
import androidx.lifecycle.coroutineScope
import com.github.mustafaozhan.basemob.util.showDialog
import com.github.mustafaozhan.basemob.util.showSnack
import com.github.mustafaozhan.basemob.util.toUnit
import com.github.mustafaozhan.basemob.view.activity.BaseActivity
import com.github.mustafaozhan.scopemob.whether
import com.github.mustafaozhan.ui.R
import com.github.mustafaozhan.ui.main.MainData.Companion.AD_INITIAL_DELAY
import com.github.mustafaozhan.ui.main.MainData.Companion.AD_PERIOD
import com.github.mustafaozhan.ui.main.MainData.Companion.BACK_DELAY
import com.github.mustafaozhan.ui.main.MainData.Companion.TEXT_EMAIL_TYPE
import com.github.mustafaozhan.ui.main.calculator.CalculatorFragmentDirections
import com.github.mustafaozhan.ui.util.updateBaseContextLocale
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdCallback
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

@Suppress("TooManyFunctions")
open class MainActivity : BaseActivity() {

    @Inject
    lateinit var mainViewModel: MainViewModel

    private lateinit var rewardedAd: RewardedAd
    private lateinit var interstitialAd: InterstitialAd
    private lateinit var adJob: Job
    private var adVisibility = false
    private var doubleBackToExitPressedOnce = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        checkDestination()
        prepareRewardedAd()
        prepareInterstitialAd()
    }

    private fun checkDestination() = with(getNavigationController()) {
        if (mainViewModel.data.firstRun) {
            graph = navInflater.inflate(R.navigation.main_graph)
                .apply {
                    startDestination = R.id.settingsFragment
                }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menu?.clear()

        when (getNavigationController().currentDestination?.id) {
            R.id.calculatorFragment -> menuInflater.inflate(R.menu.fragment_calculator_menu, menu)
            R.id.settingsFragment -> menuInflater.inflate(R.menu.fragment_settings_menu, menu)
        }

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.settings -> navigate(CalculatorFragmentDirections.actionCalculatorFragmentToSettingsFragment())
            R.id.feedback -> sendFeedBack()
            R.id.support_us -> {
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(getString(R.string.app_market_link))
                )
                intent.resolveActivity(packageManager)?.let {
                    showDialog(this, R.string.support_us, R.string.rate_and_support, R.string.rate) {
                        startActivity(intent)
                    }
                }
            }
            R.id.removeAds -> showDialog(this, R.string.remove_ads, R.string.remove_ads_text, R.string.watch) {
                showRewardedAd()
            }
            R.id.onGithub -> {
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(getString(R.string.github_url))
                )
                intent.resolveActivity(packageManager)?.let { startActivity(intent) }
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun prepareRewardedAd() {
        rewardedAd = RewardedAd(this, getString(R.string.rewarded_ad_unit_id))
        rewardedAd.loadAd(AdRequest.Builder().build(), object : RewardedAdLoadCallback() {
            override fun onRewardedAdLoaded() = Unit
            override fun onRewardedAdFailedToLoad(errorCode: Int) = Unit
        })
    }

    private fun showRewardedAd() = rewardedAd
        .whether { isLoaded }?.show(this, object : RewardedAdCallback() {
            override fun onRewardedAdOpened() = Unit
            override fun onRewardedAdClosed() = prepareRewardedAd()
            override fun onRewardedAdFailedToShow(errorCode: Int) = prepareRewardedAd()
            override fun onUserEarnedReward(@NonNull reward: RewardItem) {
                mainViewModel.data.updateAdFreeActivation()
                val intent = intent
                finish()
                startActivity(intent)
            }
        })

    private fun sendFeedBack() = Intent(Intent.ACTION_SEND).apply {
        type = TEXT_EMAIL_TYPE
        putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.mail_developer)))
        putExtra(Intent.EXTRA_SUBJECT, getString(R.string.mail_feedback_subject))
        putExtra(Intent.EXTRA_TEXT, getString(R.string.mail_extra_text) + "")
        startActivity(Intent.createChooser(this, getString(R.string.mail_intent_title)))
    }.toUnit()

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
