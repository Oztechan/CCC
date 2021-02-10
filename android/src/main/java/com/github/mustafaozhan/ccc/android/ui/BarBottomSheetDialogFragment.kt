/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ccc.android.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import com.github.mustafaozhan.basemob.adapter.BaseVBRecyclerViewAdapter
import com.github.mustafaozhan.basemob.bottomsheet.BaseVBBottomSheetDialogFragment
import com.github.mustafaozhan.ccc.android.ui.CalculatorFragment.Companion.CHANGE_BASE_EVENT
import com.github.mustafaozhan.ccc.android.util.setBackgroundByName
import com.github.mustafaozhan.ccc.android.util.visibleIf
import com.github.mustafaozhan.ccc.client.log.kermit
import com.github.mustafaozhan.ccc.client.model.Currency
import com.github.mustafaozhan.ccc.client.viewmodel.BarEffect
import com.github.mustafaozhan.ccc.client.viewmodel.BarEvent
import com.github.mustafaozhan.ccc.client.viewmodel.BarViewModel
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
        kermit.d { "BarBottomSheetDialogFragment onViewCreated" }
        initViews()
        observeStates()
        observeEffect()
        setListeners()
    }

    private fun initViews() {
        barAdapter = BarAdapter(barViewModel.event)
        binding.recyclerViewBar.adapter = barAdapter
    }

    private fun observeStates() = lifecycleScope.launchWhenStarted {
        barViewModel.state.collect {
            with(it) {
                barAdapter.submitList(currencyList)

                with(binding) {
                    loadingView.visibleIf(loading)

                    recyclerViewBar.visibleIf(enoughCurrency)
                    txtNoEnoughCurrency.visibleIf(!enoughCurrency)
                    btnSelect.visibleIf(!enoughCurrency)
                }
            }
        }
    }

    private fun observeEffect() = lifecycleScope.launchWhenStarted {
        barViewModel.effect.collect { viewEffect ->
            kermit.d { "BarBottomSheetDialogFragment observeEffect ${viewEffect::class.simpleName}" }
            when (viewEffect) {
                is BarEffect.ChangeBase -> {
                    findNavController().previousBackStackEntry?.savedStateHandle?.set(
                        CHANGE_BASE_EVENT,
                        viewEffect.newBase
                    )
                    dismissDialog()
                }
                BarEffect.OpenCurrencies -> navigate(
                    R.id.barBottomSheetDialogFragment,
                    BarBottomSheetDialogFragmentDirections.actionBarBottomSheetDialogFragmentToCurrenciesFragment()
                )
            }
        }
    }

    private fun setListeners() {
        binding.btnSelect.setOnClickListener {
            barViewModel.event.onSelectClick()
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
