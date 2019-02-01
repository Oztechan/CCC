package mustafaozhan.github.com.mycurrencies.settings


import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.view.View
import com.crashlytics.android.Crashlytics
import kotlinx.android.synthetic.main.fragment_settings.*
import kotlinx.android.synthetic.main.layout_settings_toolbar.*
import mustafaozhan.github.com.mycurrencies.R
import mustafaozhan.github.com.mycurrencies.base.BaseMvvmFragment
import mustafaozhan.github.com.mycurrencies.extensions.loadAd
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
            mRecViewSettings.layoutManager = GridLayoutManager(it, 3)
            mRecViewSettings.adapter = settingAdapter
            settingAdapter.refreshList(viewModel.currencyList, null, false)
        }

        settingAdapter.onItemClickListener = { currency: Currency, _, _, position ->
            when (viewModel.currencyList[position].isActive) {
                0 -> {
                    viewModel.currencyList[position].isActive = 1
                    updateUi(update = true, byName = true, name = currency.name, value = 1)
                }
                1 -> {
                    viewModel.currencyList[position].isActive = 0
                    if (viewModel.currencyList[position].name == viewModel.mainData.currentBase.toString()) {
                        viewModel.setCurrentBase(viewModel.currencyList.firstOrNull {
                            it.isActive == 1
                        }?.name)
                    }
                    updateUi(update = true, byName = true, name = currency.name, value = 0)
                }
            }
        }
    }


    private fun setListeners() {
        btnSelectAll.setOnClickListener { updateUi(true, false, 1) }
        btnDeSelectAll.setOnClickListener {
            updateUi(true, false, 0)
            viewModel.setCurrentBase(null)
        }
    }

    private fun updateUi(update: Boolean = false, byName: Boolean = false, value: Int = 0, name: String = "") {

        doAsync {
            if (update) {
                if (byName) {
                    viewModel.updateCurrencyStateByName(name, value)
                } else {
                    viewModel.updateAllCurrencyState(value)
                }
            }

            viewModel.initData()

            uiThread {
                try {
                    if (viewModel.currencyList.filter { currency -> currency.isActive == 1 }.count() < 2) {
                        (activity as MainActivity).snacky(getString(R.string.choose_at_least_two_currency))
                    } else if (viewModel.mainData.currentBase == Currencies.NULL) {
                        viewModel.setCurrentBase(viewModel.currencyList.firstOrNull { currency -> currency.isActive == 1 }?.name)
                    }
                    settingAdapter.refreshList(viewModel.currencyList, null, false)
                } catch (e: Exception) {
                    Crashlytics.logException(e)
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
            Crashlytics.logException(e)
            e.printStackTrace()
        }
        super.onResume()
    }
}