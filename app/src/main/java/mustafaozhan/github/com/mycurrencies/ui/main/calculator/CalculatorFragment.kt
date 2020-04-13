package mustafaozhan.github.com.mycurrencies.ui.main.calculator

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mustafaozhan.basemob.fragment.BaseDBFragment
import com.github.mustafaozhan.scopemob.whether
import mustafaozhan.github.com.mycurrencies.R
import mustafaozhan.github.com.mycurrencies.databinding.FragmentCalculatorBinding
import mustafaozhan.github.com.mycurrencies.extension.reObserve
import mustafaozhan.github.com.mycurrencies.tool.Toasty
import mustafaozhan.github.com.mycurrencies.tool.showSnacky
import mustafaozhan.github.com.mycurrencies.ui.main.calculator.model.ErrorEffect
import mustafaozhan.github.com.mycurrencies.ui.main.calculator.model.FewCurrencyEffect
import mustafaozhan.github.com.mycurrencies.ui.main.calculator.model.LongClickEffect
import mustafaozhan.github.com.mycurrencies.ui.main.calculator.model.MaximumInputEffect
import mustafaozhan.github.com.mycurrencies.ui.main.calculator.model.OfflineSuccessEffect
import mustafaozhan.github.com.mycurrencies.ui.main.calculator.model.ReverseSpinner
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
        binding.viewModel = calculatorViewModel
        calculatorViewModel.event.let {
            binding.viewEvent = it
            calculatorAdapter = CalculatorAdapter(it)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getBaseActivity()?.setSupportActionBar(binding.toolbarFragmentMain)
        initView()
        initEffect()
    }

    private fun initEffect() = calculatorViewModel.effect
        .reObserve(viewLifecycleOwner, Observer { viewEffect ->
            when (viewEffect) {
                ErrorEffect -> showSnacky(
                    view,
                    R.string.rate_not_available_offline,
                    R.string.change,
                    isIndefinite = true
                ) { binding.layoutBar.spinnerBase.expand() }
                FewCurrencyEffect -> showSnacky(view, R.string.choose_at_least_two_currency, R.string.select) {
                    navigate(CalculatorFragmentDirections.actionCalculatorFragmentToSettingsFragment())
                }
                ReverseSpinner -> with(binding.layoutBar.spinnerBase) {
                    whether { isActivated }
                        ?.apply { collapse() }
                        ?: run { expand() }
                }
                is MaximumInputEffect -> {
                    Toasty.showToasty(requireContext(), R.string.max_input)
                    calculatorViewModel.state.input.postValue(viewEffect.input.dropLast(1))
                    binding.loadingView.smoothToHide()
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

        calculatorViewModel.state.apply {
            currencyList.reObserve(viewLifecycleOwner, Observer { currencyList ->
                binding.layoutBar.spinnerBase.setItems(currencyList.map { it.name })
                calculatorAdapter.submitList(currencyList, calculatorViewModel.data.currentBase)
            })
        }
    }
}
