package mustafaozhan.github.com.mycurrencies.ui.main.calculator

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mustafaozhan.basemob.extension.reObserve
import com.github.mustafaozhan.basemob.fragment.BaseDBFragment
import com.github.mustafaozhan.scopemob.whether
import mustafaozhan.github.com.mycurrencies.R
import mustafaozhan.github.com.mycurrencies.databinding.FragmentCalculatorBinding
import mustafaozhan.github.com.mycurrencies.extension.tryToSelect
import mustafaozhan.github.com.mycurrencies.ui.main.calculator.model.ErrorEvent
import mustafaozhan.github.com.mycurrencies.ui.main.calculator.model.FewCurrencyEvent
import mustafaozhan.github.com.mycurrencies.ui.main.calculator.model.LongClickEvent
import mustafaozhan.github.com.mycurrencies.ui.main.calculator.model.MaximumInputEvent
import mustafaozhan.github.com.mycurrencies.ui.main.calculator.model.OfflineSuccessEvent
import mustafaozhan.github.com.mycurrencies.ui.main.calculator.model.ReverseSpinner
import mustafaozhan.github.com.mycurrencies.util.Toasty
import mustafaozhan.github.com.mycurrencies.util.showSnacky
import javax.inject.Inject

/**
 * Created by Mustafa Ozhan on 2018-07-12.
 */
class CalculatorFragment : BaseDBFragment<FragmentCalculatorBinding>() {

    @Inject
    lateinit var calculatorViewModel: CalculatorViewModel

    private lateinit var calculatorAdapter: CalculatorAdapter

    override fun bind(container: ViewGroup?): FragmentCalculatorBinding =
        FragmentCalculatorBinding.inflate(layoutInflater, container, false)

    override fun onBinding(dataBinding: FragmentCalculatorBinding) {
        binding.vm = calculatorViewModel
        calculatorViewModel.getActions().let {
            binding.actions = it
            calculatorAdapter = CalculatorAdapter(it)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getBaseActivity()?.setSupportActionBar(binding.toolbarFragmentMain)
        initView()
        initEvent()
    }

    override fun onResume() {
        super.onResume()
        calculatorViewModel.verifyCurrentBase()
    }

    private fun initEvent() = calculatorViewModel.events
        .reObserve(viewLifecycleOwner, Observer { viewEvent ->
            when (viewEvent) {
                ErrorEvent -> showSnacky(
                    view,
                    R.string.rate_not_available_offline,
                    R.string.change,
                    isIndefinite = true
                ) { binding.layoutBar.spinnerBase.expand() }
                FewCurrencyEvent -> showSnacky(view, R.string.choose_at_least_two_currency, R.string.select) {
                    navigate(CalculatorFragmentDirections.actionCalculatorFragmentToSettingsFragment())
                }
                ReverseSpinner -> with(binding.layoutBar.spinnerBase) {
                    whether { isActivated }
                        ?.apply { collapse() }
                        ?: run { expand() }
                }
                MaximumInputEvent -> Toasty.showToasty(requireContext(), R.string.max_input)
                is OfflineSuccessEvent -> viewEvent.date?.let {
                    Toasty.showToasty(requireContext(), getString(R.string.database_success_with_date, it))
                } ?: run {
                    Toasty.showToasty(requireContext(), R.string.database_success)
                }
                is LongClickEvent -> showSnacky(view, viewEvent.text, setIcon = viewEvent.name)
            }
        })

    private fun initView() {
        binding.recyclerViewMain.apply {
            adapter = calculatorAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        calculatorViewModel.states.apply {
            currencyList.reObserve(viewLifecycleOwner, Observer { currencyList ->
                binding.layoutBar.spinnerBase
                    .apply {
                        setItems(currencyList.map { it.name })
                        tryToSelect(calculatorViewModel.preferencesRepository.currentBase)
                    }
                calculatorAdapter.submitList(currencyList, calculatorViewModel.preferencesRepository.currentBase)
            })
        }
    }
}
