/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */
package com.oztechan.ccc.android.feature.mobile.ui.settings

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import co.touchlab.kermit.Logger
import com.github.submob.basemob.fragment.BaseVBFragment
import com.oztechan.ccc.android.core.ad.AdManager
import com.oztechan.ccc.android.feature.mobile.BuildConfig
import com.oztechan.ccc.android.feature.mobile.R
import com.oztechan.ccc.android.feature.mobile.databinding.FragmentSettingsBinding
import com.oztechan.ccc.android.feature.mobile.ui.compose.content.ComposeActivity
import com.oztechan.ccc.android.feature.mobile.util.destroyBanner
import com.oztechan.ccc.android.feature.mobile.util.getThemeMode
import com.oztechan.ccc.android.feature.mobile.util.setBannerAd
import com.oztechan.ccc.android.feature.mobile.util.showDialog
import com.oztechan.ccc.android.feature.mobile.util.showSingleChoiceDialog
import com.oztechan.ccc.android.feature.mobile.util.showSnack
import com.oztechan.ccc.android.feature.mobile.util.visibleIf
import com.oztechan.ccc.client.core.analytics.AnalyticsManager
import com.oztechan.ccc.client.core.analytics.model.ScreenName
import com.oztechan.ccc.client.core.shared.util.MAXIMUM_FLOATING_POINT
import com.oztechan.ccc.client.model.AppTheme
import com.oztechan.ccc.client.util.numberToIndex
import com.oztechan.ccc.client.viewmodel.settings.SettingsEffect
import com.oztechan.ccc.client.viewmodel.settings.SettingsViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

@Suppress("TooManyFunctions")
class SettingsFragment : BaseVBFragment<FragmentSettingsBinding>() {

    private val analyticsManager: AnalyticsManager by inject()
    private val adManager: AdManager by inject()
    private val settingsViewModel: SettingsViewModel by viewModel()

    override fun getViewBinding() = FragmentSettingsBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Logger.i { "SettingsFragment onViewCreated" }
        initViews()
        observeStates()
        observeEffects()
        setListeners()
    }

    override fun onDestroyView() {
        Logger.i { "SettingsFragment onDestroyView" }
        binding.adViewContainer.destroyBanner()
        super.onDestroyView()
    }

    @Suppress("LongMethod")
    private fun initViews() = with(binding) {
        adViewContainer.setBannerAd(
            adManager = adManager,
            adId = getString(R.string.android_banner_ad_unit_id_settings),
            shouldShowAd = settingsViewModel.shouldShowBannerAd()
        )
        with(itemCurrencies) {
            imgSettingsItem.setBackgroundResource(R.drawable.ic_currency)
            settingsItemTitle.text = getString(R.string.settings_item_currencies_title)
            settingsItemSubTitle.text = getString(R.string.settings_item_currencies_sub_title)
        }

        if (BuildConfig.DEBUG) {
            with(itemWatchers) {
                imgSettingsItem.setBackgroundResource(R.drawable.ic_watchers)
                settingsItemTitle.text = getString(R.string.settings_item_watchers_title)
                settingsItemSubTitle.text = getString(R.string.settings_item_watchers_sub_title)
            }
        }

        with(itemTheme) {
            root.visibleIf(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
            imgSettingsItem.setBackgroundResource(R.drawable.ic_dark_mode)
            settingsItemTitle.text = getString(R.string.settings_item_theme_title)
            settingsItemSubTitle.text = getString(R.string.settings_item_theme_sub_title)
        }

        with(itemDisableAds) {
            imgSettingsItem.setBackgroundResource(R.drawable.ic_premium)
            settingsItemTitle.text = getString(R.string.settings_item_premium_title)
            settingsItemSubTitle.text = getString(R.string.settings_item_premium_sub_title_no_ads_and_widget)
        }

        with(itemPrecision) {
            imgSettingsItem.setBackgroundResource(R.drawable.ic_precision)
            settingsItemTitle.text = getString(R.string.settings_item_precision_title)
            settingsItemSubTitle.text = getString(R.string.settings_item_precision_sub_title)
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
        with(itemVersion) {
            imgSettingsItem.setBackgroundResource(R.drawable.ic_version)
            settingsItemTitle.text = getString(R.string.settings_item_version_title)
            settingsItemSubTitle.text = getString(R.string.settings_item_version_sub_title)
        }
    }

    private fun observeStates() = settingsViewModel.state
        .flowWithLifecycle(lifecycle)
        .onEach {
            with(it) {
                binding.loadingView.visibleIf(loading)
                binding.itemCurrencies.settingsItemValue.text = requireContext().getString(
                    R.string.settings_active_item_value,
                    activeCurrencyCount
                )
                binding.itemTheme.settingsItemValue.text = appThemeType.themeName
                binding.itemVersion.settingsItemValue.text = version

                binding.itemDisableAds.settingsItemValue.text = if (settingsViewModel.isPremiumEverActivated()) {
                    ""
                } else {
                    if (settingsViewModel.isPremiumExpired()) {
                        getString(R.string.settings_item_premium_value_expired)
                    } else {
                        getString(
                            R.string.settings_item_premium_value_will_expire,
                            premiumEndDate
                        )
                    }
                }

                binding.itemPrecision.settingsItemValue.text = requireContext().getString(
                    if (it.precision == 1) {
                        R.string.settings_item_precision_value
                    } else {
                        R.string.settings_item_precision_value_plural
                    },
                    it.precision
                )
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)

    @Suppress("ComplexMethod")
    private fun observeEffects() = settingsViewModel.effect
        .flowWithLifecycle(lifecycle)
        .onEach { viewEffect ->
            Logger.i { "SettingsFragment observeEffects ${viewEffect::class.simpleName}" }
            when (viewEffect) {
                SettingsEffect.Back -> findNavController().popBackStack()
                SettingsEffect.OpenCurrencies -> navigate(
                    R.id.settingsFragment,
                    SettingsFragmentDirections.actionCurrenciesFragmentToCurrenciesFragment()
                )

                SettingsEffect.FeedBack -> sendFeedBack()
                is SettingsEffect.Share -> share(viewEffect.marketLink)
                is SettingsEffect.SupportUs -> activity?.showDialog(
                    R.string.support_us,
                    R.string.rate_and_support,
                    R.string.rate
                ) {
                    startIntent(Intent(Intent.ACTION_VIEW, Uri.parse(viewEffect.marketLink)))
                }

                SettingsEffect.OnGitHub -> startIntent(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(getString(R.string.github_url))
                    )
                )

                SettingsEffect.Premium -> navigate(
                    R.id.settingsFragment,
                    SettingsFragmentDirections.actionCurrenciesFragmentToPremiumBottomSheet()
                )

                SettingsEffect.ThemeDialog -> changeTheme()
                is SettingsEffect.ChangeTheme -> AppCompatDelegate.setDefaultNightMode(
                    getThemeMode(viewEffect.themeValue)
                )

                SettingsEffect.Synchronising -> view?.showSnack(R.string.txt_synchronising)
                SettingsEffect.Synchronised -> view?.showSnack(R.string.txt_synced)
                SettingsEffect.OnlyOneTimeSync -> view?.showSnack(R.string.txt_already_synced)
                SettingsEffect.AlreadyPremium -> view?.showSnack(R.string.txt_you_already_have_premium)
                SettingsEffect.SelectPrecision -> showPrecisionDialog()
                SettingsEffect.OpenWatchers -> startActivity(Intent(context, ComposeActivity::class.java))
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)

    private fun setListeners() = with(binding) {
        with(settingsViewModel.event) {
            backButton.setOnClickListener { onBackClick() }

            itemCurrencies.root.setOnClickListener { onCurrenciesClick() }
            itemWatchers.root.setOnClickListener { onWatchersClick() }
            itemTheme.root.setOnClickListener { onThemeClick() }
            itemDisableAds.root.setOnClickListener { onPremiumClick() }
            itemSync.root.setOnClickListener { onSyncClick() }
            itemSupportUs.root.setOnClickListener { onSupportUsClick() }
            itemFeedback.root.setOnClickListener { onFeedBackClick() }
            itemShare.root.setOnClickListener { onShareClick() }
            itemOnGithub.root.setOnClickListener { onOnGitHubClick() }
            itemPrecision.root.setOnClickListener { onPrecisionClick() }
        }
    }

    override fun onResume() {
        super.onResume()
        analyticsManager.trackScreen(ScreenName.Settings)
        Logger.i { "SettingsFragment onResume" }
    }

    private fun changeTheme() = AppTheme.getThemeByValue(settingsViewModel.getAppTheme())
        ?.let { currentThemeType ->
            activity?.showSingleChoiceDialog(
                getString(R.string.title_dialog_choose_theme),
                AppTheme.values().map { it.themeName }.toTypedArray(),
                currentThemeType.ordinal
            ) { index ->
                AppTheme.getThemeByOrdinal(index)?.let { settingsViewModel.updateTheme(it) }
            }
        }

    private fun showPrecisionDialog() = activity?.showSingleChoiceDialog(
        R.string.title_dialog_choose_precision,
        (1..MAXIMUM_FLOATING_POINT).map {
            requireContext().getString(
                if (it == 1) R.string.settings_item_precision_value else R.string.settings_item_precision_value_plural,
                it
            )
        }.toTypedArray(),
        settingsViewModel.state.value.precision.numberToIndex()
    ) {
        settingsViewModel.event.onPrecisionSelect(it)
    }

    private fun startIntent(intent: Intent) = getBaseActivity()?.packageManager?.let {
        intent.resolveActivity(it)?.let { startActivity(intent) }
    }

    private fun share(marketLink: String) = Intent(Intent.ACTION_SEND).apply {
        type = TEXT_TYPE
        putExtra(Intent.EXTRA_TEXT, marketLink)
        startActivity(Intent.createChooser(this, getString(R.string.settings_item_share_title)))
    }

    private fun sendFeedBack() = Intent(Intent.ACTION_SEND).apply {
        type = TEXT_EMAIL_TYPE
        putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.mail_developer)))
        putExtra(Intent.EXTRA_SUBJECT, getString(R.string.mail_feedback_subject))
        putExtra(Intent.EXTRA_TEXT, getString(R.string.mail_extra_text) + "")
        startActivity(Intent.createChooser(this, getString(R.string.mail_intent_title)))
    }

    companion object {
        private const val TEXT_EMAIL_TYPE = "text/email"
        private const val TEXT_TYPE = "text/plain"
    }
}
