/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ccc.android.ui.bar

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.github.mustafaozhan.basemob.bottomsheet.BaseDBBottomSheetDialogFragment
import com.github.mustafaozhan.ccc.android.util.setNavigationResult
import com.github.mustafaozhan.ccc.client.ui.bar.BarViewModel
import com.github.mustafaozhan.ccc.client.ui.bar.ChangeBaseNavResultEffect
import com.github.mustafaozhan.ccc.client.ui.bar.OpenCurrenciesEffect
import com.github.mustafaozhan.ccc.client.util.KEY_BASE_CURRENCY
import kotlinx.coroutines.flow.collect
import mustafaozhan.github.com.mycurrencies.R
import mustafaozhan.github.com.mycurrencies.databinding.FragmentBottomSheetBarBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class BarBottomSheetDialogFragment :
    BaseDBBottomSheetDialogFragment<FragmentBottomSheetBarBinding>() {

    private val barViewModel: BarViewModel by viewModel()

    private lateinit var barAdapter: BarAdapter

    override fun bind(container: ViewGroup?): FragmentBottomSheetBarBinding =
        FragmentBottomSheetBarBinding.inflate(layoutInflater, container, false)

    override fun onBinding(dataBinding: FragmentBottomSheetBarBinding) {
        binding.vm = barViewModel
        barViewModel.getEvent().let {
            binding.event = it
            barAdapter = BarAdapter(it)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        observeEffect()
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
        binding.recyclerViewBar.adapter = barAdapter

        lifecycleScope.launchWhenStarted {
            barViewModel.state.currencyList.collect {
                barAdapter.submitList(it)
            }
        }
    }
}
