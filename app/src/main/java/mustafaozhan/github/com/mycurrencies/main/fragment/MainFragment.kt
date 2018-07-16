package mustafaozhan.github.com.mycurrencies.main.fragment

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import kotlinx.android.synthetic.main.fragment_main.*
import mustafaozhan.github.com.mycurrencies.R
import mustafaozhan.github.com.mycurrencies.base.BaseMvvmFragment
import mustafaozhan.github.com.mycurrencies.main.fragment.adapter.RatesAdapter
import mustafaozhan.github.com.mycurrencies.room.model.Currency
import mustafaozhan.github.com.mycurrencies.tools.Currencies
import mustafaozhan.github.com.mycurrencies.tools.getResult
import mustafaozhan.github.com.mycurrencies.tools.reObserve

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
        initRecycler()
        initLiveData()
        viewModel.getCurrencies()
    }

    private fun initLiveData() {
        viewModel.ratesLiveData.reObserve(this, Observer {
            it.let {
                viewModel.currencyList.add(
                        Currency("UR", getResult(Currencies.EUR, "10", it!!)))
                ratesAdapter.refreshList(viewModel.currencyList)
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