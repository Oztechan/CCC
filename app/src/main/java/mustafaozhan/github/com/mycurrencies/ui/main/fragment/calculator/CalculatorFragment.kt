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
import mustafaozhan.github.com.mycurrencies.extension.gone
import mustafaozhan.github.com.mycurrencies.extension.reObserve
import mustafaozhan.github.com.mycurrencies.extension.replaceNonStandardDigits
import mustafaozhan.github.com.mycurrencies.extension.setBackgroundByName
import mustafaozhan.github.com.mycurrencies.extension.tryToSelect
import mustafaozhan.github.com.mycurrencies.extension.visible
import mustafaozhan.github.com.mycurrencies.tool.Toasty
import mustafaozhan.github.com.mycurrencies.tool.showSnacky
import mustafaozhan.github.com.mycurrencies.ui.main.MainDataViewModel.Companion.MINIMUM_ACTIVE_CURRENCY
import mustafaozhan.github.com.mycurrencies.ui.main.fragment.calculator.view.CalculatorViewEvent
import mustafaozhan.github.com.mycurrencies.ui.main.fragment.calculator.view.EmptyState
import mustafaozhan.github.com.mycurrencies.ui.main.fragment.calculator.view.ErrorEffect
import mustafaozhan.github.com.mycurrencies.ui.main.fragment.calculator.view.FewCurrencyEffect
import mustafaozhan.github.com.mycurrencies.ui.main.fragment.calculator.view.LoadingState
import mustafaozhan.github.com.mycurrencies.ui.main.fragment.calculator.view.LongClickEffect
import mustafaozhan.github.com.mycurrencies.ui.main.fragment.calculator.view.MaximumInputEffect
import mustafaozhan.github.com.mycurrencies.ui.main.fragment.calculator.view.OfflineSuccessEffect
import mustafaozhan.github.com.mycurrencies.ui.main.fragment.calculator.view.SuccessState
import mustafaozhan.github.com.mycurrencies.ui.main.fragment.calculator.view.SwitchBaseEffect
import javax.inject.Inject

/**
 * Created by Mustafa Ozhan on 2018-07-12.
 */
@Suppress("TooManyFunctions")
class CalculatorFragment : BaseDBFragment<FragmentCalculatorBinding>() {

    @Inject
    lateinit var calculatorViewModel: CalculatorViewModel

    private lateinit var viewEvent: CalculatorViewEvent

    private val calculatorAdapter: CalculatorAdapter by lazy { CalculatorAdapter(viewEvent) }

    override fun bind(container: ViewGroup?): FragmentCalculatorBinding =
        FragmentCalculatorBinding.inflate(layoutInflater, container, false)

    override fun onBinding(dataBinding: FragmentCalculatorBinding) {
        binding.viewModel = calculatorViewModel
        calculatorViewModel.getViewEvent().let {
            binding.viewEvent = it
            viewEvent = it
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getBaseActivity()?.setSupportActionBar(binding.toolbarFragmentMain)
        initViews()
        initViewState()
        initViewEffect()
        setListeners()
        initLiveData()
    }

    private fun initViewState() = calculatorViewModel.viewStateLiveData
        .reObserve(viewLifecycleOwner, Observer { viewState ->
            when (viewState) {
                LoadingState -> binding.loadingView.smoothToShow()
                EmptyState -> {
                    binding.txtEmpty.visible()
                    binding.loadingView.smoothToHide()
                    calculatorAdapter.submitList(mutableListOf(), calculatorViewModel.mainData.currentBase)
                }
                is SuccessState -> {
                    calculatorAdapter.submitList(
                        viewState.currencyList,
                        calculatorViewModel.mainData.currentBase
                    )
                    binding.loadingView.smoothToHide()
                }
            }
        })

    private fun initViewEffect() = calculatorViewModel.viewEffectLiveData
        .reObserve(viewLifecycleOwner, Observer { viewEffect ->
            when (viewEffect) {
                ErrorEffect -> calculatorViewModel.currencyListLiveData.value?.size
                    ?.whether { it > 1 }
                    ?.let {
                        showSnacky(
                            view,
                            R.string.rate_not_available_offline,
                            R.string.change,
                            isIndefinite = true
                        ) { binding.layoutBar.spinnerBase.expand() }
                    }
                FewCurrencyEffect -> showSnacky(view, R.string.choose_at_least_two_currency, R.string.select) {
                    navigate(CalculatorFragmentDirections.actionCalculatorFragmentToSettingsFragment())
                }
                is MaximumInputEffect -> {
                    Toasty.showToasty(requireContext(), R.string.max_input)
                    calculatorViewModel.inputLiveData.postValue(viewEffect.input.dropLast(1))
                    binding.loadingView.smoothToHide()
                }
                is OfflineSuccessEffect -> viewEffect.date?.let {
                    Toasty.showToasty(requireContext(), getString(R.string.database_success_with_date, it))
                } ?: run {
                    Toasty.showToasty(requireContext(), R.string.database_success)
                }

                is LongClickEffect -> showSnacky(view, viewEffect.text, setIcon = viewEffect.name)
                is SwitchBaseEffect -> {
                    binding.txtInput.text = viewEffect.text
                    updateBase(viewEffect.base)
                    binding.layoutBar.spinnerBase.tryToSelect(viewEffect.index)
                }
            }
        })

    @SuppressLint("SetTextI18n")
    private fun initLiveData() {
        calculatorViewModel.currencyListLiveData.reObserve(this, Observer { currencyList ->
            updateBar(currencyList.map { it.name })
            calculatorAdapter.submitList(currencyList, calculatorViewModel.mainData.currentBase)
            binding.loadingView.smoothToHide()
        })

        calculatorViewModel.outputLiveData.reObserve(this, Observer { output ->
            with(binding.layoutBar) {
                txtSymbol.text = calculatorViewModel.getCurrencyByName(
                    calculatorViewModel.mainData.currentBase.toString()
                )?.symbol ?: ""

                output.toString()
                    .whetherNot { isEmpty() }
                    ?.apply { txtOutput.text = "=  ${replaceNonStandardDigits()} " }
                    ?: run {
                        txtOutput.text = ""
                        txtSymbol.text = ""
                    }
            }
        })

        calculatorViewModel.inputLiveData.reObserve(this, Observer { input ->
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
        loadingView.bringToFront()
        txtEmpty.visible()
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
}
