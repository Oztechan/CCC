package mustafaozhan.github.com.mycurrencies.ui.main.fragment.settings

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mustafaozhan.basemob.fragment.BaseViewBindingFragment
import com.jakewharton.rxbinding2.widget.textChanges
import io.reactivex.rxkotlin.addTo
import mustafaozhan.github.com.mycurrencies.R
import mustafaozhan.github.com.mycurrencies.databinding.FragmentSettingsBinding
import mustafaozhan.github.com.mycurrencies.function.extension.checkAd
import mustafaozhan.github.com.mycurrencies.function.extension.gone
import mustafaozhan.github.com.mycurrencies.function.extension.reObserve
import mustafaozhan.github.com.mycurrencies.function.extension.visible
import mustafaozhan.github.com.mycurrencies.model.Currency
import mustafaozhan.github.com.mycurrencies.tool.Toasty.showToasty
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Mustafa Ozhan on 2018-07-12.
 */
class SettingsFragment : BaseViewBindingFragment<FragmentSettingsBinding>() {

    companion object {
        fun newInstance(): SettingsFragment = SettingsFragment()
    }

    @Inject
    lateinit var settingsViewModel: SettingsViewModel

    override fun bind() {
        binding = FragmentSettingsBinding.inflate(layoutInflater)
    }

    private val settingsAdapter: SettingsAdapter by lazy { SettingsAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getBaseActivity()?.setSupportActionBar(binding.toolbarFragmentSettings)
        initViews()
        initViewState()
        initRx()
        setListeners()
    }

    private fun initViewState() = settingsViewModel.settingsViewStateLiveData
        .reObserve(this, Observer { settingsViewState ->
            binding.txtNoResult.gone()
            when (settingsViewState) {
                SettingsViewState.FewCurrency -> showToasty(requireContext(), R.string.choose_at_least_two_currency)
                SettingsViewState.NoResult -> {
                    settingsAdapter.refreshList(mutableListOf())
                    binding.txtNoResult.visible()
                }
                is SettingsViewState.Success -> settingsAdapter.refreshList(settingsViewState.currencyList)
            }
        })

    private fun initRx() = binding.editTextSearch
        .textChanges()
        .map { it.toString() }
        .subscribe(
            { settingsViewModel.filterList(it) },
            { Timber.e(it) }
        ).addTo(compositeDisposable)

    private fun initViews() = binding.recyclerViewSettings.apply {
        layoutManager = LinearLayoutManager(requireContext())
        setHasFixedSize(true)
        adapter = settingsAdapter
    }

    private fun setListeners() {
        binding.btnSelectAll.setOnClickListener {
            settingsViewModel.updateCurrencyState(1)
            binding.editTextSearch.setText("")
        }
        binding.btnDeSelectAll.setOnClickListener {
            settingsViewModel.updateCurrencyState(0)
            binding.editTextSearch.setText("")
            settingsViewModel.setCurrentBase(null)
        }

        settingsAdapter.onItemClickListener = { currency: Currency, itemBinding, _ ->
            when (currency.isActive) {
                0 -> {
                    currency.isActive = 1
                    settingsViewModel.updateCurrencyState(1, currency.name)
                    itemBinding.checkBox.isChecked = true
                }
                1 -> {
                    currency.isActive = 0
                    settingsViewModel.updateCurrencyState(0, currency.name)
                    itemBinding.checkBox.isChecked = false
                }
            }
        }
    }

    override fun onResume() {
        settingsViewModel.refreshData()
        binding.editTextSearch.setText("")
        binding.adView.checkAd(R.string.banner_ad_unit_id_settings, settingsViewModel.isRewardExpired)
        super.onResume()
    }
}
