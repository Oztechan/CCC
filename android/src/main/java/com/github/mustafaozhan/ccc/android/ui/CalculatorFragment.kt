/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ccc.android.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DiffUtil
import com.github.mustafaozhan.basemob.adapter.BaseDBRecyclerViewAdapter
import com.github.mustafaozhan.basemob.fragment.BaseDBFragment
import com.github.mustafaozhan.ccc.android.util.Toast
import com.github.mustafaozhan.ccc.android.util.getImageResourceByName
import com.github.mustafaozhan.ccc.android.util.getNavigationResult
import com.github.mustafaozhan.ccc.android.util.reObserve
import com.github.mustafaozhan.ccc.android.util.setAdaptiveBannerAd
import com.github.mustafaozhan.ccc.android.util.showSnack
import com.github.mustafaozhan.ccc.client.ui.calculator.CalculatorEvent
import com.github.mustafaozhan.ccc.client.ui.calculator.CalculatorViewModel
import com.github.mustafaozhan.ccc.client.ui.calculator.ErrorEffect
import com.github.mustafaozhan.ccc.client.ui.calculator.FewCurrencyEffect
import com.github.mustafaozhan.ccc.client.ui.calculator.MaximumInputEffect
import com.github.mustafaozhan.ccc.client.ui.calculator.OpenBarEffect
import com.github.mustafaozhan.ccc.client.ui.calculator.OpenSettingsEffect
import com.github.mustafaozhan.ccc.client.ui.calculator.ShowRateEffect
import com.github.mustafaozhan.ccc.client.util.KEY_BASE_CURRENCY
import com.github.mustafaozhan.ccc.client.util.toValidList
import com.github.mustafaozhan.ccc.common.model.Currency
import kotlinx.coroutines.flow.collect
import mustafaozhan.github.com.mycurrencies.R
import mustafaozhan.github.com.mycurrencies.databinding.FragmentCalculatorBinding
import mustafaozhan.github.com.mycurrencies.databinding.ItemCalculatorBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class CalculatorFragment : BaseDBFragment<FragmentCalculatorBinding>() {

    private val calculatorViewModel: CalculatorViewModel by viewModel()

    private lateinit var calculatorAdapter: CalculatorAdapter

    override fun bind(container: ViewGroup?): FragmentCalculatorBinding =
        FragmentCalculatorBinding.inflate(layoutInflater, container, false)

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
            calculatorViewModel.isRewardExpired()
        )
    }

    private fun observeNavigationResult() = getNavigationResult<String>(KEY_BASE_CURRENCY)
        ?.reObserve(viewLifecycleOwner, {
            calculatorViewModel.verifyCurrentBase(it)
        })

    private fun observeEffect() = lifecycleScope.launchWhenStarted {
        calculatorViewModel.effect.collect { viewEffect ->
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

    private fun initView() {
        binding.recyclerViewMain.adapter = calculatorAdapter

        with(calculatorViewModel) {
            lifecycleScope.launchWhenStarted {
                state.currencyList.collect {
                    calculatorAdapter.submitList(it, calculatorViewModel.getCurrentBase())
                }
            }
        }
    }
}

class CalculatorAdapter(
    private val calculatorEvent: CalculatorEvent
) : BaseDBRecyclerViewAdapter<Currency, ItemCalculatorBinding>(CalculatorDiffer()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) = CalculatorDBViewHolder(
        ItemCalculatorBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    fun submitList(list: MutableList<Currency>?, currentBase: String) =
        submitList(list.toValidList(currentBase))

    inner class CalculatorDBViewHolder(itemBinding: ItemCalculatorBinding) :
        BaseDBViewHolder<Currency, ItemCalculatorBinding>(itemBinding) {

        override fun onItemBind(item: Currency) = with(itemBinding) {
            this.item = item
            this.event = calculatorEvent
        }
    }

    class CalculatorDiffer : DiffUtil.ItemCallback<Currency>() {
        override fun areItemsTheSame(oldItem: Currency, newItem: Currency) = oldItem == newItem

        override fun areContentsTheSame(oldItem: Currency, newItem: Currency) = false
    }
}
