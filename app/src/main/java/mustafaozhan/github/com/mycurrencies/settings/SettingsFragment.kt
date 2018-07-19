package mustafaozhan.github.com.mycurrencies.settings

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import kotlinx.android.synthetic.main.fragment_settings.*
import kotlinx.android.synthetic.main.layout_settings_toolbar.*
import mustafaozhan.github.com.mycurrencies.R
import mustafaozhan.github.com.mycurrencies.base.BaseMvvmFragment
import mustafaozhan.github.com.mycurrencies.room.model.Currency
import mustafaozhan.github.com.mycurrencies.settings.adapter.SettingAdapter
import mustafaozhan.github.com.mycurrencies.tools.setBackgroundByName
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread


/**
 * Created by Mustafa Ozhan on 2018-07-12.
 */
class SettingsFragment : BaseMvvmFragment<SettingsFragmentViewModel>() {

    companion object {
        fun newInstance(): SettingsFragment = SettingsFragment()
    }

    private val settingAdapter: SettingAdapter by lazy { SettingAdapter() }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        initRecycler()
        setListeners()
    }

    private fun initRecycler() {
        context?.let {
            mRecViewSettings.layoutManager = LinearLayoutManager(it)
            mRecViewSettings.adapter = settingAdapter
            settingAdapter.refreshList(viewModel.currencyList, viewModel.getCurrentBase(), false)
        }
        settingAdapter.onItemSelectedListener = { currency: Currency, _, _, position ->

            when (viewModel.currencyList[position].isActive) {
                0 -> {
                    viewModel.currencyList[position].isActive = 1
                    updateUi(update = true, byName = true, name = currency.name, value = 1)
                }
                1 -> {
                    viewModel.currencyList[position].isActive = 0

                    if (viewModel.currencyList[position].name == viewModel.getBaseCurrency().toString())
                        viewModel.setBaseCurrency(viewModel.currencyList.filter { it.isActive == 1 }[0].name)

                    updateUi(update = true, byName = true, name = currency.name, value = 0)
                }
            }

        }

    }


    private fun setListeners() {
        mSpinnerSettings.setOnItemSelectedListener { _, _, _, _ ->
            viewModel.setBaseCurrency(mSpinnerSettings.text.toString())
            imgBaseSettings.setBackgroundByName(mSpinnerSettings.text.toString())
        }


        mConstraintLayoutSettings.setOnClickListener {
            if (mSpinnerSettings.isActivated)
                mSpinnerSettings.collapse()
            else
                mSpinnerSettings.expand()
        }

        btnSelectAll.setOnClickListener { updateUi(true, false, 1) }
        btnDeSelectAll.setOnClickListener { updateUi(true, false, 0) }
    }


    override fun onResume() {
        updateUi()
        super.onResume()
    }

    private fun updateUi(update: Boolean = false, byName: Boolean = false, value: Int = 0, name: String = "") {

        doAsync {
            if (update)
                if (byName)
                    viewModel.updateCurrencyStateByName(name, value)
                else
                    viewModel.updateAllCurrencyState(value)

            viewModel.initData()

            uiThread {
                setSpinner()
                settingAdapter.refreshList(viewModel.currencyList, viewModel.getCurrentBase(), false)
            }
        }
    }

    private fun setSpinner() {
        val spinnerList = ArrayList<String>()
        viewModel.currencyList.filter {
            it.isActive == 1
        }.forEach {
            spinnerList.add(it.name)
        }
        if (spinnerList.toList().lastIndex < 1) {
            mSpinnerSettings.setItems("Select at least two currency from Settings")
            imgBaseSettings.setBackgroundByName("transparent")

        } else {
            mSpinnerSettings.setItems(spinnerList.toList())
            mSpinnerSettings.selectedIndex = spinnerList.indexOf(viewModel.getBaseCurrency().toString())
            imgBaseSettings.setBackgroundByName(viewModel.getBaseCurrency().toString())
        }


    }

    override fun getViewModelClass(): Class<SettingsFragmentViewModel> = SettingsFragmentViewModel::class.java

    override fun getLayoutResId(): Int = R.layout.fragment_settings

}