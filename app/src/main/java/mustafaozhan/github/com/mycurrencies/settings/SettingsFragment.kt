package mustafaozhan.github.com.mycurrencies.settings

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.jakewharton.rxbinding2.widget.textChanges
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.fragment_settings.adView
import kotlinx.android.synthetic.main.fragment_settings.eTxtSearch
import kotlinx.android.synthetic.main.fragment_settings.mRecViewSettings
import kotlinx.android.synthetic.main.item_setting.view.checkBox
import kotlinx.android.synthetic.main.layout_settings_toolbar.btnDeSelectAll
import kotlinx.android.synthetic.main.layout_settings_toolbar.btnSelectAll
import mustafaozhan.github.com.mycurrencies.R
import mustafaozhan.github.com.mycurrencies.base.BaseMvvmFragment
import mustafaozhan.github.com.mycurrencies.extensions.loadAd
import mustafaozhan.github.com.mycurrencies.model.Currency
import mustafaozhan.github.com.mycurrencies.settings.adapter.SettingAdapter

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
        initRx()
        setListeners()
    }

    private fun initRx() {
        eTxtSearch
            .textChanges()
            .subscribe { txt ->
                viewModel.currencyList.filter { currency ->
                    currency.name.contains(txt.toString(), true) ||
                        currency.longName.contains(txt.toString(), true) ||
                        currency.symbol.contains(txt.toString(), true)
                }.toMutableList()
                    .let { settingAdapter.refreshList(it) }
            }.addTo(compositeDisposable)
    }

    private fun initViews() {
        context?.let { ctx ->
            mRecViewSettings.apply {
                layoutManager = LinearLayoutManager(ctx)
                setHasFixedSize(true)
                adapter = settingAdapter
            }
        }
    }

    private fun setListeners() {
        btnSelectAll.setOnClickListener {
            viewModel.updateCurrencyState(1)
            eTxtSearch.setText("")
        }
        btnDeSelectAll.setOnClickListener {
            viewModel.updateCurrencyState(0)
            eTxtSearch.setText("")
            viewModel.setCurrentBase(null)
        }

        settingAdapter.onItemClickListener = { currency: Currency, itemView, _ ->
            when (currency.isActive) {
                0 -> {
                    currency.isActive = 1
                    viewModel.updateCurrencyState(1, currency.name)
                    itemView.checkBox.isChecked = true
                }
                1 -> {
                    currency.isActive = 0
                    viewModel.updateCurrencyState(0, currency.name)
                    itemView.checkBox.isChecked = false
                }
            }
        }
    }

    override fun onPause() {
        viewModel.savePreferences()
        super.onPause()
    }

    override fun onResume() {
        viewModel.refreshData()
        eTxtSearch.setText("")
        checkAd()
        super.onResume()
    }

    private fun checkAd() {
        if (viewModel.isRewardExpired()) {
            adView.loadAd(R.string.banner_ad_unit_id_settings)
        } else {
            adView.isEnabled = false
            adView.visibility = View.GONE
        }
    }
}