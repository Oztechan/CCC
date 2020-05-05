/*
 Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package mustafaozhan.github.com.mycurrencies.ui.main.calculator

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mustafaozhan.basemob.extension.reObserve
import com.github.mustafaozhan.basemob.extension.reObserveSingle
import com.github.mustafaozhan.basemob.fragment.BaseDBFragment
import com.github.mustafaozhan.scopemob.whether
import mustafaozhan.github.com.mycurrencies.R
import mustafaozhan.github.com.mycurrencies.databinding.FragmentCalculatorBinding
import mustafaozhan.github.com.mycurrencies.ui.main.calculator.model.ErrorEffect
import mustafaozhan.github.com.mycurrencies.ui.main.calculator.model.FewCurrencyEffect
import mustafaozhan.github.com.mycurrencies.ui.main.calculator.model.LongClickEffect
import mustafaozhan.github.com.mycurrencies.ui.main.calculator.model.MaximumInputEffect
import mustafaozhan.github.com.mycurrencies.ui.main.calculator.model.OfflineSuccessEffect
import mustafaozhan.github.com.mycurrencies.ui.main.calculator.model.ReverseSpinner
import mustafaozhan.github.com.mycurrencies.util.Toasty
import mustafaozhan.github.com.mycurrencies.util.extension.tryToSelect
import mustafaozhan.github.com.mycurrencies.util.showSnacky
import javax.inject.Inject

class CalculatorFragment : BaseDBFragment<FragmentCalculatorBinding>() {

    @Inject
    lateinit var calculatorViewModel: CalculatorViewModel

    private lateinit var calculatorAdapter: CalculatorAdapter

    override fun bind(container: ViewGroup?): FragmentCalculatorBinding =
        FragmentCalculatorBinding.inflate(layoutInflater, container, false)

    override fun onBinding(dataBinding: FragmentCalculatorBinding) {
        binding.vm = calculatorViewModel
        calculatorViewModel.getEvent().let {
            binding.event = it
            calculatorAdapter = CalculatorAdapter(it)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getBaseActivity()?.setSupportActionBar(binding.toolbarFragmentMain)
        initView()
        initEffect()
    }

    override fun onResume() {
        super.onResume()
        calculatorViewModel.verifyCurrentBase()
    }

    private fun initEffect() = calculatorViewModel.effect
        .reObserveSingle(viewLifecycleOwner, Observer { viewEffect ->
            when (viewEffect) {
                ErrorEffect -> showSnacky(
                    view,
                    R.string.rate_not_available_offline
                ) { binding.layoutBar.spinnerBase.expand() }
                FewCurrencyEffect -> showSnacky(view, R.string.choose_at_least_two_currency, R.string.select) {
                    navigate(CalculatorFragmentDirections.actionCalculatorFragmentToSettingsFragment())
                }
                MaximumInputEffect -> Toasty.showToasty(requireContext(), R.string.max_input)
                ReverseSpinner -> with(binding.layoutBar.spinnerBase) {
                    whether { isActivated }
                        ?.apply { collapse() }
                        ?: run { expand() }
                }
                is OfflineSuccessEffect -> viewEffect.date?.let {
                    Toasty.showToasty(requireContext(), getString(R.string.database_success_with_date, it))
                } ?: run {
                    Toasty.showToasty(requireContext(), R.string.database_success)
                }
                is LongClickEffect -> showSnacky(view, viewEffect.text, setIcon = viewEffect.name)
            }
        })

    private fun initView() {
        binding.recyclerViewMain.apply {
            adapter = calculatorAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        calculatorViewModel.apply {
            state.currencyList.reObserve(viewLifecycleOwner, Observer { list ->
                list?.let { currencyList ->
                    binding.layoutBar.spinnerBase
                        .apply {
                            setItems(currencyList.map { it.name })
                            tryToSelect(preferencesRepository.currentBase)
                        }
                    calculatorAdapter.submitList(currencyList, preferencesRepository.currentBase)
                }
            })
        }
    }
}
