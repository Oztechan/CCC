package mustafaozhan.github.com.mycurrencies.settings


import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.view.View
import kotlinx.android.synthetic.main.fragment_settings.*
import kotlinx.android.synthetic.main.layout_settings_toolbar.*
import mustafaozhan.github.com.mycurrencies.R
import mustafaozhan.github.com.mycurrencies.base.BaseMvvmFragment
import mustafaozhan.github.com.mycurrencies.extensions.loadAd
import mustafaozhan.github.com.mycurrencies.extensions.reObserve
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
            mRecViewSettings.layoutManager = GridLayoutManager(it, 3)
            mRecViewSettings.setHasFixedSize(true)
            mRecViewSettings.id
            mRecViewSettings.adapter = settingAdapter
        }

        viewModel.currencyListLiveData.reObserve(this, Observer { currency ->
            currency?.let { settingAdapter.refreshList(it, null, false) }
        })
    }


    private fun setListeners() {
        btnSelectAll.setOnClickListener { updateUi(true, 1) }

        btnDeSelectAll.setOnClickListener {
            updateUi(true, 0)
            viewModel.setCurrentBase(null)
        }

        settingAdapter.onItemClickListener = { currency: Currency, _, _, position ->
            viewModel.currencyListLiveData.value?.let { currencyList ->
                when (currencyList[position].isActive) {
                    0 -> {
                        currencyList[position].isActive = 1
                        updateUi(true, 1, currency.name)
                    }
                    1 -> {
                        if (currencyList[position].name == viewModel.mainData.currentBase.toString()) {
                            viewModel.setCurrentBase(currencyList.firstOrNull { it.isActive == 1 }?.name)
                        }
                        currencyList[position].isActive = 0
                        updateUi(true, 0, currency.name)
                    }
                }
            }
        }
    }

    private fun updateUi(update: Boolean = false, value: Int = 0, name: String = "") {

        doAsync {
            if (update) {
                if (name.isNotEmpty()) {
                    viewModel.updateCurrencyStateByName(name, value)
                } else {
                    viewModel.updateAllCurrencyState(value)
                }
            } else {
                viewModel.initData()
            }
            uiThread {
                viewModel.currencyListLiveData.value?.let { currencyList ->
                    when {
                        currencyList.filter { it.isActive == 1 }.count() < 2 -> snacky(getString(R.string.choose_currencies))
                        viewModel.mainData.currentBase == Currencies.NULL -> viewModel.setCurrentBase(currencyList.firstOrNull { it.isActive == 1 }?.name)
                    }
                    settingAdapter.refreshList(currencyList, null, false)
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
        adView.loadAd(R.string.banner_ad_unit_id_settings)
        super.onResume()
    }
}