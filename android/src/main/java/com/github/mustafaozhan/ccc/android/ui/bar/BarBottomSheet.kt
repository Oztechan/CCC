/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ccc.android.ui.bar

import android.os.Bundle
import android.view.View
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import co.touchlab.kermit.Logger
import com.github.mustafaozhan.basemob.bottomsheet.BaseVBBottomSheetDialogFragment
import com.github.mustafaozhan.ccc.android.ui.calculator.CalculatorFragment.Companion.CHANGE_BASE_EVENT
import com.github.mustafaozhan.ccc.android.util.setNavigationResult
import com.github.mustafaozhan.ccc.android.util.showLoading
import com.github.mustafaozhan.ccc.android.util.visibleIf
import com.github.mustafaozhan.ccc.client.viewmodel.bar.BarEffect
import com.github.mustafaozhan.ccc.client.viewmodel.bar.BarViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import mustafaozhan.github.com.mycurrencies.R
import mustafaozhan.github.com.mycurrencies.databinding.BottomSheetBarBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class BarBottomSheet :
    BaseVBBottomSheetDialogFragment<BottomSheetBarBinding>() {

    private val barViewModel: BarViewModel by viewModel()

    private lateinit var barAdapter: BarAdapter

    override fun getViewBinding() = BottomSheetBarBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Logger.i { "BarBottomSheet onViewCreated" }
        initViews()
        observeStates()
        observeEffects()
        setListeners()
    }

    override fun onDestroyView() {
        Logger.i { "BarBottomSheet onDestroyView" }
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

    private fun observeEffects() = barViewModel.effect
        .flowWithLifecycle(lifecycle)
        .onEach { viewEffect ->
            Logger.i { "BarBottomSheet observeEffects ${viewEffect::class.simpleName}" }
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
