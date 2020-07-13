/*
 Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ui.main.settings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.github.mustafaozhan.basemob.util.reObserve
import com.github.mustafaozhan.basemob.util.showDialog
import com.github.mustafaozhan.basemob.util.toUnit
import com.github.mustafaozhan.basemob.view.fragment.BaseDBFragment
import com.github.mustafaozhan.ui.R
import com.github.mustafaozhan.ui.databinding.FragmentSettingsBinding
import com.github.mustafaozhan.ui.main.MainData
import javax.inject.Inject

class SettingsFragment : BaseDBFragment<FragmentSettingsBinding>() {
    @Inject
    lateinit var settingsViewModel: SettingsViewModel

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

    private fun observeEffect() = settingsViewModel.effect
        .reObserve(viewLifecycleOwner, Observer { viewEffect ->
            when (viewEffect) {
                BackEffect -> getBaseActivity()?.onBackPressed()
                CurrenciesEffect -> navigate(
                    R.id.settingsFragment,
                    SettingsFragmentDirections.actionCurrenciesFragmentToCurrenciesFragment()
                )
                FeedBackEffect -> sendFeedBack()
                SupportUsEffect -> showDialog(
                    requireActivity(),
                    R.string.support_us,
                    R.string.rate_and_support,
                    R.string.rate
                ) {
                    startIntent(Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.app_market_link))))
                }
                OnGitHubEffect -> startIntent(Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.github_url))))
            }
        })

    private fun startIntent(intent: Intent) {
        getBaseActivity()?.packageManager?.let {
            intent.resolveActivity(it)?.let { startActivity(intent) }
        }
    }

    private fun sendFeedBack() = Intent(Intent.ACTION_SEND).apply {
        type = MainData.TEXT_EMAIL_TYPE
        putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.mail_developer)))
        putExtra(Intent.EXTRA_SUBJECT, getString(R.string.mail_feedback_subject))
        putExtra(Intent.EXTRA_TEXT, getString(R.string.mail_extra_text) + "")
        startActivity(Intent.createChooser(this, getString(R.string.mail_intent_title)))
    }.toUnit()
}
