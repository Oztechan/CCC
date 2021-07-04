/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ccc.android.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DiffUtil
import com.github.mustafaozhan.basemob.adapter.BaseVBRecyclerViewAdapter
import com.github.mustafaozhan.basemob.bottomsheet.BaseVBBottomSheetDialogFragment
import com.github.mustafaozhan.ccc.android.ui.CalculatorFragment.Companion.CHANGE_BASE_EVENT
import com.github.mustafaozhan.ccc.android.util.setBackgroundByName
import com.github.mustafaozhan.ccc.android.util.setNavigationResult
import com.github.mustafaozhan.ccc.android.util.showLoading
import com.github.mustafaozhan.ccc.android.util.visibleIf
import com.github.mustafaozhan.ccc.client.model.Currency
import com.github.mustafaozhan.ccc.client.viewmodel.bar.BarEffect
import com.github.mustafaozhan.ccc.client.viewmodel.bar.BarEvent
import com.github.mustafaozhan.ccc.client.viewmodel.bar.BarViewModel
import com.github.mustafaozhan.logmob.kermit
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import mustafaozhan.github.com.mycurrencies.R
import mustafaozhan.github.com.mycurrencies.databinding.BottomSheetBarBinding
import mustafaozhan.github.com.mycurrencies.databinding.ItemBarBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class BarBottomSheet :
    BaseVBBottomSheetDialogFragment<BottomSheetBarBinding>() {

    private val barViewModel: BarViewModel by viewModel()

    private lateinit var barAdapter: BarAdapter

    override fun getViewBinding() = BottomSheetBarBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        kermit.d { "BarBottomSheet onViewCreated" }
        initViews()
        observeStates()
        observeEffect()
        setListeners()
    }

    override fun onDestroyView() {
        binding.recyclerViewBar.adapter = null
        super.onDestroyView()
    }

    private fun initViews() {
        barAdapter = BarAdapter(barViewModel.event)
        binding.recyclerViewBar.adapter = barAdapter
    }

    private fun observeStates() = barViewModel.state
        .flowWithLifecycle(lifecycle)
        .onEach {
            with(it) {
                barAdapter.submitList(currencyList)

                with(binding) {
                    loadingView.showLoading(loading)

                    recyclerViewBar.visibleIf(enoughCurrency)
                    txtNoEnoughCurrency.visibleIf(!enoughCurrency)
                    btnSelect.visibleIf(!enoughCurrency)
                }
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)

    private fun observeEffect() = barViewModel.effect
        .flowWithLifecycle(lifecycle)
        .onEach { viewEffect ->
            kermit.d { "BarBottomSheet observeEffect ${viewEffect::class.simpleName}" }
            when (viewEffect) {
                is BarEffect.ChangeBase -> {
                    setNavigationResult(
                        R.id.calculatorFragment,
                        viewEffect.newBase,
                        CHANGE_BASE_EVENT
                    )
                    dismissDialog()
                }
                BarEffect.OpenCurrencies -> navigate(
                    R.id.barBottomSheet,
                    BarBottomSheetDirections.actionBarBottomSheetToCurrenciesFragment()
                )
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)

    private fun setListeners() = binding.btnSelect.setOnClickListener {
        barViewModel.event.onSelectClick()
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

    inner class CalculatorVBViewHolder(itemBinding: ItemBarBinding) :
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
