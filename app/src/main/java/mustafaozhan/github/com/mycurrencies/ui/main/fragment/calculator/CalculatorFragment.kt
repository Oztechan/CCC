package mustafaozhan.github.com.mycurrencies.ui.main.fragment.calculator

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mustafaozhan.basemob.fragment.BaseDBFragment
import com.github.mustafaozhan.scopemob.whether
import com.github.mustafaozhan.scopemob.whetherNot
import mustafaozhan.github.com.mycurrencies.R
import mustafaozhan.github.com.mycurrencies.databinding.FragmentCalculatorBinding
import mustafaozhan.github.com.mycurrencies.extension.dropDecimal
import mustafaozhan.github.com.mycurrencies.extension.gone
import mustafaozhan.github.com.mycurrencies.extension.reObserve
import mustafaozhan.github.com.mycurrencies.extension.replaceNonStandardDigits
import mustafaozhan.github.com.mycurrencies.extension.setBackgroundByName
import mustafaozhan.github.com.mycurrencies.extension.tryToSelect
import mustafaozhan.github.com.mycurrencies.tool.showSnacky
import mustafaozhan.github.com.mycurrencies.ui.main.MainDataViewModel.Companion.MINIMUM_ACTIVE_CURRENCY
import javax.inject.Inject

/**
 * Created by Mustafa Ozhan on 2018-07-12.
 */
@Suppress("TooManyFunctions")
class CalculatorFragment : BaseDBFragment<FragmentCalculatorBinding>() {

    companion object {
        fun newInstance(): CalculatorFragment = CalculatorFragment()
    }

    @Inject
    lateinit var calculatorViewModel: CalculatorViewModel

    private val calculatorAdapter: CalculatorAdapter by lazy { CalculatorAdapter() }

    override fun bind(container: ViewGroup?): FragmentCalculatorBinding =
        FragmentCalculatorBinding.inflate(layoutInflater, container, false)

    override fun onBinding(dataBinding: FragmentCalculatorBinding) {
        binding.viewModel = calculatorViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getBaseActivity()?.setSupportActionBar(binding.toolbarFragmentMain)
        initViews()
        setListeners()
        initLiveData()
    }

    @SuppressLint("SetTextI18n")
    private fun initLiveData() {
        calculatorViewModel.currencyListLiveData.reObserve(viewLifecycleOwner, Observer { currencyList ->
            updateBar(currencyList.map { it.name })
        })

        calculatorViewModel.outputLiveData.reObserve(viewLifecycleOwner, Observer { output ->
            with(binding.layoutBar) {
                txtSymbol.text = calculatorViewModel.getCurrencyByName(
                    calculatorViewModel.mainData.currentBase.toString()
                )?.symbol

                output.toString()
                    .whetherNot { isEmpty() }
                    ?.apply { txtOutput.text = "=  ${replaceNonStandardDigits()} " }
                    ?: run {
                        txtOutput.text = ""
                        txtSymbol.text = ""
                    }
            }
        })

        calculatorViewModel.inputLiveData.reObserve(viewLifecycleOwner, Observer { input ->
            if (input.isEmpty()) {
                calculatorViewModel.postEmptyState()
                calculatorViewModel.outputLiveData.postValue("")
            } else {
                calculatorViewModel.calculateOutput(input)
                binding.txtEmpty.gone()
            }
        })
    }

    private fun initViews() = with(binding) {
        recyclerViewMain.layoutManager = LinearLayoutManager(requireContext())
        recyclerViewMain.adapter = calculatorAdapter

        calculatorAdapter.onItemClickListener = { currency, itemBinding, _: Int ->
            calculatorViewModel.inputLiveData.postValue(itemBinding.txtAmount.text.toString().dropDecimal())
            updateBase(currency.name)
            calculatorViewModel.currencyListLiveData.value
                ?.whether { indexOf(currency) < layoutBar.spinnerBase.getItems<String>().size }
                ?.apply { layoutBar.spinnerBase.tryToSelect(indexOf(currency)) }
                ?: run { layoutBar.spinnerBase.expand() }
        }
        calculatorAdapter.onItemLongClickListener = { currency, _ ->
            showSnacky(
                view,
                "${calculatorViewModel.getClickedItemRate(currency.name)} ${currency.getVariablesOneLine()}",
                setIcon = currency.name
            )
            true
        }
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
}
