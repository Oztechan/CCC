/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ccc.android.ui.calculator

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.lifecycle.lifecycleScope
import com.github.mustafaozhan.basemob.fragment.BaseVBFragment
import com.github.mustafaozhan.ccc.android.util.Toast
import com.github.mustafaozhan.ccc.android.util.dataState
import com.github.mustafaozhan.ccc.android.util.getImageResourceByName
import com.github.mustafaozhan.ccc.android.util.getNavigationResult
import com.github.mustafaozhan.ccc.android.util.reObserve
import com.github.mustafaozhan.ccc.android.util.setAdaptiveBannerAd
import com.github.mustafaozhan.ccc.android.util.setBackgroundByName
import com.github.mustafaozhan.ccc.android.util.showSnack
import com.github.mustafaozhan.ccc.android.util.visibleIf
import com.github.mustafaozhan.ccc.client.ui.calculator.ErrorEffect
import com.github.mustafaozhan.ccc.client.ui.calculator.FewCurrencyEffect
import com.github.mustafaozhan.ccc.client.ui.calculator.MaximumInputEffect
import com.github.mustafaozhan.ccc.client.ui.calculator.OpenBarEffect
import com.github.mustafaozhan.ccc.client.ui.calculator.OpenSettingsEffect
import com.github.mustafaozhan.ccc.client.ui.calculator.ShowRateEffect
import com.github.mustafaozhan.ccc.client.util.KEY_BASE_CURRENCY
import com.github.mustafaozhan.ccc.common.model.CurrencyType
import kotlinx.coroutines.flow.collect
import mustafaozhan.github.com.mycurrencies.R
import mustafaozhan.github.com.mycurrencies.databinding.FragmentCalculatorBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class CalculatorFragment : BaseVBFragment<FragmentCalculatorBinding>() {

    private val vm: CalculatorViewModel by viewModel()

    private lateinit var calculatorAdapter: CalculatorAdapter

    override fun bind() {
        binding = FragmentCalculatorBinding.inflate(layoutInflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        observeStates()
        observeEffect()
        setListeners()
        observeNavigationResult()
    }

    private fun initViews() = with(binding) {
        calculatorAdapter = CalculatorAdapter(vm.useCase.getEvent())
        recyclerViewMain.adapter = calculatorAdapter
    }

    @SuppressLint("SetTextI18n")
    private fun observeStates() = with(vm.useCase.state) {
        lifecycleScope.launchWhenStarted {
            currencyList.collect {
                calculatorAdapter.submitList(it, vm.useCase.getCurrentBase())
            }
        }
        lifecycleScope.launchWhenStarted {
            input.collect {
                binding.txtInput.text = it
            }
        }
        lifecycleScope.launchWhenStarted {
            base.collect {
                with(binding.layoutBar) {
                    ivBase.setBackgroundByName(it)
                    txtBase.text = if (it == CurrencyType.NULL.toString()) "" else "  $it"
                }
            }
        }
        lifecycleScope.launchWhenStarted {
            output.collect {
                binding.layoutBar.txtOutput.text = if (it.isNotEmpty()) " = $it" else ""
            }
        }
        lifecycleScope.launchWhenStarted {
            symbol.collect {
                binding.layoutBar.txtSymbol.text = " $it"
            }
        }
        lifecycleScope.launchWhenStarted {
            loading.collect {
                binding.loadingView.visibleIf(it)
            }
        }
        lifecycleScope.launchWhenStarted {
            dataState.collect {
                binding.txtAppStatus.dataState(it)
            }
        }
    }

    private fun observeEffect() = lifecycleScope.launchWhenStarted {
        vm.useCase.effect.collect { viewEffect ->
            when (viewEffect) {
                ErrorEffect -> Toast.show(requireContext(), R.string.error_text_unknown)
                FewCurrencyEffect -> showSnack(
                    requireView(),
                    R.string.choose_at_least_two_currency,
                    R.string.select
                ) {
                    navigate(
                        R.id.calculatorFragment,
                        CalculatorFragmentDirections.actionCalculatorFragmentToCurrenciesFragment()
                    )
                }
                MaximumInputEffect -> Toast.show(requireContext(), R.string.max_input)
                OpenBarEffect -> navigate(
                    R.id.calculatorFragment,
                    CalculatorFragmentDirections.actionCalculatorFragmentToBarBottomSheetDialogFragment()
                )
                OpenSettingsEffect -> navigate(
                    R.id.calculatorFragment,
                    CalculatorFragmentDirections.actionCalculatorFragmentToSettingsFragment()
                )
                is ShowRateEffect -> showSnack(
                    requireView(),
                    viewEffect.text,
                    icon = requireContext().getImageResourceByName(viewEffect.name)
                )
            }
        }
    }

    private fun setListeners() = with(binding) {
        with(vm.useCase.getEvent()) {
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

    private fun Button.setKeyboardListener() {
        setOnClickListener {
            vm.useCase.onKeyPress(text.toString())
        }
    }

    private fun observeNavigationResult() = getNavigationResult<String>(KEY_BASE_CURRENCY)
        ?.reObserve(viewLifecycleOwner, {
            vm.useCase.verifyCurrentBase(it)
        })

    override fun onResume() {
        super.onResume()
        binding.adViewContainer.setAdaptiveBannerAd(
            getString(R.string.banner_ad_unit_id_currencies),
            vm.useCase.isRewardExpired()
        )
    }
}
