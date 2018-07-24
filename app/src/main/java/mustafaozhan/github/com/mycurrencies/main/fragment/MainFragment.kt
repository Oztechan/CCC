package mustafaozhan.github.com.mycurrencies.main.fragment


import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.Toast
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.jakewharton.rxbinding2.widget.textChanges
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.layout_keyboard_content.*
import kotlinx.android.synthetic.main.layout_main_toolbar.*
import mustafaozhan.github.com.mycurrencies.R
import mustafaozhan.github.com.mycurrencies.base.BaseMvvmFragment
import mustafaozhan.github.com.mycurrencies.extensions.addText
import mustafaozhan.github.com.mycurrencies.extensions.getResult
import mustafaozhan.github.com.mycurrencies.extensions.reObserve
import mustafaozhan.github.com.mycurrencies.extensions.setBackgroundByName
import mustafaozhan.github.com.mycurrencies.main.fragment.adapter.CurrencyAdapter
import mustafaozhan.github.com.mycurrencies.tools.Currencies
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

/**
 * Created by Mustafa Ozhan on 2018-07-12.
 */
class MainFragment : BaseMvvmFragment<MainFragmentViewModel>() {

    companion object {
        fun newInstance(): MainFragment = MainFragment()
    }

    private val currencyAdapter: CurrencyAdapter by lazy { CurrencyAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        viewModel.loadPreferences()
        loading.bringToFront()
        loading.smoothToHide()
        setListeners()
        initRx()
        initLiveData()
        initRecycler()

    }

    private fun updateUi() {
        doAsync {
            viewModel.initData()
            uiThread {
                setSpinner()
            }
        }
    }

    override fun onResume() {
        viewModel.loadPreferences()
        updateUi()
        try {
            loadAd()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        super.onResume()
    }

    private fun setSpinner() {

        val spinnerList = ArrayList<String>()
        viewModel.currencyList.filter {
            it.isActive == 1
        }.forEach {
            spinnerList.add(it.name)
        }
        if (spinnerList.lastIndex < 1) {
            Toast.makeText(context, "Please Select at least 2 currency from Settings", Toast.LENGTH_SHORT).show()
            imgBase.setBackgroundByName("transparent")
            mSpinner.setItems("")
            currencyAdapter.refreshList(viewModel.currencyList, viewModel.mainData.currentBase, true)
        } else {
            mSpinner.setItems(spinnerList)
            if (viewModel.mainData.baseCurrency == Currencies.NULL && viewModel.currencyList.isNotEmpty()) {
                viewModel.mainData.baseCurrency = (Currencies.valueOf(viewModel.currencyList.filter { it.isActive == 1 }[0].name))
                mSpinner.selectedIndex = spinnerList.indexOf(viewModel.mainData.currentBase.toString())
                imgBase.setBackgroundByName(mSpinner.text.toString())
            } else {
                mSpinner.setItems(spinnerList)
                if (viewModel.mainData.baseCurrency == Currencies.NULL)
                    viewModel.mainData.baseCurrency = (Currencies.valueOf(viewModel.currencyList.filter { it.isActive == 1 }[0].name))
                viewModel.currencyList.filter {
                    it.isActive == 1
                }.forEach {
                    if (it.name == viewModel.mainData.baseCurrency.toString())
                        mSpinner.selectedIndex = spinnerList.indexOf(viewModel.mainData.baseCurrency.toString())
                }
            }
            imgBase.setBackgroundByName(mSpinner.text.toString())
            currencyAdapter.refreshList(viewModel.currencyList, viewModel.mainData.currentBase, true)
        }


    }

    @SuppressLint("SetTextI18n")
    private fun initRx() {
        txtMainToolbar.textChanges()
                .subscribe {
                    if (viewModel.currencyList.size > 1) {
                        loading.smoothToShow()
                        viewModel.getCurrencies()
                        viewModel.input = it.toString()
                        viewModel.output = viewModel.calculate(it.toString())

                        if (viewModel.output != "NaN" && viewModel.output != "")
                            txtResult.text = "=    ${viewModel.output}"
                        else
                            txtResult.text = ""
                    }
                }
    }

    private fun setListeners() {
        mSpinner.setOnItemSelectedListener { _, _, _, _ ->
            viewModel.mainData.currentBase=Currencies.valueOf(mSpinner.text.toString())
            imgBase.setBackgroundByName(mSpinner.text.toString())
            txtMainToolbar.text = txtMainToolbar.text//invoking rx in case of different currency selected
        }

        mConstraintLayout.setOnClickListener {
            if (mSpinner.isActivated)
                mSpinner.collapse()
            else
                mSpinner.expand()
        }

        btnSeven.setOnClickListener { txtMainToolbar.addText("7", viewModel.currencyList.size) }
        btnEight.setOnClickListener { txtMainToolbar.addText("8", viewModel.currencyList.size) }
        btnNine.setOnClickListener { txtMainToolbar.addText("9", viewModel.currencyList.size) }
        btnDivide.setOnClickListener { txtMainToolbar.addText("/", viewModel.currencyList.size) }
        btnFour.setOnClickListener { txtMainToolbar.addText("4", viewModel.currencyList.size) }
        btnFive.setOnClickListener { txtMainToolbar.addText("5", viewModel.currencyList.size) }
        btnSix.setOnClickListener { txtMainToolbar.addText("6", viewModel.currencyList.size) }
        btnMultiply.setOnClickListener { txtMainToolbar.addText("*", viewModel.currencyList.size) }
        btnOne.setOnClickListener { txtMainToolbar.addText("1", viewModel.currencyList.size) }
        btnTwo.setOnClickListener { txtMainToolbar.addText("2", viewModel.currencyList.size) }
        btnThree.setOnClickListener { txtMainToolbar.addText("3", viewModel.currencyList.size) }
        btnMinus.setOnClickListener { txtMainToolbar.addText("-", viewModel.currencyList.size) }
        btnDot.setOnClickListener { txtMainToolbar.addText(".", viewModel.currencyList.size) }
        btnZero.setOnClickListener { txtMainToolbar.addText("0", viewModel.currencyList.size) }
        btnPercent.setOnClickListener { txtMainToolbar.addText("%", viewModel.currencyList.size) }
        btnPlus.setOnClickListener { txtMainToolbar.addText("+", viewModel.currencyList.size) }

        btnDoubleZero.setOnClickListener { txtMainToolbar.addText("000", viewModel.currencyList.size) }
        btnAc.setOnClickListener {
            txtMainToolbar.text = ""
            txtResult.text = ""
        }

        btnDelete.setOnClickListener {
            if (txtMainToolbar.text.toString() != "")
                txtMainToolbar.text = txtMainToolbar.text.toString().substring(0, txtMainToolbar.text.toString().length - 1)
        }
    }

    private fun initLiveData() {
        viewModel.currenciesLiveData.reObserve(this, Observer {
            it.let {
                val tempRate = it
                viewModel.currencyList.forEach {
                    it.rate = getResult(it.name, viewModel.output, tempRate)
                }
                currencyAdapter.refreshList(viewModel.currencyList, viewModel.mainData.currentBase, true)
                loading.smoothToHide()
            }
        })
    }


    private fun initRecycler() {
        context?.let {
            mRecViewCurrency.layoutManager = LinearLayoutManager(it)
            mRecViewCurrency.adapter = currencyAdapter       }
    }

    override fun getViewModelClass(): Class<MainFragmentViewModel> = MainFragmentViewModel::class.java

    override fun getLayoutResId(): Int = R.layout.fragment_main

    private fun loadAd() {
        MobileAds.initialize(context, resources.getString(R.string.banner_ad_unit_id_main))
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)
    }

    override fun onPause() {
        viewModel.savePreferences()
        super.onPause()
    }

}