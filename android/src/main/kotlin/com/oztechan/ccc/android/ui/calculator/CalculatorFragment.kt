/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */
package com.oztechan.ccc.android.ui.calculator

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import co.touchlab.kermit.Logger
import com.github.submob.basemob.fragment.BaseVBFragment
import com.oztechan.ccc.ad.AdManager
import com.oztechan.ccc.analytics.AnalyticsManager
import com.oztechan.ccc.analytics.model.ScreenName
import com.oztechan.ccc.android.R
import com.oztechan.ccc.android.databinding.FragmentCalculatorBinding
import com.oztechan.ccc.android.util.copyToClipBoard
import com.oztechan.ccc.android.util.dataState
import com.oztechan.ccc.android.util.destroyBanner
import com.oztechan.ccc.android.util.getNavigationResult
import com.oztechan.ccc.android.util.setBackgroundByName
import com.oztechan.ccc.android.util.setBannerAd
import com.oztechan.ccc.android.util.showSnack
import com.oztechan.ccc.android.util.visibleIf
import com.oztechan.ccc.client.util.toValidList
import com.oztechan.ccc.client.viewmodel.calculator.CalculatorEffect
import com.oztechan.ccc.client.viewmodel.calculator.CalculatorViewModel
import com.oztechan.ccc.res.getImageIdByName
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

@Suppress("TooManyFunctions")
class CalculatorFragment : BaseVBFragment<FragmentCalculatorBinding>() {

    private val analyticsManager: AnalyticsManager by inject()
    private val adManager: AdManager by inject()
    private val calculatorViewModel: CalculatorViewModel by viewModel()

    private val calculatorAdapter: CalculatorAdapter by lazy {
        CalculatorAdapter(calculatorViewModel.event)
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
        analyticsManager.trackScreen(ScreenName.Calculator)
    }

    override fun onDestroyView() {
        Logger.i { "CalculatorFragment onDestroyView" }
        binding.adViewContainer.destroyBanner()
        binding.recyclerViewMain.adapter = null
        super.onDestroyView()
    }

    private fun observeNavigationResults() = getNavigationResult<String>(
        CHANGE_BASE_EVENT,
        R.id.calculatorFragment
    )?.observe(viewLifecycleOwner) {
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

                binding.loadingView.visibleIf(loading, true)
                binding.txtAppStatus.dataState(conversionState)
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)

    private fun observeEffects() = calculatorViewModel.effect
        .flowWithLifecycle(lifecycle)
        .onEach { viewEffect ->
            Logger.i { "CalculatorFragment observeEffects ${viewEffect::class.simpleName}" }
            when (viewEffect) {
                CalculatorEffect.Error -> view?.showSnack(R.string.error_text_unknown)
                CalculatorEffect.FewCurrency -> view?.showSnack(
                    R.string.choose_at_least_two_currency,
                    R.string.select
                ) {
                    navigate(
                        R.id.calculatorFragment,
                        CalculatorFragmentDirections.actionCalculatorFragmentToCurrenciesFragment()
                    )
                }

                CalculatorEffect.TooBigNumber -> view?.showSnack(R.string.text_too_big_number)
                CalculatorEffect.OpenBar -> navigate(
                    R.id.calculatorFragment,
                    CalculatorFragmentDirections.actionCalculatorFragmentToSelectCurrencyBottomSheet()
                )

                CalculatorEffect.OpenSettings -> navigate(
                    R.id.calculatorFragment,
                    CalculatorFragmentDirections.actionCalculatorFragmentToSettingsFragment()
                )

                is CalculatorEffect.CopyToClipboard -> view?.copyToClipBoard(viewEffect.amount)
                is CalculatorEffect.ShowConversion -> view?.showSnack(
                    viewEffect.text,
                    icon = viewEffect.code.getImageIdByName()
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
