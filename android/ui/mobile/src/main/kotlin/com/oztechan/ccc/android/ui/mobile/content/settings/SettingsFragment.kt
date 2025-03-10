/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */
package com.oztechan.ccc.android.ui.mobile.content.settings

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.net.toUri
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import co.touchlab.kermit.Logger
import com.github.submob.basemob.fragment.BaseVBFragment
import com.oztechan.ccc.android.core.ad.AdManager
import com.oztechan.ccc.android.ui.mobile.BuildConfig
import com.oztechan.ccc.android.ui.mobile.R
import com.oztechan.ccc.android.ui.mobile.content.main.ComposeMainActivity
import com.oztechan.ccc.android.ui.mobile.databinding.FragmentSettingsBinding
import com.oztechan.ccc.android.ui.mobile.util.buildBanner
import com.oztechan.ccc.android.ui.mobile.util.destroyBanner
import com.oztechan.ccc.android.ui.mobile.util.getThemeMode
import com.oztechan.ccc.android.ui.mobile.util.resolveAndStartIntent
import com.oztechan.ccc.android.ui.mobile.util.showDialog
import com.oztechan.ccc.android.ui.mobile.util.showSingleChoiceDialog
import com.oztechan.ccc.android.ui.mobile.util.showSnack
import com.oztechan.ccc.android.ui.mobile.util.visibleIf
import com.oztechan.ccc.client.core.analytics.AnalyticsManager
import com.oztechan.ccc.client.core.analytics.model.ScreenName
import com.oztechan.ccc.client.core.shared.model.AppTheme
import com.oztechan.ccc.client.core.shared.util.MAXIMUM_FLOATING_POINT
import com.oztechan.ccc.client.viewmodel.settings.SettingsEffect
import com.oztechan.ccc.client.viewmodel.settings.SettingsViewModel
import com.oztechan.ccc.client.viewmodel.settings.model.PremiumStatus
import com.oztechan.ccc.client.viewmodel.settings.util.numberToIndex
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

@Suppress("TooManyFunctions")
class SettingsFragment : BaseVBFragment<FragmentSettingsBinding>() {

    private val analyticsManager: AnalyticsManager by inject()
    private val adManager: AdManager by inject()
    private val viewModel: SettingsViewModel by viewModel()

    override fun getViewBinding() = FragmentSettingsBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Logger.i { "SettingsFragment onViewCreated" }
        binding.initViews()
        binding.observeStates()
        binding.setListeners()
        observeEffects()
    }

    override fun onDestroyView() {
        Logger.i { "SettingsFragment onDestroyView" }
        binding.adViewContainer.destroyBanner()
        super.onDestroyView()
    }

    @Suppress("LongMethod")
    private fun FragmentSettingsBinding.initViews() {
        adViewContainer.buildBanner(
            adManager = adManager,
            adId = if (BuildConfig.DEBUG) {
                getString(R.string.banner_ad_unit_id_settings_debug)
            } else {
                getString(R.string.banner_ad_unit_id_settings_release)
            },
            shouldShowAd = viewModel.state.value.isBannerAdVisible
        )

        with(itemCurrencies) {
            imgSettingsItem.setBackgroundResource(R.drawable.ic_currency)
            settingsItemTitle.text = getString(R.string.settings_item_currencies_title)
            settingsItemSubTitle.text = getString(R.string.settings_item_currencies_sub_title)
        }
        with(itemWatchers) {
            root.visibleIf(BuildConfig.DEBUG)
            imgSettingsItem.setBackgroundResource(R.drawable.ic_watchers)
            settingsItemTitle.text = getString(R.string.settings_item_watchers_title)
            settingsItemSubTitle.text = getString(R.string.settings_item_watchers_sub_title)
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
            settingsItemSubTitle.text =
                getString(R.string.settings_item_premium_sub_title_no_ads_and_widget)
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
        with(itemPrivacySettings) {
            root.visibleIf(adManager.isPrivacyOptionsRequired())
            imgSettingsItem.setBackgroundResource(R.drawable.ic_privacy_settings)
            settingsItemTitle.text = getString(R.string.settings_item_privacy_settings_title)
            settingsItemSubTitle.text = getString(R.string.settings_item_privacy_settings_sub_title)
        }
        with(itemVersion) {
            imgSettingsItem.setBackgroundResource(R.drawable.ic_version)
            settingsItemTitle.text = getString(R.string.settings_item_version_title)
            settingsItemSubTitle.text = getString(R.string.settings_item_version_sub_title)
        }
    }

    private fun FragmentSettingsBinding.observeStates() = viewModel.state
        .flowWithLifecycle(lifecycle)
        .onEach {
            with(it) {
                itemCurrencies.settingsItemValue.text = requireContext().getString(
                    R.string.settings_active_item_value,
                    activeCurrencyCount
                )
                itemTheme.settingsItemValue.text = appThemeType.themeName
                itemVersion.settingsItemValue.text = version

                itemDisableAds.settingsItemValue.text = when (val state = it.premiumStatus) {
                    PremiumStatus.NeverActivated -> ""
                    is PremiumStatus.Active -> getString(
                        R.string.settings_item_premium_value_will_expire,
                        state.until
                    )

                    is PremiumStatus.Expired -> getString(
                        R.string.settings_item_premium_value_expired,
                        state.at
                    )
                }

                itemPrecision.settingsItemValue.text = requireContext().getString(
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
    private fun observeEffects() = viewModel.effect
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
                    requireContext().resolveAndStartIntent(
                        Intent(
                            Intent.ACTION_VIEW,
                            viewEffect.marketLink.toUri()
                        )
                    )
                }

                SettingsEffect.PrivacySettings -> adManager.showConsentForm(requireActivity())

                SettingsEffect.OnGitHub -> requireContext().resolveAndStartIntent(
                    Intent(
                        Intent.ACTION_VIEW,
                        getString(R.string.github_url).toUri()
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
                SettingsEffect.OpenWatchers -> startActivity(
                    Intent(
                        context,
                        ComposeMainActivity::class.java
                    )
                )
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)

    private fun FragmentSettingsBinding.setListeners() = with(viewModel.event) {
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
        itemPrivacySettings.root.setOnClickListener { onPrivacySettingsClick() }
        itemPrecision.root.setOnClickListener { onPrecisionClick() }
    }

    override fun onResume() {
        super.onResume()
        analyticsManager.trackScreen(ScreenName.Settings)
        Logger.i { "SettingsFragment onResume" }
    }

    private fun changeTheme() {
        activity?.showSingleChoiceDialog(
            getString(R.string.title_dialog_choose_theme),
            AppTheme.values().map { it.themeName }.toTypedArray(),
            viewModel.state.value.appThemeType.ordinal
        ) { index ->
            AppTheme.getThemeByOrdinal(index)?.let { viewModel.event.onThemeChange(it) }
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
        viewModel.state.value.precision.numberToIndex()
    ) {
        viewModel.event.onPrecisionSelect(it)
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
