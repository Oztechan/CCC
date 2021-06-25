/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ccc.android.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.addRepeatingJob
import androidx.recyclerview.widget.DiffUtil
import com.github.mustafaozhan.basemob.adapter.BaseVBRecyclerViewAdapter
import com.github.mustafaozhan.basemob.fragment.BaseVBFragment
import com.github.mustafaozhan.ccc.android.util.dataState
import com.github.mustafaozhan.ccc.android.util.getImageResourceByName
import com.github.mustafaozhan.ccc.android.util.getNavigationResult
import com.github.mustafaozhan.ccc.android.util.setAdaptiveBannerAd
import com.github.mustafaozhan.ccc.android.util.setBackgroundByName
import com.github.mustafaozhan.ccc.android.util.showLoading
import com.github.mustafaozhan.ccc.android.util.showSnack
import com.github.mustafaozhan.ccc.client.model.Currency
import com.github.mustafaozhan.ccc.client.util.getFormatted
import com.github.mustafaozhan.ccc.client.util.toStandardDigits
import com.github.mustafaozhan.ccc.client.util.toValidList
import com.github.mustafaozhan.ccc.client.viewmodel.calculator.CalculatorEffect
import com.github.mustafaozhan.ccc.client.viewmodel.calculator.CalculatorEvent
import com.github.mustafaozhan.ccc.client.viewmodel.calculator.CalculatorViewModel
import com.github.mustafaozhan.logmob.kermit
import kotlinx.coroutines.flow.collect
import mustafaozhan.github.com.mycurrencies.R
import mustafaozhan.github.com.mycurrencies.databinding.FragmentCalculatorBinding
import mustafaozhan.github.com.mycurrencies.databinding.ItemCalculatorBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class CalculatorFragment : BaseVBFragment<FragmentCalculatorBinding>() {

    companion object {
        const val CHANGE_BASE_EVENT = "change_base"
    }

    private val calculatorViewModel: CalculatorViewModel by viewModel()

    private lateinit var calculatorAdapter: CalculatorAdapter

    override fun getViewBinding() = FragmentCalculatorBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        kermit.d { "CalculatorFragment onViewCreated" }
        initViews()
        observeNavigationResults()
        observeStates()
        observeEffect()
        setListeners()
    }

    override fun onDestroyView() {
        binding.adViewContainer.removeAllViews()
        binding.recyclerViewMain.adapter = null
        super.onDestroyView()
    }

    private fun observeNavigationResults() = getNavigationResult<String>(CHANGE_BASE_EVENT)
        ?.observe(viewLifecycleOwner) {
            calculatorViewModel.event.onBaseChange(it)
        }

    private fun initViews() = with(binding) {
        adViewContainer.setAdaptiveBannerAd(
            getString(R.string.android_banner_ad_unit_id_calculator),
            calculatorViewModel.isRewardExpired()
        )
        calculatorAdapter = CalculatorAdapter(calculatorViewModel.event)
        recyclerViewMain.adapter = calculatorAdapter
    }

    @SuppressLint("SetTextI18n")
    private fun observeStates() = viewLifecycleOwner.addRepeatingJob(Lifecycle.State.STARTED) {
        calculatorViewModel.state.collect {
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
        }
    }

    private fun observeEffect() = viewLifecycleOwner.addRepeatingJob(Lifecycle.State.STARTED) {
        calculatorViewModel.effect.collect { viewEffect ->
            kermit.d { "CalculatorFragment observeEffect ${viewEffect::class.simpleName}" }
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
                    CalculatorFragmentDirections.actionCalculatorFragmentToBarBottomSheet()
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
        }
    }

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
        calculatorViewModel.onKeyPress(text.toString())
    }
}

class CalculatorAdapter(
    private val calculatorEvent: CalculatorEvent
) : BaseVBRecyclerViewAdapter<Currency, ItemCalculatorBinding>(CalculatorDiffer()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) = CalculatorVBViewHolder(
        ItemCalculatorBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    inner class CalculatorVBViewHolder(itemBinding: ItemCalculatorBinding) :
        BaseVBViewHolder<Currency, ItemCalculatorBinding>(itemBinding) {

        override fun onItemBind(item: Currency) = with(itemBinding) {
            txtAmount.text = item.rate.getFormatted().toStandardDigits()
            txtSymbol.text = item.symbol
            txtType.text = item.name
            imgItem.setBackgroundByName(item.name)
            root.setOnClickListener { calculatorEvent.onItemClick(item) }
            root.setOnLongClickListener { calculatorEvent.onItemLongClick(item) }
        }
    }

    class CalculatorDiffer : DiffUtil.ItemCallback<Currency>() {
        override fun areItemsTheSame(oldItem: Currency, newItem: Currency) = oldItem == newItem

        override fun areContentsTheSame(oldItem: Currency, newItem: Currency) = false
    }
}
