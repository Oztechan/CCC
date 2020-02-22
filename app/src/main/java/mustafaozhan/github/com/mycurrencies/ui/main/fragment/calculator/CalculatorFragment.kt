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
import mustafaozhan.github.com.mycurrencies.data.room.AppDatabase
import mustafaozhan.github.com.mycurrencies.databinding.FragmentCalculatorBinding
import mustafaozhan.github.com.mycurrencies.function.extension.addText
import mustafaozhan.github.com.mycurrencies.function.extension.checkAd
import mustafaozhan.github.com.mycurrencies.function.extension.dropDecimal
import mustafaozhan.github.com.mycurrencies.function.extension.reObserve
import mustafaozhan.github.com.mycurrencies.function.extension.replaceNonStandardDigits
import mustafaozhan.github.com.mycurrencies.function.extension.setBackgroundByName
import mustafaozhan.github.com.mycurrencies.function.extension.tryToSelect
import mustafaozhan.github.com.mycurrencies.function.scope.whether
import mustafaozhan.github.com.mycurrencies.function.scope.whetherNot
import mustafaozhan.github.com.mycurrencies.model.Rates
import mustafaozhan.github.com.mycurrencies.tool.Toasty
import mustafaozhan.github.com.mycurrencies.tool.showSnacky
import mustafaozhan.github.com.mycurrencies.ui.main.fragment.settings.SettingsFragment
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import timber.log.Timber

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
        binding.adView.checkAd(R.string.banner_ad_unit_id_main, viewModel.isRewardExpired)
    }

    private fun setRx() {
        binding.txtInput.textChanges()
            .map { it.toString() }
            .subscribe(
                { viewModel.calculateOutput(it) },
                { Timber.e(it) }
            )
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
                        Toasty.showToasty(requireContext(), getString(R.string.database_success_with_date, it))
                    } ?: run {
                        Toasty.showToasty(requireContext(), getString(R.string.database_success))
                    }
                }
                CalculatorViewState.Error -> {
                    viewModel.currencyListLiveData.value?.size
                        ?.whether { it > 1 }
                        ?.let {
                            showSnacky(
                                view,
                                getString(R.string.rate_not_available_offline),
                                getString(R.string.change)
                            ) { binding.layoutBar.spinnerBase.expand() }
                        }

                    calculatorFragmentAdapter.refreshList(mutableListOf(), viewModel.mainData.currentBase)
                    binding.loadingView.smoothToHide()
                }
                is CalculatorViewState.MaximumInput -> {
                    Toasty.showToasty(requireContext(), getString(R.string.max_input))
                    binding.txtInput.text = calculatorViewState.input.dropLast(1)
                    binding.loadingView.smoothToHide()
                }
                CalculatorViewState.FewCurrency -> {
                    showSnacky(
                        view,
                        getString(R.string.choose_at_least_two_currency),
                        getString(R.string.select)
                    ) { replaceFragment(SettingsFragment.newInstance(), true) }

                    calculatorFragmentAdapter.refreshList(mutableListOf(), viewModel.mainData.currentBase)
                    binding.loadingView.smoothToHide()
                }
            }
        })

    private fun clearScreen() = with(binding) {
        txtInput.text = ""
        layoutBar.txtOutput.text = ""
        layoutBar.txtSymbol.text = ""
    }

    @SuppressLint("SetTextI18n")
    private fun initLiveData() {
        viewModel.currencyListLiveData.reObserve(this, Observer { currencyList ->
            currencyList?.let {
                updateBar(currencyList.map { it.name })
                calculatorFragmentAdapter.refreshList(currencyList, viewModel.mainData.currentBase)
                binding.loadingView.smoothToHide()
            }
        })

        viewModel.outputLiveData.reObserve(this, Observer { output ->
            with(binding.layoutBar) {
                txtSymbol.text = viewModel.getCurrencyByName(
                    viewModel.mainData.currentBase.toString()
                )?.symbol

                output.toString()
                    .whetherNot { isEmpty() }
                    ?.apply { txtOutput.text = "=  ${replaceNonStandardDigits()} " }
                    ?: run { clearScreen() }
            }
        })
    }

    private fun onSearchSuccess(rates: Rates) {
        viewModel.currencyListLiveData.value?.let { currencyList ->
            currencyList.forEach { it.rate = viewModel.calculateResultByCurrency(it.name, rates) }
            calculatorFragmentAdapter.refreshList(currencyList, viewModel.mainData.currentBase)
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

            viewModel.currencyListLiveData
                .value
                ?.whether { indexOf(currency) < layoutBar.spinnerBase.getItems<String>().size }
                ?.apply { layoutBar.spinnerBase.tryToSelect(indexOf(currency)) }
                ?: run { layoutBar.spinnerBase.expand() }

            layoutBar.ivBase.setBackgroundByName(currency.name)
        }
        calculatorFragmentAdapter.onItemLongClickListener = { currency, _ ->
            showSnacky(
                view,
                "${viewModel.getClickedItemRate(currency.name)} ${currency.getVariablesOneLine()}",
                setIcon = currency.name,
                isLong = false
            )
            true
        }
    }

    private fun updateBar(spinnerList: List<String>) = with(binding.layoutBar) {
        if (spinnerList.size < 2) {
            showSnacky(
                view,
                getString(R.string.choose_at_least_two_currency),
                getString(R.string.select)) {
                replaceFragment(SettingsFragment.newInstance(), true)
            }
            clearScreen()
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
            with(spinnerBase) {
                whether { isActivated }
                    ?.apply { collapse() }
                    ?: run { expand() }
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
        layoutKeyboard.btnAc.setOnClickListener { clearScreen() }
        layoutKeyboard.btnDelete.setOnClickListener {
            binding.txtInput
                .text
                .toString()
                .whetherNot { isEmpty() }
                ?.apply { binding.txtInput.text = substring(0, length - 1) }
        }
    }

    private fun initData() = viewModel.apply {
        refreshData()

        if (loadResetData() && !mainData.firstRun) {
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
