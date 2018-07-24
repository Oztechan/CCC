package mustafaozhan.github.com.mycurrencies.settings


import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.Toast
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import kotlinx.android.synthetic.main.fragment_settings.*
import kotlinx.android.synthetic.main.layout_settings_toolbar.*
import mustafaozhan.github.com.mycurrencies.R
import mustafaozhan.github.com.mycurrencies.base.BaseMvvmFragment
import mustafaozhan.github.com.mycurrencies.extensions.setBackgroundByName
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
                            && viewModel.currencyList.filter { it.isActive == 1 }.size > 1)
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
        btnDeSelectAll.setOnClickListener {
            updateUi(true, false, 0)
            viewModel.setBaseCurrency(null)
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

    private fun updateUi(update: Boolean = false, byName: Boolean = false, value: Int = 0, name: String = "") {

        doAsync {
            if (update)
                if (byName) {
                    viewModel.updateCurrencyStateByName(name, value)
                } else
                    viewModel.updateAllCurrencyState(value)

            viewModel.initData()

            uiThread {
                setSpinner()
                settingAdapter.refreshList(viewModel.currencyList, null, false)
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
        if (spinnerList.toList().size <= 1) {
            Toast.makeText(context, "Please Select at least 2 currency from Settings", Toast.LENGTH_SHORT).show()
            imgBaseSettings.setBackgroundByName("transparent")
            mSpinnerSettings.setItems("")
            settingAdapter.refreshList(viewModel.currencyList, null, false)
        } else {
            mSpinnerSettings.setItems(spinnerList)
            if (viewModel.mainData.baseCurrency == Currencies.NULL && viewModel.currencyList.isNotEmpty()) {
                viewModel.setBaseCurrency(viewModel.currencyList.filter { it.isActive == 1 }[0].name)
                mSpinnerSettings.selectedIndex = spinnerList.indexOf(viewModel.mainData.baseCurrency.toString())
                imgBaseSettings.setBackgroundByName(mSpinnerSettings.text.toString())
            } else {
                mSpinnerSettings.setItems(spinnerList)
                if (viewModel.mainData.baseCurrency == Currencies.NULL)
                    viewModel.setBaseCurrency(viewModel.currencyList.filter { it.isActive == 1 }[0].name)

                viewModel.currencyList.filter {
                    it.isActive == 1
                }.forEach {
                    if (it.name == viewModel.mainData.baseCurrency.toString()) {
                        mSpinnerSettings.selectedIndex = spinnerList.indexOf(viewModel.mainData.baseCurrency.toString())
                        viewModel.setBaseCurrency(it.name)
                    }
                }


            }
            imgBaseSettings.setBackgroundByName(mSpinnerSettings.text.toString())
        }


    }

    override fun getViewModelClass(): Class<SettingsFragmentViewModel> = SettingsFragmentViewModel::class.java

    override fun getLayoutResId(): Int = R.layout.fragment_settings

    private fun loadAd() {
        MobileAds.initialize(context, resources.getString(R.string.banner_ad_unit_id_settings))
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)
    }


    override fun onPause() {
        viewModel.savePreferences()
        super.onPause()
    }



}