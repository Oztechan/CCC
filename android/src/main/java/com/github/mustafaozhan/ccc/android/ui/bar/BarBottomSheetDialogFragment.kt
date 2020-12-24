/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ccc.android.ui.bar

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.github.mustafaozhan.basemob.bottomsheet.BaseVBBottomSheetDialogFragment
import com.github.mustafaozhan.ccc.android.util.setNavigationResult
import com.github.mustafaozhan.ccc.android.util.visibleIf
import com.github.mustafaozhan.ccc.client.ui.bar.ChangeBaseNavResultEffect
import com.github.mustafaozhan.ccc.client.ui.bar.OpenCurrenciesEffect
import com.github.mustafaozhan.ccc.client.util.KEY_BASE_CURRENCY
import kotlinx.coroutines.flow.collect
import mustafaozhan.github.com.mycurrencies.R
import mustafaozhan.github.com.mycurrencies.databinding.FragmentBottomSheetBarBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class BarBottomSheetDialogFragment :
    BaseVBBottomSheetDialogFragment<FragmentBottomSheetBarBinding>() {

    private val vm: BarViewModel by viewModel()

    private lateinit var barAdapter: BarAdapter

    override fun bind() {
        binding = FragmentBottomSheetBarBinding.inflate(layoutInflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        observeStates()
        observeEffect()
        setListeners()
    }

    private fun initViews() {
        barAdapter = BarAdapter(vm.useCase.getEvent())
        binding.recyclerViewBar.adapter = barAdapter
    }

    private fun observeStates() = with(vm.useCase.state) {
        lifecycleScope.launchWhenStarted {
            currencyList.collect {
                barAdapter.submitList(it)
            }
        }

        lifecycleScope.launchWhenStarted {
            loading.collect {
                binding.loadingView.visibleIf(it)
            }
        }

        lifecycleScope.launchWhenStarted {
            enoughCurrency.collect {
                binding.recyclerViewBar.visibleIf(it)
                binding.txtNoEnoughCurrency.visibleIf(!it)
                binding.btnSelect.visibleIf(!it)
            }
        }
    }

    private fun observeEffect() = lifecycleScope.launchWhenStarted {
        vm.useCase.effect.collect { viewEffect ->
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

    private fun setListeners() {
        binding.btnSelect.setOnClickListener {
            vm.useCase.getEvent().onSelectClick()
        }
    }
}
