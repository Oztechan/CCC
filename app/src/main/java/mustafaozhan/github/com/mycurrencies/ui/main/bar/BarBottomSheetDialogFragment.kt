/*
 Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package mustafaozhan.github.com.mycurrencies.ui.main.bar

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mustafaozhan.basemob.util.reObserve
import com.github.mustafaozhan.basemob.util.setNavigationResult
import com.github.mustafaozhan.basemob.view.bottomsheet.BaseDBBottomSheetDialogFragment
import mustafaozhan.github.com.mycurrencies.R
import mustafaozhan.github.com.mycurrencies.databinding.FragmentBottomSheetBarBinding
import mustafaozhan.github.com.mycurrencies.ui.main.MainData.Companion.KEY_BASE_CURRENCY
import mustafaozhan.github.com.mycurrencies.ui.main.calculator.bar.BarBottomSheetDialogFragmentDirections
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
                OpenSettingsEffect -> navigate(
                    R.id.barBottomSheetDialogFragment,
                    BarBottomSheetDialogFragmentDirections.actionBarBottomSheetDialogFragmentToSettingsFragment(),
                    dismiss = false
                )
            }
        })

    private fun initView() {
        binding.recyclerViewBar.apply {
            adapter = barAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        barViewModel.state.currencyList.reObserve(viewLifecycleOwner, Observer {
            barAdapter.submitList(it)
        })
    }
}
