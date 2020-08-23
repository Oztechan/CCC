/*
 Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ui.main.settings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatDelegate
import com.github.mustafaozhan.basemob.util.Toast
import com.github.mustafaozhan.basemob.util.reObserve
import com.github.mustafaozhan.basemob.util.showDialog
import com.github.mustafaozhan.basemob.util.showSingleChoiceDialog
import com.github.mustafaozhan.basemob.util.toUnit
import com.github.mustafaozhan.basemob.view.fragment.BaseDBFragment
import com.github.mustafaozhan.scopemob.whether
import com.github.mustafaozhan.ui.R
import com.github.mustafaozhan.ui.databinding.FragmentSettingsBinding
import com.github.mustafaozhan.ui.main.MainData
import com.github.mustafaozhan.ui.main.model.AppTheme
import com.github.mustafaozhan.ui.util.setAdaptiveBannerAd
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdCallback
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import javax.inject.Inject

@Suppress("TooManyFunctions")
class SettingsFragment : BaseDBFragment<FragmentSettingsBinding>() {
    @Inject
    lateinit var settingsViewModel: SettingsViewModel

    private lateinit var rewardedAd: RewardedAd

    override fun bind(container: ViewGroup?): FragmentSettingsBinding =
        FragmentSettingsBinding.inflate(layoutInflater, container, false)

    override fun onBinding(dataBinding: FragmentSettingsBinding) {
        binding.vm = settingsViewModel
        binding.event = settingsViewModel.getEvent()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeEffect()
    }

    override fun onResume() {
        super.onResume()
        binding.adViewContainer.setAdaptiveBannerAd(
            getString(R.string.banner_ad_unit_id_settings),
            settingsViewModel.data.isRewardExpired
        )
    }

    private fun observeEffect() = settingsViewModel.effect
        .reObserve(viewLifecycleOwner, { viewEffect ->
            when (viewEffect) {
                BackEffect -> getBaseActivity()?.onBackPressed()
                CurrenciesEffect -> navigate(
                    R.id.settingsFragment,
                    SettingsFragmentDirections.actionCurrenciesFragmentToCurrenciesFragment()
                )
                FeedBackEffect -> sendFeedBack()
                ShareEffect -> share()
                SupportUsEffect -> showDialog(
                    requireActivity(),
                    R.string.support_us,
                    R.string.rate_and_support,
                    R.string.rate
                ) { startIntent(Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.app_market_link)))) }
                OnGitHubEffect -> startIntent(Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.github_url))))
                RemoveAdsEffect -> showDialog(
                    requireActivity(),
                    R.string.remove_ads,
                    R.string.remove_ads_text,
                    R.string.watch
                ) { prepareRewardedAd() }
                ThemeDialogEffect -> changeTheme()
                is ChangeThemeEffect -> AppCompatDelegate.setDefaultNightMode(viewEffect.themeValue)
                SynchronisedEffect -> Toast.show(requireContext(), R.string.txt_synced)
                OnlyOneTimeSyncEffect -> Toast.show(requireContext(), R.string.txt_already_synced)
            }
        })

    private fun changeTheme() {
        AppTheme.getThemeByValue(settingsViewModel.data.appTheme)?.let { currentThemeType ->
            showSingleChoiceDialog(
                requireActivity(),
                getString(R.string.title_dialog_choose_theme),
                AppTheme.values().map { it.typeName }.toTypedArray(),
                currentThemeType.order
            ) { index ->
                AppTheme.getThemeByOrder(index)?.let { settingsViewModel.updateTheme(it) }
            }
        }
    }

    private fun showRewardedAd() = rewardedAd
        .whether { isLoaded }
        ?.show(requireActivity(), object : RewardedAdCallback() {
            override fun onRewardedAdOpened() = Unit
            override fun onRewardedAdClosed() = Unit
            override fun onRewardedAdFailedToShow(errorCode: Int) = context?.let {
                Toast.show(it, R.string.error_text_unknown)
            }.toUnit()

            override fun onUserEarnedReward(@NonNull reward: RewardItem) {
                settingsViewModel.updateAddFreeDate()
                activity?.run {
                    finish()
                    startActivity(intent)
                }
            }
        }).toUnit()

    private fun prepareRewardedAd() {
        rewardedAd = RewardedAd(requireContext(), getString(R.string.rewarded_ad_unit_id))
        rewardedAd.loadAd(AdRequest.Builder().build(), object : RewardedAdLoadCallback() {
            override fun onRewardedAdLoaded() = showRewardedAd()
            override fun onRewardedAdFailedToLoad(errorCode: Int) = context?.let {
                Toast.show(it, R.string.error_text_unknown)
            }.toUnit()
        })
    }

    private fun startIntent(intent: Intent) {
        getBaseActivity()?.packageManager?.let {
            intent.resolveActivity(it)?.let { startActivity(intent) }
        }
    }

    private fun share() = Intent(Intent.ACTION_SEND).apply {
        type = MainData.TEXT_TYPE
        putExtra(Intent.EXTRA_TEXT, getString(R.string.app_market_link))
        startActivity(Intent.createChooser(this, getString(R.string.settings_item_share_title)))
    }.toUnit()

    private fun sendFeedBack() = Intent(Intent.ACTION_SEND).apply {
        type = MainData.TEXT_EMAIL_TYPE
        putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.mail_developer)))
        putExtra(Intent.EXTRA_SUBJECT, getString(R.string.mail_feedback_subject))
        putExtra(Intent.EXTRA_TEXT, getString(R.string.mail_extra_text) + "")
        startActivity(Intent.createChooser(this, getString(R.string.mail_intent_title)))
    }.toUnit()
}
