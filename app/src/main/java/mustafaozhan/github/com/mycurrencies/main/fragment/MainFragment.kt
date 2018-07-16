package mustafaozhan.github.com.mycurrencies.main.fragment

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import com.jakewharton.rxbinding2.widget.textChanges
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.layout_keyboard_content.*
import kotlinx.android.synthetic.main.layout_main_toolbar.*
import mustafaozhan.github.com.mycurrencies.R
import mustafaozhan.github.com.mycurrencies.base.BaseMvvmFragment
import mustafaozhan.github.com.mycurrencies.main.fragment.adapter.RatesAdapter
import mustafaozhan.github.com.mycurrencies.room.model.Currency
import mustafaozhan.github.com.mycurrencies.tools.addText
import mustafaozhan.github.com.mycurrencies.tools.getResult
import mustafaozhan.github.com.mycurrencies.tools.reObserve
import mustafaozhan.github.com.mycurrencies.tools.setBackgroundByName
import kotlin.math.log

/**
 * Created by Mustafa Ozhan on 2018-07-12.
 */
class MainFragment : BaseMvvmFragment<MainFragmentViewModel>() {

    companion object {
        fun newInstance(): MainFragment = MainFragment()
    }

    private val ratesAdapter: RatesAdapter by lazy { RatesAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        viewModel.initData()
        setListeners()
        initRecycler()
        initLiveData()
        initRx()
        viewModel.getCurrencies()
    }

    private fun initRx() {
        txtMainToolbar.textChanges()
                .subscribe {
                    viewModel.input=it.toString()
                }
    }

    private fun setListeners() {
        mSpinner.setOnItemSelectedListener { _, _, _, _ ->
            //            refreshEditText()
            imgBase.setBackgroundByName(mSpinner.text.toString())
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
                //                viewModel.currencyList.add(
//                        Currency("UR", getResult(Currencies.EUR, "10", it!!)))
//                ratesAdapter.refreshList(viewModel.currencyList)
            }
        })
    }


    private fun initRecycler() {
        context?.let {
            mRecViewCurrency.layoutManager = LinearLayoutManager(it)
            mRecViewCurrency.adapter = ratesAdapter
//            ratesAdapter.onItemSelectedListener = { rates: Rates, _, _ -> replaceFragment(SelectModeFragment.newInstance(), true) }
        }
    }

    override fun getViewModelClass(): Class<MainFragmentViewModel> = MainFragmentViewModel::class.java

    override fun getLayoutResId(): Int = R.layout.fragment_main

}