/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */
package com.oztechan.ccc.android.ui.selectcurrency

import android.os.Bundle
import android.view.View
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import co.touchlab.kermit.Logger
import com.github.submob.basemob.bottomsheet.BaseVBBottomSheetDialogFragment
import com.oztechan.ccc.analytics.AnalyticsManager
import com.oztechan.ccc.analytics.model.ScreenName
import com.oztechan.ccc.android.ui.calculator.CalculatorFragment.Companion.CHANGE_BASE_EVENT
import com.oztechan.ccc.android.util.setNavigationResult
import com.oztechan.ccc.android.util.showLoading
import com.oztechan.ccc.android.util.visibleIf
import com.oztechan.ccc.client.viewmodel.selectcurrency.SelectCurrencyEffect
import com.oztechan.ccc.client.viewmodel.selectcurrency.SelectCurrencyViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import mustafaozhan.github.com.mycurrencies.R
import mustafaozhan.github.com.mycurrencies.databinding.BottomSheetSelectCurrencyBinding
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class SelectCurrencyBottomSheet :
    BaseVBBottomSheetDialogFragment<BottomSheetSelectCurrencyBinding>() {

    private val analyticsManager: AnalyticsManager by inject()
    private val selectCurrencyViewModel: SelectCurrencyViewModel by viewModel()

    private val selectCurrencyAdapter: SelectCurrencyAdapter by lazy {
        SelectCurrencyAdapter(selectCurrencyViewModel.event)
    }

    override fun getViewBinding() = BottomSheetSelectCurrencyBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Logger.i { "SelectCurrencyBottomSheet onViewCreated" }
        initViews()
        observeStates()
        observeEffects()
        setListeners()
    }

    override fun onDestroyView() {
        Logger.i { "SelectCurrencyBottomSheet onDestroyView" }
        binding.recyclerViewSelectCurrency.adapter = null
        super.onDestroyView()
    }

    override fun onResume() {
        super.onResume()
        analyticsManager.trackScreen(ScreenName.SelectCurrency)
    }

    private fun initViews() {
        binding.recyclerViewSelectCurrency.adapter = selectCurrencyAdapter
    }

    private fun observeStates() = selectCurrencyViewModel.state
        .flowWithLifecycle(lifecycle)
        .onEach {
            with(it) {
                selectCurrencyAdapter.submitList(currencyList)

                with(binding) {
                    loadingView.showLoading(loading)

                    recyclerViewSelectCurrency.visibleIf(enoughCurrency)
                    txtNoEnoughCurrency.visibleIf(!enoughCurrency)
                    btnSelect.visibleIf(!enoughCurrency)
                }
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)

    private fun observeEffects() = selectCurrencyViewModel.effect
        .flowWithLifecycle(lifecycle)
        .onEach { viewEffect ->
            Logger.i { "SelectCurrencyBottomSheet observeEffects ${viewEffect::class.simpleName}" }
            when (viewEffect) {
                is SelectCurrencyEffect.CurrencyChange -> {
                    setNavigationResult(
                        R.id.calculatorFragment,
                        viewEffect.newBase,
                        CHANGE_BASE_EVENT
                    )
                    dismissDialog()
                }
                SelectCurrencyEffect.OpenCurrencies -> navigate(
                    R.id.selectCurrencyBottomSheet,
                    SelectCurrencyBottomSheetDirections.actionSelectCurrencyBottomSheetToCurrenciesFragment()
                )
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)

    private fun setListeners() = binding.btnSelect.setOnClickListener {
        selectCurrencyViewModel.event.onSelectClick()
    }
}
