package mustafaozhan.github.com.mycurrencies.settings


import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.view.View
import kotlinx.android.synthetic.main.fragment_settings.*
import kotlinx.android.synthetic.main.layout_settings_toolbar.*
import mustafaozhan.github.com.mycurrencies.R
import mustafaozhan.github.com.mycurrencies.base.BaseMvvmFragment
import mustafaozhan.github.com.mycurrencies.extensions.loadAd
import mustafaozhan.github.com.mycurrencies.extensions.setBackgroundByName
import mustafaozhan.github.com.mycurrencies.extensions.setSelectedIndex
import mustafaozhan.github.com.mycurrencies.main.activity.MainActivity
import mustafaozhan.github.com.mycurrencies.room.model.Currency
import mustafaozhan.github.com.mycurrencies.settings.adapter.SettingAdapter
import mustafaozhan.github.com.mycurrencies.tools.Currencies
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread


/**
 * Created by Mustafa Ozhan on 2018-07-12.
 */
class SettingsFragment : BaseMvvmFragment<SettingsFragmentViewModel>() {

    companion object {
        fun newInstance(): SettingsFragment = SettingsFragment()
    }

    override fun getViewModelClass(): Class<SettingsFragmentViewModel> = SettingsFragmentViewModel::class.java

    override fun getLayoutResId(): Int = R.layout.fragment_settings

    private val settingAdapter: SettingAdapter by lazy { SettingAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        initViews()
        setListeners()
    }

    private fun initViews() {
        context?.let {
            mRecViewSettings.layoutManager = GridLayoutManager(it, 2)
            mRecViewSettings.adapter = settingAdapter
            settingAdapter.refreshList(viewModel.currencyList, null, false)
        }
        settingAdapter.onItemSelectedListener = { currency: Currency, _, _, position ->

            when (viewModel.currencyList[position].isActive) {
                0 -> {
                    viewModel.currencyList[position].isActive = 1
                    updateUi(update = true, byName = true, name = currency.name, value = 1)
                }
                1 -> {
                    viewModel.currencyList[position].isActive = 0

                    if (viewModel.currencyList[position].name == viewModel.mainData.baseCurrency.toString()
                            &&
                            viewModel.currencyList.filter { it.isActive == 1 }.size > 2)
                        viewModel.setBaseCurrency(viewModel.currencyList.filter { it.isActive == 1 }[0].name)
                    updateUi(update = true, byName = true, name = currency.name, value = 0)
                }
            }
        }

    }


    private fun setListeners() {
        mSpinnerSettings.setOnItemSelectedListener { _, _, _, item ->
            if (item == "LAST USED") {
                viewModel.setBaseCurrency(viewModel.mainData.currentBase.toString())
                viewModel.mainData.lastUsed = true
                imgBaseSettings.setBackgroundByName(viewModel.mainData.currentBase.toString())
            } else {
                viewModel.setBaseCurrency(item.toString())
                viewModel.mainData.lastUsed = true
                imgBaseSettings.setBackgroundByName(item.toString())
            }

        }


        mConstraintLayoutSettings.setOnClickListener {
            if (mSpinnerSettings.isActivated)
                mSpinnerSettings.collapse()
            else
                mSpinnerSettings.expand()
        }

        btnSelectAll.setOnClickListener { updateUi(true, false, 1) }
        btnDeSelectAll.setOnClickListener {
            updateUi(true, false, 0)
            viewModel.setBaseCurrency(null)
        }
    }

    private fun updateUi(update: Boolean = false, byName: Boolean = false, value: Int = 0, name: String = "") {

        doAsync {
            if (update)
                if (byName) {
                    viewModel.updateCurrencyStateByName(name, value)
                } else
                    viewModel.updateAllCurrencyState(value)

            viewModel.initData()

            uiThread { _ ->
                try {
                    val spinnerList = ArrayList<String>()
                    spinnerList.add("LAST USED")
                    viewModel.currencyList.filter { it.isActive == 1 }.forEach { spinnerList.add(it.name) }

                    if (spinnerList.toList().size <= 2) {
                        (activity as MainActivity).snacky("Please Select at least 2 currencies")
                        imgBaseSettings.setBackgroundByName("transparent")
                        mSpinnerSettings.setItems("")
                    } else {
                        mSpinnerSettings.setItems(spinnerList)
                        if (viewModel.mainData.baseCurrency == Currencies.NULL && viewModel.currencyList.isNotEmpty()) {
                            viewModel.setBaseCurrency(viewModel.currencyList.filter { it.isActive == 1 }[0].name)
                        } else {
                            mSpinnerSettings.setItems(spinnerList)
                            if (viewModel.mainData.baseCurrency == Currencies.NULL) {
                                viewModel.setBaseCurrency(viewModel.currencyList.filter { it.isActive == 1 }[0].name)
                            }
                            viewModel.currencyList.filter {
                                it.isActive == 1
                                        && it.name == viewModel.mainData.baseCurrency.toString()
                            }.forEach {
                                viewModel.setBaseCurrency(it.name)
                            }
                        }
                        mSpinnerSettings.setSelectedIndex(viewModel.mainData.lastUsed, viewModel.mainData.baseCurrency.toString())
                        imgBaseSettings.setBackgroundByName(viewModel.mainData.baseCurrency.toString())
                    }
                    viewModel.setBaseCurrency(if (mSpinnerSettings.text.toString() == "") null else if (mSpinnerSettings.text.toString() == "LAST USED") viewModel.mainData.baseCurrency.toString() else mSpinnerSettings.text.toString())
                    settingAdapter.refreshList(viewModel.currencyList, null, false)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }


    override fun onPause() {
        viewModel.savePreferences()
        super.onPause()
    }

    override fun onResume() {
        viewModel.loadPreferences()
        updateUi()
        try {
            adView.loadAd(R.string.banner_ad_unit_id_settings)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        super.onResume()
    }
}