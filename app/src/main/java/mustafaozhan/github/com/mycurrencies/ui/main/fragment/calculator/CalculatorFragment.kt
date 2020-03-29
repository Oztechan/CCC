package mustafaozhan.github.com.mycurrencies.ui.main.fragment.calculator

import android.annotation.SuppressLint
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
import mustafaozhan.github.com.mycurrencies.extension.setBackgroundByName
import mustafaozhan.github.com.mycurrencies.extension.tryToSelect
import mustafaozhan.github.com.mycurrencies.extension.visible
import mustafaozhan.github.com.mycurrencies.model.Currency
import mustafaozhan.github.com.mycurrencies.model.Rates
import mustafaozhan.github.com.mycurrencies.tool.Toasty
import mustafaozhan.github.com.mycurrencies.tool.showSnacky
import mustafaozhan.github.com.mycurrencies.ui.main.MainDataViewModel.Companion.MINIMUM_ACTIVE_CURRENCY
import javax.inject.Inject

/**
 * Created by Mustafa Ozhan on 2018-07-12.
 */
@Suppress("TooManyFunctions")
class CalculatorFragment : BaseDBFragment<FragmentCalculatorBinding>(), CalculatorAction, CalculatorItemAction {

    companion object {
        fun newInstance(): CalculatorFragment = CalculatorFragment()
    }

    @Inject
    lateinit var calculatorViewModel: CalculatorViewModel

    private val calculatorAdapter: CalculatorAdapter by lazy { CalculatorAdapter(this) }

    override fun bind(container: ViewGroup?): FragmentCalculatorBinding =
        FragmentCalculatorBinding.inflate(layoutInflater, container, false)

    override fun onBinding(dataBinding: FragmentCalculatorBinding) {
        binding.viewModel = calculatorViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getBaseActivity()?.setSupportActionBar(binding.toolbarFragmentMain)
        initViews()
        initViewState()
        setListeners()
        initLiveData()
    }

    @SuppressLint("SetTextI18n")
    private fun initLiveData() {
        calculatorViewModel.currencyListLiveData.reObserve(viewLifecycleOwner, Observer { currencyList ->
            if (currencyList != null) {
                updateBar(currencyList.map { it.name })
            }
        })
    }

    private fun initViewState() = calculatorViewModel.calculatorViewStateLiveData
        .reObserve(viewLifecycleOwner, Observer { calculatorViewState ->
            when (calculatorViewState) {
                CalculatorViewState.Loading -> binding.loadingView.smoothToShow()
                CalculatorViewState.Error -> {
                    calculatorViewModel.currencyListLiveData.value?.size
                        ?.whether { it > 1 }
                        ?.let {
                            showSnacky(
                                view,
                                R.string.rate_not_available_offline,
                                R.string.change,
                                isIndefinite = true
                            ) { binding.layoutBar.spinnerBase.expand() }
                        }

                    calculatorAdapter.submitList(mutableListOf(), calculatorViewModel.mainData.currentBase)
                    binding.loadingView.smoothToHide()
                }
                CalculatorViewState.Empty -> {
                    binding.txtEmpty.visible()
                    binding.loadingView.smoothToHide()
                    calculatorAdapter.submitList(mutableListOf(), calculatorViewModel.mainData.currentBase)
                }
                CalculatorViewState.FewCurrency -> {
                    showSnacky(view, R.string.choose_at_least_two_currency, R.string.select) {
                        navigate(CalculatorFragmentDirections.actionCalculatorFragmentToSettingsFragment())
                    }

                    calculatorAdapter.submitList(mutableListOf(), calculatorViewModel.mainData.currentBase)
                    binding.loadingView.smoothToHide()
                }
                is CalculatorViewState.Success -> onStateSuccess(calculatorViewState.rates)
                is CalculatorViewState.OfflineSuccess -> {
                    onStateSuccess(calculatorViewState.rates)
                    calculatorViewState.rates.date?.let {
                        Toasty.showToasty(requireContext(), getString(R.string.database_success_with_date, it))
                    } ?: run {
                        Toasty.showToasty(requireContext(), R.string.database_success)
                    }
                }
                is CalculatorViewState.MaximumInput -> {
                    Toasty.showToasty(requireContext(), R.string.max_input)
                    calculatorViewModel.inputLiveData.postValue(calculatorViewState.input.dropLast(1))
                    binding.loadingView.smoothToHide()
                }
            }
        })

    private fun onStateSuccess(rates: Rates) {
        calculatorViewModel.currencyListLiveData.value?.let { currencyList ->
            currencyList.forEach { it.rate = calculatorViewModel.calculateResultByCurrency(it.name, rates) }
            calculatorAdapter.submitList(currencyList, calculatorViewModel.mainData.currentBase)
        }
        binding.loadingView.smoothToHide()
    }

    private fun initViews() = with(binding) {
        recyclerViewMain.layoutManager = LinearLayoutManager(requireContext())
        recyclerViewMain.adapter = calculatorAdapter
    }

    private fun updateBar(spinnerList: List<String>) = with(binding.layoutBar) {
        spinnerList
            .whether { size >= MINIMUM_ACTIVE_CURRENCY }
            ?.apply {
                spinnerBase.setItems(this)
                spinnerBase.tryToSelect(indexOf(calculatorViewModel.verifyCurrentBase(this).toString()))
                ivBase.setBackgroundByName(spinnerBase.text.toString())
            } ?: run {
            showSnacky(view, R.string.choose_at_least_two_currency, R.string.select) {
                navigate(CalculatorFragmentDirections.actionCalculatorFragmentToSettingsFragment())
            }
            spinnerBase.setItems("")
            ivBase.setBackgroundByName("transparent")
        }
    }

    private fun setListeners() = with(binding) {
        layoutBar.spinnerBase.setOnItemSelectedListener { _, _, _, item -> updateBase(item.toString()) }
        layoutBar.layoutBar.setOnClickListener {
            with(layoutBar.spinnerBase) {
                whether { isActivated }
                    ?.apply { collapse() }
                    ?: run { expand() }
            }
        }
    }

    private fun updateBase(base: String) {
        calculatorViewModel.updateCurrentBase(base)
        calculatorViewModel.inputLiveData.postValue(calculatorViewModel.inputLiveData.value)
        binding.layoutBar.ivBase.setBackgroundByName(base)
    }

    override fun switchBase(amount: String, currency: Currency) {
        with(binding) {
            calculatorViewModel.inputLiveData.postValue(amount)
            updateBase(currency.name)
            calculatorViewModel.currencyListLiveData.value
                ?.whether { indexOf(currency) < layoutBar.spinnerBase.getItems<String>().size }
                ?.apply { layoutBar.spinnerBase.tryToSelect(indexOf(currency)) }
                ?: run { layoutBar.spinnerBase.expand() }
        }
    }

    override fun showCurrencyComparison(currency: Currency) {
        showSnacky(
            view,
            "${calculatorViewModel.getClickedItemRate(currency.name)} ${currency.getVariablesOneLine()}",
            setIcon = currency.name
        )
    }
}
