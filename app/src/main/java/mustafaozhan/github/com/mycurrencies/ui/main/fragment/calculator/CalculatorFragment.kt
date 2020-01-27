package mustafaozhan.github.com.mycurrencies.ui.main.fragment.calculator

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.jakewharton.rxbinding2.widget.textChanges
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.item_currency.view.txt_amount
import mustafaozhan.github.com.mycurrencies.R
import mustafaozhan.github.com.mycurrencies.base.fragment.BaseViewBindingFragment
import mustafaozhan.github.com.mycurrencies.databinding.FragmentCalculatorBinding
import mustafaozhan.github.com.mycurrencies.extensions.addText
import mustafaozhan.github.com.mycurrencies.extensions.checkAd
import mustafaozhan.github.com.mycurrencies.extensions.dropDecimal
import mustafaozhan.github.com.mycurrencies.extensions.reObserve
import mustafaozhan.github.com.mycurrencies.extensions.replaceNonStandardDigits
import mustafaozhan.github.com.mycurrencies.extensions.setBackgroundByName
import mustafaozhan.github.com.mycurrencies.extensions.tryToSelect
import mustafaozhan.github.com.mycurrencies.model.Rates
import mustafaozhan.github.com.mycurrencies.room.AppDatabase
import mustafaozhan.github.com.mycurrencies.ui.main.fragment.settings.SettingsFragment
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

/**
 * Created by Mustafa Ozhan on 2018-07-12.
 */
@Suppress("TooManyFunctions")
class CalculatorFragment : BaseViewBindingFragment<CalculatorViewModel, FragmentCalculatorBinding>() {

    companion object {
        fun newInstance(): CalculatorFragment = CalculatorFragment()
    }

    override fun getLayoutResId(): Int = R.layout.fragment_calculator

    override fun bind() {
        binding = FragmentCalculatorBinding.inflate(layoutInflater)
    }

    private val calculatorFragmentAdapter: CalculatorFragmentAdapter by lazy { CalculatorFragmentAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setSupportActionBar(binding.toolbarFragmentMain)
        initViews()
        setListeners()
        setKeyboard()
        initViewState()
        setRx()
        initLiveData()
    }

    override fun onResume() {
        super.onResume()
        initData()
        binding.adView.checkAd(R.string.banner_ad_unit_id_main, viewModel.isRewardExpired())
    }

    private fun setRx() {
        binding.txtInput.textChanges()
            .map { it.toString() }
            .subscribe({ input ->
                viewModel.calculateOutput(input)
            }, { throwable ->
                logException(throwable)
            })
            .addTo(compositeDisposable)
    }

    private fun initViewState() = viewModel.calculatorViewStateLiveData
        .reObserve(this, Observer { calculatorViewState ->
            when (calculatorViewState) {
                CalculatorViewState.Loading -> binding.loadingView.smoothToShow()
                is CalculatorViewState.Success -> onSearchSuccess(calculatorViewState.rates)
                is CalculatorViewState.OfflineSuccess -> {
                    onSearchSuccess(calculatorViewState.rates)
                    calculatorViewState.rates.date?.let {
                        toasty(getString(R.string.database_success_with_date, it))
                    } ?: run {
                        toasty(getString(R.string.database_success))
                    }
                }
                CalculatorViewState.Error -> {
                    if (viewModel.currencyListLiveData.value?.size ?: 0 > 1) {
                        snacky(getString(R.string.rate_not_available_offline), getString(R.string.change)) {
                            binding.layoutBar.spinnerBase.expand()
                        }
                    }

                    calculatorFragmentAdapter.refreshList(mutableListOf(), viewModel.getMainData().currentBase)
                    binding.loadingView.smoothToHide()
                }
                is CalculatorViewState.MaximumInput -> {
                    toasty(getString(R.string.max_input))
                    binding.txtInput.text = calculatorViewState.input.dropLast(1)
                }
                CalculatorViewState.FewCurrency -> {
                    snacky(getString(R.string.choose_at_least_two_currency), getString(R.string.select)) {
                        replaceFragment(SettingsFragment.newInstance(), true)
                    }
                }
            }
        })

    @SuppressLint("SetTextI18n")
    private fun initLiveData() {
        viewModel.currencyListLiveData.reObserve(this, Observer { currencyList ->
            currencyList?.let {
                updateBar(currencyList.map { it.name })
                calculatorFragmentAdapter.refreshList(currencyList, viewModel.getMainData().currentBase)
                binding.loadingView.smoothToHide()
            }
        })

        viewModel.outputLiveData.reObserve(this, Observer { output ->
            with(binding.layoutBar) {
                txtSymbol.text = viewModel.getCurrencyByName(
                    viewModel.getMainData().currentBase.toString()
                )?.symbol

                if (output.toString().isEmpty()) {
                    txtOutput.text = ""
                    txtSymbol.text = ""
                } else {
                    txtOutput.text = "=  ${output.toString().replaceNonStandardDigits()} "
                }
            }
        })
    }

    private fun onSearchSuccess(rates: Rates) {
        viewModel.currencyListLiveData.value?.let { currencyList ->
            currencyList.forEach { it.rate = viewModel.calculateResultByCurrency(it.name, rates) }
            calculatorFragmentAdapter.refreshList(currencyList, viewModel.getMainData().currentBase)
        }
        binding.loadingView.smoothToHide()
    }

    private fun initViews() = with(binding) {
        loadingView.bringToFront()
        context?.let { ctx ->
            recyclerViewMain.layoutManager = LinearLayoutManager(ctx)
            recyclerViewMain.adapter = calculatorFragmentAdapter
        }
        calculatorFragmentAdapter.onItemClickListener = { currency, itemView: View, _: Int ->
            txtInput.text = itemView.txt_amount.text.toString().dropDecimal()
            viewModel.updateCurrentBase(currency.name)
            viewModel.calculateOutput(itemView.txt_amount.text.toString().dropDecimal())
            viewModel.currencyListLiveData.value?.let { currencyList ->
                if (currencyList.indexOf(currency) < layoutBar.spinnerBase.getItems<String>().size) {
                    layoutBar.spinnerBase.tryToSelect(currencyList.indexOf(currency))
                } else {
                    layoutBar.spinnerBase.expand()
                }
            }
            layoutBar.ivBase.setBackgroundByName(currency.name)
        }
        calculatorFragmentAdapter.onItemLongClickListener = { currency, _ ->
            snacky(
                "${viewModel.getClickedItemRate(currency.name)} ${currency.getVariablesOneLine()}",
                setIcon = currency.name,
                isLong = false)
            true
        }
    }

    private fun updateBar(spinnerList: List<String>) = with(binding.layoutBar) {
        if (spinnerList.size < 2) {
            snacky(
                context?.getString(R.string.choose_at_least_two_currency),
                context?.getString(R.string.select)) {
                replaceFragment(SettingsFragment.newInstance(), true)
            }
            spinnerBase.setItems("")
            ivBase.setBackgroundByName("transparent")
        } else {
            spinnerBase.setItems(spinnerList)
            spinnerBase.tryToSelect(spinnerList.indexOf(viewModel.verifyCurrentBase(spinnerList).toString()))
            ivBase.setBackgroundByName(spinnerBase.text.toString())
        }
    }

    private fun setListeners() = with(binding.layoutBar) {
        spinnerBase.setOnItemSelectedListener { _, _, _, item ->
            viewModel.updateCurrentBase(item.toString())
            viewModel.calculateOutput(binding.txtInput.text.toString())
            ivBase.setBackgroundByName(item.toString())
        }
        layoutBar.setOnClickListener {
            if (spinnerBase.isActivated) {
                spinnerBase.collapse()
            } else {
                spinnerBase.expand()
            }
        }
    }

    private fun setKeyboard() = with(binding) {
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
            binding.txtInput.text = ""
            binding.layoutBar.txtOutput.text = ""
            binding.layoutBar.txtSymbol.text = ""
        }
        layoutKeyboard.btnDelete.setOnClickListener {
            if (binding.txtInput.text.toString() != "") {
                binding.txtInput.text = binding.txtInput.text.toString()
                    .substring(0, binding.txtInput.text.toString().length - 1)
            }
        }
    }

    private fun initData() = viewModel.apply {
        refreshData()

        if (loadResetData() && !getMainData().firstRun) {
            doAsync {
                AppDatabase.database.clearAllTables()
                resetFirstRun()
                uiThread {
                    persistResetData(false)
                    refreshData()
                    getCurrencies()
                }
            }
        } else {
            getCurrencies()
        }
    }
}
