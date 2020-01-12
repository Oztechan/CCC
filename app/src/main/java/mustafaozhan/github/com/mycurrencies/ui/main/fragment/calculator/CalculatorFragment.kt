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
    override fun bind() {
        binding = FragmentCalculatorBinding.inflate(layoutInflater)
    }

    companion object {
        fun newInstance(): CalculatorFragment = CalculatorFragment()
        const val MAX_DIGIT = 12
    }

    override fun getLayoutResId(): Int = R.layout.fragment_calculator

    private val calculatorFragmentAdapter: CalculatorFragmentAdapter by lazy { CalculatorFragmentAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        initViews()
        setListeners()
        setKeyboard()
        setRx()
        initLiveData()
    }

    private fun initToolbar() {
        getBaseActivity()?.setSupportActionBar(binding.appBarLayout.toolbarFragmentMain)
    }

    private fun setRx() {
        binding.appBarLayout.txtMainToolbar.textChanges()
            .subscribe({ txt ->
                viewModel.currencyListLiveData.value?.let { currencyList ->
                    if (currencyList.size > 1) {
                        viewModel.calculateOutput(txt.toString())
                        viewModel.getCurrencies()
                        getOutputText()
                    } else {
                        snacky(getString(R.string.choose_at_least_two_currency), getString(R.string.select)) {
                            getBaseActivity()?.replaceFragment(SettingsFragment.newInstance(), true)
                        }
                    }
                }
            }, {
                logException(it)
            })
            .addTo(compositeDisposable)
    }

    private fun initLiveData() {
        viewModel.calculatorViewStateLiveData.reObserve(this, Observer { calculatorViewState ->
            when (calculatorViewState) {
                CalculatorViewState.Loading -> binding.loadingView.smoothToShow()
                is CalculatorViewState.BackEndSuccess -> onSearchSuccess(calculatorViewState.rates)
                is CalculatorViewState.DataBaseSuccess -> {
                    onSearchSuccess(calculatorViewState.rates)
                    toasty(getString(R.string.database_success))
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
            }
        })

        viewModel.currencyListLiveData.reObserve(this, Observer { currencyList ->
            currencyList?.let {
                updateBar(currencyList.map { it.name })
                calculatorFragmentAdapter.refreshList(currencyList, viewModel.getMainData().currentBase)
                binding.loadingView.smoothToHide()
            }
        })
    }

    private fun onSearchSuccess(rates: Rates) {
        viewModel.currencyListLiveData.value?.let { currencyList ->
            currencyList.forEach { it.rate = viewModel.calculateResultByCurrency(it.name, viewModel.output, rates) }
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
            appBarLayout.txtMainToolbar.text = itemView.txt_amount.text.toString().replace(" ", "")
            viewModel.updateCurrentBase(currency.name)
            viewModel.getCurrencies()
            viewModel.calculateOutput(itemView.txt_amount.text.toString().replace(" ", ""))
            getOutputText()
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

    @SuppressLint("SetTextI18n")
    private fun getOutputText() = with(binding.layoutBar) {
        txtSymbol.text = viewModel.getCurrencyByName(
            viewModel.getMainData().currentBase.toString()
        )?.symbol

        if (viewModel.output.isEmpty()) {
            txtResult.text = ""
            txtSymbol.text = ""
        } else {
            txtResult.text = "=  ${viewModel.output.replaceNonStandardDigits()} "
        }
    }

    private fun updateBar(spinnerList: List<String>) = with(binding.layoutBar) {
        if (spinnerList.size < 2) {
            snacky(
                context?.getString(R.string.choose_at_least_two_currency),
                context?.getString(R.string.select)) {
                getBaseActivity()?.replaceFragment(SettingsFragment.newInstance(), true)
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
            viewModel.apply {
                updateCurrentBase(item.toString())
                getCurrencies()
            }
            getOutputText()
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

    private fun setKeyboard() = with(binding.layoutKeyboard) {
        btnSeven.setOnClickListener { keyboardPressed("7") }
        btnEight.setOnClickListener { keyboardPressed("8") }
        btnNine.setOnClickListener { keyboardPressed("9") }
        btnDivide.setOnClickListener { keyboardPressed("/") }
        btnFour.setOnClickListener { keyboardPressed("4") }
        btnFive.setOnClickListener { keyboardPressed("5") }
        btnSix.setOnClickListener { keyboardPressed("6") }
        btnMultiply.setOnClickListener { keyboardPressed("*") }
        btnOne.setOnClickListener { keyboardPressed("1") }
        btnTwo.setOnClickListener { keyboardPressed("2") }
        btnThree.setOnClickListener { keyboardPressed("3") }
        btnMinus.setOnClickListener { keyboardPressed("-") }
        btnDot.setOnClickListener { keyboardPressed(".") }
        btnZero.setOnClickListener { keyboardPressed("0") }
        btnPercent.setOnClickListener { keyboardPressed("%") }
        btnPlus.setOnClickListener { keyboardPressed("+") }
        btnTripleZero.setOnClickListener { keyboardPressed("000") }
        btnZero.setOnClickListener { keyboardPressed("0") }
        btnAc.setOnClickListener {
            binding.appBarLayout.txtMainToolbar.text = ""
            binding.layoutBar.txtResult.text = ""
            binding.layoutBar.txtSymbol.text = ""
        }
        btnDelete.setOnClickListener {
            if (binding.appBarLayout.txtMainToolbar.text.toString() != "") {
                binding.appBarLayout.txtMainToolbar.text = binding.appBarLayout.txtMainToolbar.text.toString()
                    .substring(0, binding.appBarLayout.txtMainToolbar.text.toString().length - 1)
            }
        }
    }

    private fun keyboardPressed(txt: String) =
        if (viewModel.output.length < MAX_DIGIT) {
            binding.appBarLayout.txtMainToolbar.addText(txt)
        } else {
            toasty(getString(R.string.max_input))
        }

    override fun onResume() {
        super.onResume()
        initData()
        binding.adView.checkAd(R.string.banner_ad_unit_id_main, viewModel.isRewardExpired())
    }

    private fun initData() {
        binding.loadingView.smoothToShow()
        viewModel.apply {
            rates = null
            refreshData()

            if (loadResetData() && !getMainData().firstRun) {
                doAsync {
                    AppDatabase.database.clearAllTables()
                    viewModel.resetFirstRun()
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
}
