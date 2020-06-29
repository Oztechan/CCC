/*
 Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ui.main.settings

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.github.mustafaozhan.basemob.util.Toast.show
import com.github.mustafaozhan.basemob.util.reObserve
import com.github.mustafaozhan.basemob.util.setNavigationResult
import com.github.mustafaozhan.basemob.view.fragment.BaseDBFragment
import com.github.mustafaozhan.ui.R
import com.github.mustafaozhan.ui.databinding.FragmentSettingsBinding
import com.github.mustafaozhan.ui.main.MainData.Companion.KEY_BASE_CURRENCY
import com.github.mustafaozhan.ui.main.settings.SettingsData.Companion.SPAN_LANDSCAPE
import com.github.mustafaozhan.ui.main.settings.SettingsData.Companion.SPAN_VERTICAL
import javax.inject.Inject

class SettingsFragment : BaseDBFragment<FragmentSettingsBinding>() {

    @Inject
    lateinit var settingsViewModel: SettingsViewModel

    private lateinit var settingsAdapter: SettingsAdapter

    override fun bind(container: ViewGroup?): FragmentSettingsBinding =
        FragmentSettingsBinding.inflate(layoutInflater, container, false)

    override fun onBinding(dataBinding: FragmentSettingsBinding) {
        binding.vm = settingsViewModel
        settingsViewModel.getEvent().let {
            binding.event = it
            settingsAdapter = SettingsAdapter(it)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getBaseActivity()?.setSupportActionBar(binding.toolbarFragmentSettings)
        initView()
        observeEffect()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        binding.recyclerViewSettings.layoutManager = GridLayoutManager(
            requireContext(),
            getSpanSize(newConfig.orientation)
        )
    }

    private fun getSpanSize(orientation: Int) = when (orientation) {
        Configuration.ORIENTATION_LANDSCAPE -> SPAN_LANDSCAPE
        else -> SPAN_VERTICAL
    }

    private fun initView() {
        with(binding.recyclerViewSettings) {
            layoutManager = GridLayoutManager(
                requireContext(),
                getSpanSize(resources.configuration.orientation)
            )
            setHasFixedSize(true)
            adapter = settingsAdapter
        }

        settingsViewModel.state.currencyList.reObserve(viewLifecycleOwner, Observer {
            settingsAdapter.submitList(it)
        })
    }

    private fun observeEffect() = settingsViewModel.effect
        .reObserve(viewLifecycleOwner, Observer { viewEffect ->
            when (viewEffect) {
                FewCurrencyEffect -> show(requireContext(), R.string.choose_at_least_two_currency)
                CalculatorEffect -> navigate(
                    R.id.settingsFragment,
                    SettingsFragmentDirections.actionSettingsFragmentToCalculatorFragment()
                )
                is ChangeBaseNavResultEffect -> {
                    setNavigationResult(viewEffect.newBase, KEY_BASE_CURRENCY)
                }
            }
        })
}
