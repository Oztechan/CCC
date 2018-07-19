package mustafaozhan.github.com.mycurrencies.main.fragment


import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.jakewharton.rxbinding2.widget.textChanges
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.layout_keyboard_content.*
import kotlinx.android.synthetic.main.layout_main_toolbar.*
import mustafaozhan.github.com.mycurrencies.R
import mustafaozhan.github.com.mycurrencies.base.BaseMvvmFragment
import mustafaozhan.github.com.mycurrencies.main.fragment.adapter.CurrencyAdapter
import mustafaozhan.github.com.mycurrencies.tools.*

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
        loading.bringToFront()
        setListeners()
        initRx()
        initLiveData()
        initRecycler()

    }

    override fun onResume() {

        viewModel.initData()
        setSpinner()
        super.onResume()
    }

    private fun setSpinner() {

        val spinnerList = ArrayList<String>()
        spinnerList.clear()
        viewModel.currencyList.filter {
            it.isActive == 1
        }.forEach {
            spinnerList.add(it.name)
        }
        if (spinnerList.toList().lastIndex < 1) {
            mSpinner.setItems("Select at least two currency from Settings")
            imgBase.setBackgroundByName("transparent")
            currencyAdapter.refreshList(viewModel.currencyList, viewModel.getCurrentBase(), viewModel.getBaseCurrency(), true)
        } else {
            mSpinner.setItems(spinnerList.toList())
            mSpinner.selectedIndex = spinnerList.indexOf(viewModel.getBaseCurrency().toString())
            imgBase.setBackgroundByName(mSpinner.text.toString())
        }

    }

    @SuppressLint("SetTextI18n")
    private fun initRx() {
        txtMainToolbar.textChanges()
                .subscribe {
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

    private fun setListeners() {
        mSpinner.setOnItemSelectedListener { _, _, _, _ ->
            viewModel.setCurrentBase(mSpinner.text.toString())
            imgBase.setBackgroundByName(mSpinner.text.toString())
            txtMainToolbar.text = txtMainToolbar.text//invoking rx in case of different currency selected
        }

        mConstraintLayout.setOnClickListener {
            if (mSpinner.isActivated)
                mSpinner.collapse()
            else
                mSpinner.expand()
        }

        btnSeven.setOnClickListener { txtMainToolbar.addText("7") }
        btnEight.setOnClickListener { txtMainToolbar.addText("8") }
        btnNine.setOnClickListener { txtMainToolbar.addText("9") }
        btnDivide.setOnClickListener { txtMainToolbar.addText("/") }
        btnFour.setOnClickListener { txtMainToolbar.addText("4") }
        btnFive.setOnClickListener { txtMainToolbar.addText("5") }
        btnSix.setOnClickListener { txtMainToolbar.addText("6") }
        btnMultiply.setOnClickListener { txtMainToolbar.addText("*") }
        btnOne.setOnClickListener { txtMainToolbar.addText("1") }
        btnTwo.setOnClickListener { txtMainToolbar.addText("2") }
        btnThree.setOnClickListener { txtMainToolbar.addText("3") }
        btnMinus.setOnClickListener { txtMainToolbar.addText("-") }
        btnDot.setOnClickListener { txtMainToolbar.addText(".") }
        btnZero.setOnClickListener { txtMainToolbar.addText("0") }
        btnPercent.setOnClickListener { txtMainToolbar.addText("%") }
        btnPlus.setOnClickListener { txtMainToolbar.addText("+") }

        btnDoubleZero.setOnClickListener { txtMainToolbar.addText("000") }
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
        viewModel.ratesLiveData.reObserve(this, Observer {
            it.let {
                val tempRate = it
                viewModel.currencyList.forEach {
                    it.rate = getResult(it.name, viewModel.output, tempRate)
                }
                viewModel.checkList()
                currencyAdapter.refreshList(viewModel.currencyList, viewModel.getCurrentBase(), viewModel.getBaseCurrency(), true)
                loading.smoothToHide()
            }
        })
    }


    private fun initRecycler() {
        context?.let {
            mRecViewCurrency.layoutManager = LinearLayoutManager(it)
            mRecViewCurrency.adapter = currencyAdapter
//            currencyAdapter.onItemSelectedListener = { rates: Rates, _, _ -> replaceFragment(SelectModeFragment.newInstance(), true) }
        }
    }

    override fun getViewModelClass(): Class<MainFragmentViewModel> = MainFragmentViewModel::class.java

    override fun getLayoutResId(): Int = R.layout.fragment_main

}