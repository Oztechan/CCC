/*
 Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package mustafaozhan.github.com.mycurrencies.ui.main.calculator.bar

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mustafaozhan.basemob.bottomsheet.BaseDBBottomSheetDialogFragment
import com.github.mustafaozhan.basemob.extension.reObserveSingle
import mustafaozhan.github.com.mycurrencies.databinding.FragmentBottomSheetBarBinding
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
        initEffect()
    }

    private fun initEffect() = barViewModel.effect
        .reObserveSingle(viewLifecycleOwner, Observer { viewEffect ->
            when (viewEffect) {
            }
        })

    private fun initView() {
        binding.recyclerViewBar.apply {
            adapter = barAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        barViewModel.apply {
//            state.currencyList.reObserve(viewLifecycleOwner, Observer { list ->
//                list?.let { currencyList ->
//                    binding.layoutBar.spinnerBase
//                        .apply {
//                            setItems(currencyList.map { it.name })
//                            tryToSelect(preferencesRepository.currentBase)
//                        }
//                    calculatorAdapter.submitList(currencyList, preferencesRepository.currentBase)
//                }
//            })
        }
    }
}
