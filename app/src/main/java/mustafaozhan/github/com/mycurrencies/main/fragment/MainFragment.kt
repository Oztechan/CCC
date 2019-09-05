package mustafaozhan.github.com.mycurrencies.main.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.jakewharton.rxbinding2.widget.textChanges
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.fragment_main.ad_view
import kotlinx.android.synthetic.main.fragment_main.layout_bar
import kotlinx.android.synthetic.main.fragment_main.loading_view
import kotlinx.android.synthetic.main.fragment_main.recycler_view_main
import kotlinx.android.synthetic.main.item_currency.view.txt_amount
import kotlinx.android.synthetic.main.layout_bar.iv_base
import kotlinx.android.synthetic.main.layout_bar.spinner_base
import kotlinx.android.synthetic.main.layout_bar.txt_result
import kotlinx.android.synthetic.main.layout_bar.txt_symbol
import kotlinx.android.synthetic.main.layout_keyboard_content.btn_ac
import kotlinx.android.synthetic.main.layout_keyboard_content.btn_delete
import kotlinx.android.synthetic.main.layout_keyboard_content.btn_divide
import kotlinx.android.synthetic.main.layout_keyboard_content.btn_dot
import kotlinx.android.synthetic.main.layout_keyboard_content.btn_eight
import kotlinx.android.synthetic.main.layout_keyboard_content.btn_five
import kotlinx.android.synthetic.main.layout_keyboard_content.btn_four
import kotlinx.android.synthetic.main.layout_keyboard_content.btn_minus
import kotlinx.android.synthetic.main.layout_keyboard_content.btn_multiply
import kotlinx.android.synthetic.main.layout_keyboard_content.btn_nine
import kotlinx.android.synthetic.main.layout_keyboard_content.btn_one
import kotlinx.android.synthetic.main.layout_keyboard_content.btn_percent
import kotlinx.android.synthetic.main.layout_keyboard_content.btn_plus
import kotlinx.android.synthetic.main.layout_keyboard_content.btn_seven
import kotlinx.android.synthetic.main.layout_keyboard_content.btn_six
import kotlinx.android.synthetic.main.layout_keyboard_content.btn_three
import kotlinx.android.synthetic.main.layout_keyboard_content.btn_triple_zero
import kotlinx.android.synthetic.main.layout_keyboard_content.btn_two
import kotlinx.android.synthetic.main.layout_keyboard_content.btn_zero
import kotlinx.android.synthetic.main.layout_main_toolbar.txt_main_toolbar
import mustafaozhan.github.com.mycurrencies.R
import mustafaozhan.github.com.mycurrencies.base.BaseMvvmFragment
import mustafaozhan.github.com.mycurrencies.extensions.addText
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
        const val MAX_DIGIT = 12
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
        txt_main_toolbar.textChanges()
            .subscribe { txt ->
                loading_view.smoothToShow()
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
                currencyList.forEach { it.rate = viewModel.calculateResultByCurrency(it.name, viewModel.output, rates) }
                rates?.let {
                    currencyAdapter.refreshList(currencyList, viewModel.mainData.currentBase)
                } ?: run {
                    if (currencyList.size > 1) {
                        snacky(getString(R.string.rate_not_available_offline), getString(R.string.change)) {
                            spinner_base.expand()
                        }
                    }
                    currencyAdapter.refreshList(mutableListOf(), viewModel.mainData.currentBase)
                }
            }
            loading_view.smoothToHide()
        })
        viewModel.currencyListLiveData.reObserve(this, Observer { currencyList ->
            currencyList?.let {
                updateBar(currencyList.map { it.name })
                currencyAdapter.refreshList(currencyList, viewModel.mainData.currentBase)
                loading_view.smoothToHide()
            }
        })
    }

    private fun initViews() {
        loading_view.bringToFront()
        context?.let { ctx ->
            recycler_view_main.layoutManager = LinearLayoutManager(ctx)
            recycler_view_main.adapter = currencyAdapter
        }
        currencyAdapter.onItemClickListener = { currency, itemView: View, _: Int ->
            txt_main_toolbar.text = itemView.txt_amount.text.toString().replace(" ", "")
            viewModel.updateCurrentBase(currency.name)
            viewModel.getCurrencies()
            viewModel.calculateOutput(itemView.txt_amount.text.toString().replace(" ", ""))
            getOutputText()
            viewModel.currencyListLiveData.value?.let { currencyList ->
                if (currencyList.indexOf(currency) < spinner_base.getItems<String>().size) {
                    spinner_base.selectedIndex = currencyList.indexOf(currency)
                } else {
                    spinner_base.expand()
                }
            }
            iv_base.setBackgroundByName(currency.name)
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
        txt_symbol.text = viewModel.getCurrencyByName(
            viewModel.mainData.currentBase.toString()
        )?.symbol ?: ""
        when {
            viewModel.output.isEmpty() -> {
                txt_result.text = ""
                txt_symbol.text = ""
            }
            else -> txt_result.text = "=  ${viewModel.output} "
        }
    }

    private fun updateBar(spinnerList: List<String>) =
        if (spinnerList.size < 2) {
            snacky(
                context?.getString(R.string.choose_at_least_two_currency),
                context?.getString(R.string.select)) {
                getBaseActivity()?.replaceFragment(SettingsFragment.newInstance(), true)
            }
            spinner_base.setItems("")
            iv_base.setBackgroundByName("transparent")
        } else {
            spinner_base.setItems(spinnerList)
            spinner_base.selectedIndex = spinnerList.indexOf(viewModel.verifyCurrentBase(spinnerList).toString())
            iv_base.setBackgroundByName(spinner_base.text.toString())
        }

    private fun setListeners() {
        spinner_base.setOnItemSelectedListener { _, _, _, item ->
            viewModel.apply {
                updateCurrentBase(item.toString())
                getCurrencies()
            }
            getOutputText()
            iv_base.setBackgroundByName(item.toString())
        }
        layout_bar.setOnClickListener {
            if (spinner_base.isActivated) {
                spinner_base.collapse()
            } else {
                spinner_base.expand()
            }
        }
    }

    private fun setKeyboard() {
        btn_seven.setOnClickListener { keyboardPressed("7") }
        btn_eight.setOnClickListener { keyboardPressed("8") }
        btn_nine.setOnClickListener { keyboardPressed("9") }
        btn_divide.setOnClickListener { keyboardPressed("/") }
        btn_four.setOnClickListener { keyboardPressed("4") }
        btn_five.setOnClickListener { keyboardPressed("5") }
        btn_six.setOnClickListener { keyboardPressed("6") }
        btn_multiply.setOnClickListener { keyboardPressed("*") }
        btn_one.setOnClickListener { keyboardPressed("1") }
        btn_two.setOnClickListener { keyboardPressed("2") }
        btn_three.setOnClickListener { keyboardPressed("3") }
        btn_minus.setOnClickListener { keyboardPressed("-") }
        btn_dot.setOnClickListener { keyboardPressed(".") }
        btn_zero.setOnClickListener { keyboardPressed("0") }
        btn_percent.setOnClickListener { keyboardPressed("%") }
        btn_plus.setOnClickListener { keyboardPressed("+") }
        btn_triple_zero.setOnClickListener { keyboardPressed("000") }
        btn_ac.setOnClickListener {
            txt_main_toolbar.text = ""
            txt_result.text = ""
            txt_symbol.text = ""
        }
        btn_delete.setOnClickListener {
            if (txt_main_toolbar.text.toString() != "") {
                txt_main_toolbar.text = txt_main_toolbar.text.toString()
                    .substring(0, txt_main_toolbar.text.toString().length - 1)
            }
        }
    }

    private fun keyboardPressed(txt: String) =
        if (viewModel.output.length < MAX_DIGIT) {
            txt_main_toolbar.addText(txt)
        } else {
            snacky(getString(R.string.max_input), isLong = false)
        }

    override fun onPause() {
        viewModel.savePreferences()
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        initData()
        ad_view.loadAd(R.string.banner_ad_unit_id_main)
    }

    private fun initData() {
        loading_view.smoothToShow()
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
