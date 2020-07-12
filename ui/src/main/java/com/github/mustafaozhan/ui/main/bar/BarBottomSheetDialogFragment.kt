/*
 Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ui.main.bar

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.github.mustafaozhan.basemob.util.reObserve
import com.github.mustafaozhan.basemob.util.setNavigationResult
import com.github.mustafaozhan.basemob.view.bottomsheet.BaseDBBottomSheetDialogFragment
import com.github.mustafaozhan.ui.R
import com.github.mustafaozhan.ui.databinding.FragmentBottomSheetBarBinding
import com.github.mustafaozhan.ui.main.MainData.Companion.KEY_BASE_CURRENCY
import javax.inject.Inject

class BarBottomSheetDialogFragment : BaseDBBottomSheetDialogFragment<FragmentBottomSheetBarBinding>() {

    @Inject
    lateinit var barViewModel: BarViewModel

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
        .reObserve(viewLifecycleOwner, Observer { viewEffect ->
            when (viewEffect) {
                is ChangeBaseNavResultEffect -> {
                    setNavigationResult(viewEffect.newBase, KEY_BASE_CURRENCY)
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

        barViewModel.state.currencyList.reObserve(viewLifecycleOwner, Observer {
            barAdapter.submitList(it)
        })
    }
}
