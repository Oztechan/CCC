/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ccc.android.ui.bar

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import com.github.mustafaozhan.basemob.bottomsheet.BaseDBBottomSheetDialogFragment
import com.github.mustafaozhan.ccc.android.util.KEY_BASE_CURRENCY
import com.github.mustafaozhan.ccc.android.util.reObserve
import com.github.mustafaozhan.ccc.android.util.setNavigationResult
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

    private fun observeEffect() = barViewModel.effect
        .reObserve(viewLifecycleOwner, { viewEffect ->
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
        })

    private fun initView() {
        binding.recyclerViewBar.adapter = barAdapter

        barViewModel.state.currencyList.reObserve(viewLifecycleOwner, {
            barAdapter.submitList(it)
        })
    }
}
