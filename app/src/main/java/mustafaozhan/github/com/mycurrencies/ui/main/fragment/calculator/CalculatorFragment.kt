package mustafaozhan.github.com.mycurrencies.ui.main.fragment.calculator

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mustafaozhan.basemob.fragment.BaseVBFragment
import com.github.mustafaozhan.logmob.logError
import com.github.mustafaozhan.scopemob.whether
import com.github.mustafaozhan.scopemob.whetherNot
import com.jakewharton.rxbinding2.widget.textChanges
import mustafaozhan.github.com.mycurrencies.R
import mustafaozhan.github.com.mycurrencies.databinding.FragmentCalculatorBinding
import mustafaozhan.github.com.mycurrencies.extension.addText
import mustafaozhan.github.com.mycurrencies.extension.checkAd
import mustafaozhan.github.com.mycurrencies.extension.dropDecimal
import mustafaozhan.github.com.mycurrencies.extension.gone
import mustafaozhan.github.com.mycurrencies.extension.reObserve
import mustafaozhan.github.com.mycurrencies.extension.replaceNonStandardDigits
import mustafaozhan.github.com.mycurrencies.extension.setBackgroundByName
import mustafaozhan.github.com.mycurrencies.extension.tryToSelect
import mustafaozhan.github.com.mycurrencies.extension.visible
import mustafaozhan.github.com.mycurrencies.tool.Toasty
import mustafaozhan.github.com.mycurrencies.tool.showSnacky
import mustafaozhan.github.com.mycurrencies.ui.main.MainDataViewModel.Companion.MINIMUM_ACTIVE_CURRENCY
import mustafaozhan.github.com.mycurrencies.ui.main.fragment.calculator.view.EmptyState
import mustafaozhan.github.com.mycurrencies.ui.main.fragment.calculator.view.ErrorEffect
import mustafaozhan.github.com.mycurrencies.ui.main.fragment.calculator.view.FewCurrencyEffect
import mustafaozhan.github.com.mycurrencies.ui.main.fragment.calculator.view.LoadingState
import mustafaozhan.github.com.mycurrencies.ui.main.fragment.calculator.view.MaximumInputEffect
import mustafaozhan.github.com.mycurrencies.ui.main.fragment.calculator.view.OfflineSuccessEffect
import mustafaozhan.github.com.mycurrencies.ui.main.fragment.calculator.view.SuccessState
import javax.inject.Inject

/**
 * Created by Mustafa Ozhan on 2018-07-12.
 */
@Suppress("TooManyFunctions")
class CalculatorFragment : BaseVBFragment<FragmentCalculatorBinding>() {

    @Inject
    lateinit var calculatorViewModel: CalculatorViewModel

    private val calculatorAdapter: CalculatorAdapter by lazy { CalculatorAdapter() }

    override fun bind() {
        binding = FragmentCalculatorBinding.inflate(layoutInflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getBaseActivity()?.setSupportActionBar(binding.toolbarFragmentMain)
        initViews()
        initViewState()
        initViewEffect()
        setRx()
        setListeners()
        initLiveData()
    }

    private fun setRx() = compositeDisposable.add(
        binding.txtInput.textChanges()
            .map { it.toString() }
            .subscribe(
                {
                    if (it.isEmpty()) {
                        calculatorViewModel.postEmptyState()
                        calculatorViewModel.outputLiveData.postValue("")
                    } else {
                        calculatorViewModel.calculateOutput(it)
                        binding.txtEmpty.gone()
                    }
                },
                { logError(it) }
            )
    )

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
                    calculatorViewModel.currencyListLiveData.value?.let { currencyList ->
                        currencyList.forEach {
                            it.rate = calculatorViewModel.calculateResultByCurrency(it.name, viewState.rates)
                        }
                        calculatorAdapter.submitList(currencyList, calculatorViewModel.mainData.currentBase)
                    }
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
                    binding.txtInput.text = viewEffect.input.dropLast(1)
                    binding.loadingView.smoothToHide()
                }
                is OfflineSuccessEffect -> {
                    viewEffect.date?.let {
                        Toasty.showToasty(requireContext(), getString(R.string.database_success_with_date, it))
                    } ?: run {
                        Toasty.showToasty(requireContext(), R.string.database_success)
                    }
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
    }

    private fun initViews() = with(binding) {
        adView.checkAd(R.string.banner_ad_unit_id_main, calculatorViewModel.isRewardExpired)
        loadingView.bringToFront()
        txtEmpty.visible()
        recyclerViewMain.layoutManager = LinearLayoutManager(requireContext())
        recyclerViewMain.adapter = calculatorAdapter

        calculatorAdapter.onItemClickListener = { currency, itemBinding ->
            txtInput.text = itemBinding.txtAmount.text.toString().dropDecimal()
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
        layoutKeyboard.btnSeven.setOnClickListener { txtInput.addText("7") }
        layoutKeyboard.btnEight.setOnClickListener { txtInput.addText("8") }
        layoutKeyboard.btnNine.setOnClickListener { txtInput.addText("9") }
        layoutKeyboard.btnDivide.setOnClickListener { txtInput.addText("/") }
        layoutKeyboard.btnFour.setOnClickListener { txtInput.addText("4") }
        layoutKeyboard.btnFive.setOnClickListener { txtInput.addText("5") }
        layoutKeyboard.btnSix.setOnClickListener { txtInput.addText("6") }
        layoutKeyboard.btnMultiply.setOnClickListener { txtInput.addText("*") }
        layoutKeyboard.btnOne.setOnClickListener { txtInput.addText("1") }
        layoutKeyboard.btnTwo.setOnClickListener { txtInput.addText("2") }
        layoutKeyboard.btnThree.setOnClickListener { txtInput.addText("3") }
        layoutKeyboard.btnMinus.setOnClickListener { txtInput.addText("-") }
        layoutKeyboard.btnDot.setOnClickListener { txtInput.addText(".") }
        layoutKeyboard.btnZero.setOnClickListener { txtInput.addText("0") }
        layoutKeyboard.btnPercent.setOnClickListener { txtInput.addText("%") }
        layoutKeyboard.btnPlus.setOnClickListener { txtInput.addText("+") }
        layoutKeyboard.btnTripleZero.setOnClickListener { txtInput.addText("000") }
        layoutKeyboard.btnZero.setOnClickListener { txtInput.addText("0") }
        layoutKeyboard.btnAc.setOnClickListener {
            txtInput.text = ""
            layoutBar.txtOutput.text = ""
            layoutBar.txtSymbol.text = ""
        }
        layoutKeyboard.btnDelete.setOnClickListener {
            txtInput.text.toString()
                .whetherNot { isEmpty() }
                ?.apply { txtInput.text = substring(0, length - 1) }
        }
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
        binding.txtInput.text = binding.txtInput.text
        binding.layoutBar.ivBase.setBackgroundByName(base)
    }
}
