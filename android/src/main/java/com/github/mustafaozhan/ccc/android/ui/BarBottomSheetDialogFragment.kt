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
import com.github.mustafaozhan.basemob.adapter.BaseVBRecyclerViewAdapter
import com.github.mustafaozhan.basemob.bottomsheet.BaseVBBottomSheetDialogFragment
import com.github.mustafaozhan.ccc.android.util.setBackgroundByName
import com.github.mustafaozhan.ccc.android.util.setNavigationResult
import com.github.mustafaozhan.ccc.android.util.visibleIf
import com.github.mustafaozhan.ccc.client.ui.bar.BarEvent
import com.github.mustafaozhan.ccc.client.ui.bar.BarViewModel
import com.github.mustafaozhan.ccc.client.ui.bar.ChangeBaseNavResultEffect
import com.github.mustafaozhan.ccc.client.ui.bar.OpenCurrenciesEffect
import com.github.mustafaozhan.ccc.client.util.KEY_BASE_CURRENCY
import com.github.mustafaozhan.ccc.common.model.Currency
import kotlinx.coroutines.flow.collect
import mustafaozhan.github.com.mycurrencies.R
import mustafaozhan.github.com.mycurrencies.databinding.FragmentBottomSheetBarBinding
import mustafaozhan.github.com.mycurrencies.databinding.ItemBarBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class BarBottomSheetDialogFragment :
    BaseVBBottomSheetDialogFragment<FragmentBottomSheetBarBinding>() {

    private val barViewModel: BarViewModel by viewModel()

    private lateinit var barAdapter: BarAdapter

    override fun bind() {
        binding = FragmentBottomSheetBarBinding.inflate(layoutInflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        observeEffect()
        setListeners()
    }

    private fun observeEffect() = lifecycleScope.launchWhenStarted {
        barViewModel.effect.collect { viewEffect ->
            when (viewEffect) {
                is ChangeBaseNavResultEffect -> {
                    setNavigationResult(
                        R.id.calculatorFragment,
                        viewEffect.newBase,
                        KEY_BASE_CURRENCY
                    )
                    dismissDialog()
                }
                OpenCurrenciesEffect -> navigate(
                    R.id.barBottomSheetDialogFragment,
                    BarBottomSheetDialogFragmentDirections.actionBarBottomSheetDialogFragmentToCurrenciesFragment(),
                    dismiss = false
                )
            }
        }
    }

    private fun initView() {
        barAdapter = BarAdapter(barViewModel.getEvent())
        binding.recyclerViewBar.adapter = barAdapter

        with(barViewModel.state) {
            lifecycleScope.launchWhenStarted {
                currencyList.collect {
                    barAdapter.submitList(it)
                }
                loading.collect {
                    binding.loadingView.visibleIf(it)
                }
                enoughCurrency.collect {
                    binding.recyclerViewBar.visibleIf(it)
                    binding.txtNoEnoughCurrency.visibleIf(!it)
                    binding.btnSelect.visibleIf(!it)
                }
            }
        }
    }

    private fun setListeners() {
        binding.btnSelect.setOnClickListener {
            barViewModel.getEvent().onSelectClick()
        }
    }
}

class BarAdapter(
    private val barEvent: BarEvent
) : BaseVBRecyclerViewAdapter<Currency, ItemBarBinding>(CalculatorDiffer()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) = CalculatorVBViewHolder(
        ItemBarBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    inner class CalculatorVBViewHolder(private val itemBinding: ItemBarBinding) :
        BaseVBViewHolder<Currency, ItemBarBinding>(itemBinding) {

        override fun onItemBind(item: Currency) = with(itemBinding) {
            imgIcon.setBackgroundByName(item.name)
            txtSettingItem.text = item.getVariablesOneLine()
            root.setOnClickListener { barEvent.onItemClick(item) }
        }
    }

    class CalculatorDiffer : DiffUtil.ItemCallback<Currency>() {
        override fun areItemsTheSame(oldItem: Currency, newItem: Currency) = false

        override fun areContentsTheSame(oldItem: Currency, newItem: Currency) = false
    }
}
