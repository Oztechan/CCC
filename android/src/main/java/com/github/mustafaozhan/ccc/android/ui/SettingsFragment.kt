/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ccc.android.ui

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import com.github.mustafaozhan.basemob.fragment.BaseVBFragment
import com.github.mustafaozhan.ccc.android.util.Toast
import com.github.mustafaozhan.ccc.android.util.setAdaptiveBannerAd
import com.github.mustafaozhan.ccc.android.util.showDialog
import com.github.mustafaozhan.ccc.android.util.showSingleChoiceDialog
import com.github.mustafaozhan.ccc.android.util.visibleIf
import com.github.mustafaozhan.ccc.client.model.AppTheme
import com.github.mustafaozhan.ccc.client.ui.settings.BackEffect
import com.github.mustafaozhan.ccc.client.ui.settings.ChangeThemeEffect
import com.github.mustafaozhan.ccc.client.ui.settings.CurrenciesEffect
import com.github.mustafaozhan.ccc.client.ui.settings.FeedBackEffect
import com.github.mustafaozhan.ccc.client.ui.settings.OnGitHubEffect
import com.github.mustafaozhan.ccc.client.ui.settings.OnlyOneTimeSyncEffect
import com.github.mustafaozhan.ccc.client.ui.settings.RemoveAdsEffect
import com.github.mustafaozhan.ccc.client.ui.settings.SettingsViewModel
import com.github.mustafaozhan.ccc.client.ui.settings.ShareEffect
import com.github.mustafaozhan.ccc.client.ui.settings.SupportUsEffect
import com.github.mustafaozhan.ccc.client.ui.settings.SynchronisedEffect
import com.github.mustafaozhan.ccc.client.ui.settings.ThemeDialogEffect
import com.github.mustafaozhan.ccc.client.util.toUnit
import com.github.mustafaozhan.ccc.common.log.kermit
import com.github.mustafaozhan.scopemob.whether
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdCallback
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import kotlinx.coroutines.flow.collect
import mustafaozhan.github.com.mycurrencies.R
import mustafaozhan.github.com.mycurrencies.databinding.FragmentSettingsBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

@Suppress("TooManyFunctions")
class SettingsFragment : BaseVBFragment<FragmentSettingsBinding>() {

    companion object {
        private const val TEXT_EMAIL_TYPE = "text/email"
        private const val TEXT_TYPE = "text/plain"
    }

    private val settingsViewModel: SettingsViewModel by viewModel()

    private lateinit var rewardedAd: RewardedAd

    override fun bind() {
        binding = FragmentSettingsBinding.inflate(layoutInflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        kermit.d { "SettingsFragment onViewCreated" }
        initViews()
        observeStates()
        observeEffect()
        setListeners()
    }

    private fun initViews() = with(binding) {
        with(itemCurrencies) {
            imgSettingsItem.setBackgroundResource(R.drawable.ic_currency)
            settingsItemTitle.text = getString(R.string.settings_item_currencies_title)
            settingsItemSubTitle.text = getString(R.string.settings_item_currencies_sub_title)
        }

        with(itemTheme) {
            root.visibleIf(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
            imgSettingsItem.setBackgroundResource(R.drawable.ic_dark_mode)
            settingsItemTitle.text = getString(R.string.settings_item_theme_title)
            settingsItemSubTitle.text = getString(R.string.settings_item_theme_sub_title)
        }
        with(itemDisableAds) {
            imgSettingsItem.setBackgroundResource(R.drawable.ic_disable_ads)
            settingsItemTitle.text = getString(R.string.settings_item_remove_ads_title)
            settingsItemSubTitle.text = getString(R.string.settings_item_remove_ads_sub_title)
        }
        with(itemSync) {
            imgSettingsItem.setBackgroundResource(R.drawable.ic_sync)
            settingsItemTitle.text = getString(R.string.settings_item_sync_title)
            settingsItemSubTitle.text = getString(R.string.settings_item_sync_sub_title)
        }
        with(itemSupportUs) {
            imgSettingsItem.setBackgroundResource(R.drawable.ic_store)
            settingsItemTitle.text = getString(R.string.settings_item_support_us_title)
            settingsItemSubTitle.text = getString(R.string.settings_item_support_us_sub_title)
        }
        with(itemFeedback) {
            imgSettingsItem.setBackgroundResource(R.drawable.ic_email)
            settingsItemTitle.text = getString(R.string.settings_item_feedback_title)
            settingsItemSubTitle.text = getString(R.string.settings_item_feedback_sub_title)
        }
        with(itemShare) {
            imgSettingsItem.setBackgroundResource(R.drawable.ic_share)
            settingsItemTitle.text = getString(R.string.settings_item_share_title)
            settingsItemSubTitle.text = getString(R.string.settings_item_share_sub_title)
        }
        with(itemOnGithub) {
            imgSettingsItem.setBackgroundResource(R.drawable.ic_on_github)
            settingsItemTitle.text = getString(R.string.settings_item_on_github_title)
            settingsItemSubTitle.text = getString(R.string.settings_item_on_github_sub_title)
        }
    }

    private fun observeStates() = with(settingsViewModel.state) {
        lifecycleScope.launchWhenStarted {
            activeCurrencyCount.collect {
                binding.itemCurrencies.settingsItemValue.text = requireContext().resources
                    .getQuantityString(R.plurals.settings_item_currencies_value, it, it)
            }
        }

        lifecycleScope.launchWhenStarted {
            appThemeType.collect {
                binding.itemTheme.settingsItemValue.text = it.typeName
            }
        }

        lifecycleScope.launchWhenStarted {
            addFreeDate.collect {
                binding.itemDisableAds.settingsItemValue.text =
                    if (settingsViewModel.getAdFreeActivatedDate() == 0.toLong()) "" else {
                        if (settingsViewModel.isRewardExpired()) {
                            getString(R.string.settings_item_remove_ads_value_expired)
                        } else {
                            getString(R.string.settings_item_remove_ads_value_will_expire, it)
                        }
                    }
            }
        }
    }

    private fun observeEffect() = lifecycleScope.launchWhenStarted {
        settingsViewModel.effect.collect { viewEffect ->
            kermit.d { "SettingsFragment observeEffect ${viewEffect::class.simpleName}" }
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
                ) {
                    startIntent(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse(getString(R.string.app_market_link))
                        )
                    )
                }
                OnGitHubEffect -> startIntent(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(getString(R.string.github_url))
                    )
                )
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
        }
    }.toUnit()

    private fun setListeners() = with(binding) {
        with(settingsViewModel.getEvent()) {
            backButton.setOnClickListener { onBackClick() }

            itemCurrencies.root.setOnClickListener { onCurrenciesClick() }
            itemTheme.root.setOnClickListener { onThemeClick() }
            itemDisableAds.root.setOnClickListener { onRemoveAdsClick() }
            itemSync.root.setOnClickListener { onSyncClick() }
            itemSupportUs.root.setOnClickListener { onSupportUsClick() }
            itemFeedback.root.setOnClickListener { onFeedBackClick() }
            itemShare.root.setOnClickListener { onShareClick() }
            itemOnGithub.root.setOnClickListener { onOnGitHubClick() }
        }
    }

    override fun onResume() {
        super.onResume()
        kermit.d { "SettingsFragment onResume" }
        binding.adViewContainer.setAdaptiveBannerAd(
            getString(R.string.banner_ad_unit_id_settings),
            settingsViewModel.isRewardExpired()
        )
    }

    private fun changeTheme() {
        AppTheme.getThemeByValue(settingsViewModel.getAppTheme())?.let { currentThemeType ->
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
            override fun onRewardedAdOpened() = kermit.d { "SettingsFragment onRewardedAdOpened" }

            override fun onRewardedAdClosed() = kermit.d { "SettingsFragment onRewardedAdClosed" }

            override fun onRewardedAdFailedToShow(errorCode: Int) = context?.let {
                kermit.d { "SettingsFragment onRewardedAdFailedToShow" }
                Toast.show(it, R.string.error_text_unknown)
            }.toUnit()

            override fun onUserEarnedReward(@NonNull reward: RewardItem) {
                kermit.d { "SettingsFragment onUserEarnedReward" }
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
            override fun onRewardedAdLoaded() {
                kermit.d { "SettingsFragment onRewardedAdLoaded" }
                showRewardedAd()
            }

            override fun onRewardedAdFailedToLoad(errorCode: Int) = context?.let {
                kermit.d { "SettingsFragment onRewardedAdFailedToLoad" }
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
        type = TEXT_TYPE
        putExtra(Intent.EXTRA_TEXT, getString(R.string.app_market_link))
        startActivity(Intent.createChooser(this, getString(R.string.settings_item_share_title)))
    }.toUnit()

    private fun sendFeedBack() = Intent(Intent.ACTION_SEND).apply {
        type = TEXT_EMAIL_TYPE
        putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.mail_developer)))
        putExtra(Intent.EXTRA_SUBJECT, getString(R.string.mail_feedback_subject))
        putExtra(Intent.EXTRA_TEXT, getString(R.string.mail_extra_text) + "")
        startActivity(Intent.createChooser(this, getString(R.string.mail_intent_title)))
    }.toUnit()
}
