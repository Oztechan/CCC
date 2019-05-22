package mustafaozhan.github.com.mycurrencies.main.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.jakewharton.rxbinding2.widget.textChanges
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.fragment_main.adView
import kotlinx.android.synthetic.main.fragment_main.layoutBar
import kotlinx.android.synthetic.main.fragment_main.loading
import kotlinx.android.synthetic.main.fragment_main.mRecViewCurrency
import kotlinx.android.synthetic.main.item_currency.view.txtAmount
import kotlinx.android.synthetic.main.layout_bar.imgBase
import kotlinx.android.synthetic.main.layout_bar.mSpinner
import kotlinx.android.synthetic.main.layout_bar.txtResult
import kotlinx.android.synthetic.main.layout_bar.txtResultSymbol
import kotlinx.android.synthetic.main.layout_keyboard_content.btnAc
import kotlinx.android.synthetic.main.layout_keyboard_content.btnDelete
import kotlinx.android.synthetic.main.layout_keyboard_content.btnDivide
import kotlinx.android.synthetic.main.layout_keyboard_content.btnDot
import kotlinx.android.synthetic.main.layout_keyboard_content.btnDoubleZero
import kotlinx.android.synthetic.main.layout_keyboard_content.btnEight
import kotlinx.android.synthetic.main.layout_keyboard_content.btnFive
import kotlinx.android.synthetic.main.layout_keyboard_content.btnFour
import kotlinx.android.synthetic.main.layout_keyboard_content.btnMinus
import kotlinx.android.synthetic.main.layout_keyboard_content.btnMultiply
import kotlinx.android.synthetic.main.layout_keyboard_content.btnNine
import kotlinx.android.synthetic.main.layout_keyboard_content.btnOne
import kotlinx.android.synthetic.main.layout_keyboard_content.btnPercent
import kotlinx.android.synthetic.main.layout_keyboard_content.btnPlus
import kotlinx.android.synthetic.main.layout_keyboard_content.btnSeven
import kotlinx.android.synthetic.main.layout_keyboard_content.btnSix
import kotlinx.android.synthetic.main.layout_keyboard_content.btnThree
import kotlinx.android.synthetic.main.layout_keyboard_content.btnTwo
import kotlinx.android.synthetic.main.layout_keyboard_content.btnZero
import kotlinx.android.synthetic.main.layout_main_toolbar.txtMainToolbar
import mustafaozhan.github.com.mycurrencies.R
import mustafaozhan.github.com.mycurrencies.base.BaseMvvmFragment
import mustafaozhan.github.com.mycurrencies.extensions.addText
import mustafaozhan.github.com.mycurrencies.extensions.calculateResultByCurrency
import mustafaozhan.github.com.mycurrencies.extensions.loadAd
import mustafaozhan.github.com.mycurrencies.extensions.reObserve
import mustafaozhan.github.com.mycurrencies.extensions.setBackgroundByName
import mustafaozhan.github.com.mycurrencies.main.fragment.adapter.CurrencyAdapter
import mustafaozhan.github.com.mycurrencies.room.AppDatabase
import mustafaozhan.github.com.mycurrencies.settings.SettingsFragment
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

/**
 * Created by Mustafa Ozhan on 2018-07-12.
 */
@Suppress("TooManyFunctions")
class MainFragment : BaseMvvmFragment<MainFragmentViewModel>() {

    companion object {
        fun newInstance(): MainFragment = MainFragment()
    }

    override fun getViewModelClass(): Class<MainFragmentViewModel> =
        MainFragmentViewModel::class.java

    override fun getLayoutResId(): Int = R.layout.fragment_main

    private val currencyAdapter: CurrencyAdapter by lazy { CurrencyAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        initViews()
        setListeners()
        setKeyboard()
        setRx()
        initLiveData()
    }

    private fun setRx() {
        txtMainToolbar.textChanges()
            .subscribe { txt ->
                loading.smoothToShow()
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
            }.addTo(compositeDisposable)
    }

    private fun initLiveData() {
        viewModel.ratesLiveData.reObserve(this, Observer { rates ->
            viewModel.currencyListLiveData.value?.let { currencyList ->
                currencyList.forEach { it.rate = calculateResultByCurrency(it.name, viewModel.output, rates) }
                rates?.let {
                    currencyAdapter.refreshList(currencyList, viewModel.mainData.currentBase)
                } ?: run {
                    if (currencyList.size > 1) {
                        snacky(getString(R.string.rate_not_available_offline), getString(R.string.change)) {
                            mSpinner.expand()
                        }
                    }
                    currencyAdapter.refreshList(mutableListOf(), viewModel.mainData.currentBase)
                }
            }
            loading.smoothToHide()
        })
        viewModel.currencyListLiveData.reObserve(this, Observer { currencyList ->
            currencyList?.let {
                updateBar(currencyList.map { it.name })
                currencyAdapter.refreshList(currencyList, viewModel.mainData.currentBase)
                loading.smoothToHide()
            }
        })
    }

    private fun initViews() {
        loading.bringToFront()
        context?.let { ctx ->
            mRecViewCurrency.layoutManager = LinearLayoutManager(ctx)
            mRecViewCurrency.adapter = currencyAdapter
        }
        currencyAdapter.onItemClickListener = { currency, itemView: View, _: Int ->
            txtMainToolbar.text = itemView.txtAmount.text.toString().replace(" ", "")
            viewModel.updateCurrentBase(currency.name)
            viewModel.getCurrencies()
            viewModel.calculateOutput(itemView.txtAmount.text.toString().replace(" ", ""))
            getOutputText()
            viewModel.currencyListLiveData.value?.let { currencyList ->
                if (currencyList.indexOf(currency) < mSpinner.getItems<String>().size) {
                    mSpinner.selectedIndex = currencyList.indexOf(currency)
                } else {
                    mSpinner.expand()
                }
            }
            imgBase.setBackgroundByName(currency.name)
        }
        currencyAdapter.onItemLongClickListener = { currency, _ ->
            snacky(
                "${viewModel.getClickedItemRate(currency.name)} ${currency.getVariablesOneLine()}",
                setIcon = currency.name,
                isLong = false)
            true
        }
    }

    @SuppressLint("SetTextI18n")
    private fun getOutputText() {
        txtResultSymbol.text = viewModel.getCurrencyByName(
            viewModel.mainData.currentBase.toString()
        )?.symbol ?: ""
        when {
            viewModel.output.isEmpty() -> {
                txtResult.text = ""
                txtResultSymbol.text = ""
            }
            else -> txtResult.text = "=  ${viewModel.output} "
        }
    }

    private fun updateBar(spinnerList: List<String>) =
        if (spinnerList.size < 2) {
            snacky(
                context?.getString(R.string.choose_at_least_two_currency),
                context?.getString(R.string.select)) {
                getBaseActivity()?.replaceFragment(SettingsFragment.newInstance(), true)
            }
            mSpinner.setItems("")
            imgBase.setBackgroundByName("transparent")
        } else {
            mSpinner.setItems(spinnerList)
            mSpinner.selectedIndex = spinnerList.indexOf(viewModel.verifyCurrentBase(spinnerList).toString())
            imgBase.setBackgroundByName(mSpinner.text.toString())
        }

    private fun setListeners() {
        mSpinner.setOnItemSelectedListener { _, _, _, item ->
            viewModel.apply {
                updateCurrentBase(item.toString())
                getCurrencies()
            }
            getOutputText()
            imgBase.setBackgroundByName(item.toString())
        }
        layoutBar.setOnClickListener {
            if (mSpinner.isActivated) {
                mSpinner.collapse()
            } else {
                mSpinner.expand()
            }
        }
    }

    private fun setKeyboard() {
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
        btnDoubleZero.setOnClickListener { keyboardPressed("000") }
        btnAc.setOnClickListener {
            txtMainToolbar.text = ""
            txtResult.text = ""
            txtResultSymbol.text = ""
        }
        btnDelete.setOnClickListener {
            if (txtMainToolbar.text.toString() != "") {
                txtMainToolbar.text = txtMainToolbar.text.toString()
                    .substring(0, txtMainToolbar.text.toString().length - 1)
            }
        }
    }

    private fun keyboardPressed(txt: String) {
        if (!txtMainToolbar.addText(txt)) {
            snacky(getString(R.string.max_input), isLong = false)
        }
    }

    override fun onPause() {
        viewModel.savePreferences()
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        initData()
        adView.loadAd(R.string.banner_ad_unit_id_main)
    }

    private fun initData() {
        loading.smoothToShow()
        viewModel.apply {
            rates = null
            refreshData()
            if (loadResetData() && !mainData.firstRun) {
                doAsync {
                    AppDatabase.database.clearAllTables()
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