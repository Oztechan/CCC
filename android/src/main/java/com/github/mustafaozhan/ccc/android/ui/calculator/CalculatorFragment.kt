/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ccc.android.ui.calculator

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import com.github.mustafaozhan.basemob.fragment.BaseDBFragment
import com.github.mustafaozhan.ccc.android.ui.main.MainData.Companion.KEY_BASE_CURRENCY
import com.github.mustafaozhan.ccc.android.util.Toast
import com.github.mustafaozhan.ccc.android.util.getImageResourceByName
import com.github.mustafaozhan.ccc.android.util.getNavigationResult
import com.github.mustafaozhan.ccc.android.util.reObserve
import com.github.mustafaozhan.ccc.android.util.setAdaptiveBannerAd
import com.github.mustafaozhan.ccc.android.util.showSnack
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject
import mustafaozhan.github.com.mycurrencies.R
import mustafaozhan.github.com.mycurrencies.databinding.FragmentCalculatorBinding

class CalculatorFragment : BaseDBFragment<FragmentCalculatorBinding>() {

    @Inject
    lateinit var calculatorViewModel: CalculatorViewModel

    private lateinit var calculatorAdapter: CalculatorAdapter

    override fun bind(container: ViewGroup?): FragmentCalculatorBinding =
        FragmentCalculatorBinding.inflate(layoutInflater, container, false)

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onBinding(dataBinding: FragmentCalculatorBinding) {
        binding.vm = calculatorViewModel
        calculatorViewModel.getEvent().let {
            binding.event = it
            calculatorAdapter = CalculatorAdapter(it)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        observeEffect()
        observeNavigationResult()
    }

    override fun onResume() {
        super.onResume()
        binding.adViewContainer.setAdaptiveBannerAd(
            getString(R.string.banner_ad_unit_id_currencies),
            calculatorViewModel.data.isRewardExpired
        )
    }

    private fun observeNavigationResult() = getNavigationResult<String>(KEY_BASE_CURRENCY)
        ?.reObserve(viewLifecycleOwner, {
            calculatorViewModel.verifyCurrentBase(it)
        })

    private fun observeEffect() = calculatorViewModel.effect
        .reObserve(viewLifecycleOwner, { viewEffect ->
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
        })

    private fun initView() {
        binding.recyclerViewMain.adapter = calculatorAdapter

        with(calculatorViewModel) {
            state.currencyList.reObserve(viewLifecycleOwner, {
                calculatorAdapter.submitList(it, data.currentBase)
            })
        }
    }
}
