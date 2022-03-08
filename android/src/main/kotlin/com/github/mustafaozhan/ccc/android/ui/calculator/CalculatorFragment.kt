/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ccc.android.ui.calculator

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import co.touchlab.kermit.Logger
import com.github.mustafaozhan.basemob.fragment.BaseVBFragment
import com.github.mustafaozhan.ccc.android.util.dataState
import com.github.mustafaozhan.ccc.android.util.getImageResourceByName
import com.github.mustafaozhan.ccc.android.util.getNavigationResult
import com.github.mustafaozhan.ccc.android.util.setBackgroundByName
import com.github.mustafaozhan.ccc.android.util.setBannerAd
import com.github.mustafaozhan.ccc.android.util.showLoading
import com.github.mustafaozhan.ccc.android.util.showSnack
import com.github.mustafaozhan.ccc.client.util.toValidList
import com.github.mustafaozhan.ccc.client.viewmodel.calculator.CalculatorEffect
import com.github.mustafaozhan.ccc.client.viewmodel.calculator.CalculatorViewModel
import com.oztechan.ccc.ad.AdManager
import com.oztechan.ccc.analytics.AnalyticsManager
import com.oztechan.ccc.analytics.model.UserProperty
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import mustafaozhan.github.com.mycurrencies.R
import mustafaozhan.github.com.mycurrencies.databinding.FragmentCalculatorBinding
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

@Suppress("TooManyFunctions")
class CalculatorFragment : BaseVBFragment<FragmentCalculatorBinding>() {

    private val analyticsManager: AnalyticsManager by inject()
    private val adManager: AdManager by inject()
    private val calculatorViewModel: CalculatorViewModel by viewModel()

    private val calculatorAdapter: CalculatorAdapter by lazy {
        CalculatorAdapter(calculatorViewModel.event, analyticsManager)
    }

    override fun getViewBinding() = FragmentCalculatorBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Logger.i { "CalculatorFragment onViewCreated" }
        initViews()
        observeNavigationResults()
        observeStates()
        observeEffects()
        setListeners()
    }

    override fun onResume() {
        super.onResume()
        analyticsManager.trackScreen(this::class.simpleName.toString())
    }

    override fun onDestroyView() {
        Logger.i { "CalculatorFragment onDestroyView" }
        binding.adViewContainer.removeAllViews()
        binding.recyclerViewMain.adapter = null
        super.onDestroyView()
    }

    override fun onPause() {
        Logger.i { "CalculatorFragment onPause" }
        trackUserProperties()
        super.onPause()
    }

    private fun trackUserProperties() = with(calculatorViewModel.state.value) {
        analyticsManager.setUserProperty(
            UserProperty.BASE_CURRENCY,
            base
        )

        currencyList.filter { it.isActive }
            .run {
                analyticsManager.setUserProperty(
                    UserProperty.CURRENCY_COUNT,
                    this.count().toString()
                )
                analyticsManager.setUserProperty(
                    UserProperty.ACTIVE_CURRENCIES,
                    this.joinToString(",") { currency -> currency.name }
                )
            }
    }

    private fun observeNavigationResults() = getNavigationResult<String>(CHANGE_BASE_EVENT)
        ?.observe(viewLifecycleOwner) {
            Logger.i { "CalculatorFragment observeNavigationResults $it" }
            calculatorViewModel.event.onBaseChange(it)
        }

    private fun initViews() = with(binding) {
        adViewContainer.setBannerAd(
            adManager = adManager,
            adId = getString(R.string.android_banner_ad_unit_id_calculator),
            shouldShowAd = calculatorViewModel.shouldShowBannerAd()
        )
        recyclerViewMain.adapter = calculatorAdapter
    }

    @SuppressLint("SetTextI18n")
    private fun observeStates() = calculatorViewModel.state
        .flowWithLifecycle(lifecycle)
        .onEach {
            with(it) {
                calculatorAdapter.submitList(currencyList.toValidList(calculatorViewModel.state.value.base))

                binding.txtInput.text = input
                with(binding.layoutBar) {
                    ivBase.setBackgroundByName(base)
                    txtBase.text = if (base.isEmpty()) base else "  $base"
                    txtOutput.text = if (output.isNotEmpty()) " = $output" else ""
                    txtSymbol.text = " $symbol"
                }

                binding.loadingView.showLoading(loading)
                binding.txtAppStatus.dataState(rateState)
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)

    private fun observeEffects() = calculatorViewModel.effect
        .flowWithLifecycle(lifecycle)
        .onEach { viewEffect ->
            Logger.i { "CalculatorFragment observeEffects ${viewEffect::class.simpleName}" }
            when (viewEffect) {
                CalculatorEffect.Error -> showSnack(
                    requireView(),
                    R.string.error_text_unknown
                )
                CalculatorEffect.FewCurrency -> showSnack(
                    requireView(),
                    R.string.choose_at_least_two_currency,
                    R.string.select
                ) {
                    navigate(
                        R.id.calculatorFragment,
                        CalculatorFragmentDirections.actionCalculatorFragmentToCurrenciesFragment()
                    )
                }
                CalculatorEffect.MaximumInput -> showSnack(
                    requireView(),
                    R.string.max_input
                )
                CalculatorEffect.OpenBar -> navigate(
                    R.id.calculatorFragment,
                    CalculatorFragmentDirections.actionCalculatorFragmentToChangeBaseBottomSheet()
                )
                CalculatorEffect.OpenSettings -> navigate(
                    R.id.calculatorFragment,
                    CalculatorFragmentDirections.actionCalculatorFragmentToSettingsFragment()
                )
                is CalculatorEffect.ShowRate -> showSnack(
                    requireView(),
                    viewEffect.text,
                    icon = requireContext().getImageResourceByName(viewEffect.name)
                )
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)

    private fun setListeners() = with(binding) {
        with(calculatorViewModel.event) {
            btnSettings.setOnClickListener { onSettingsClicked() }
            layoutBar.root.setOnClickListener { onBarClick() }

            with(layoutKeyboard) {
                one.setKeyboardListener()
                two.setKeyboardListener()
                three.setKeyboardListener()
                four.setKeyboardListener()
                five.setKeyboardListener()
                six.setKeyboardListener()
                seven.setKeyboardListener()
                eight.setKeyboardListener()
                nine.setKeyboardListener()
                zero.setKeyboardListener()
                tripleZero.setKeyboardListener()
                multiply.setKeyboardListener()
                divide.setKeyboardListener()
                minus.setKeyboardListener()
                plus.setKeyboardListener()
                percent.setKeyboardListener()
                dot.setKeyboardListener()
                openParentheses.setKeyboardListener()
                closeParentheses.setKeyboardListener()
                ac.setKeyboardListener()
                del.setKeyboardListener()
            }
        }
    }

    private fun Button.setKeyboardListener() = setOnClickListener {
        calculatorViewModel.event.onKeyPress(text.toString())
    }

    companion object {
        const val CHANGE_BASE_EVENT = "change_base"
    }
}
