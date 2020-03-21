package mustafaozhan.github.com.mycurrencies.ui.main.fragment.settings

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mustafaozhan.basemob.fragment.BaseDBFragment
import mustafaozhan.github.com.mycurrencies.R
import mustafaozhan.github.com.mycurrencies.databinding.FragmentSettingsBinding
import mustafaozhan.github.com.mycurrencies.extension.checkAd
import mustafaozhan.github.com.mycurrencies.extension.gone
import mustafaozhan.github.com.mycurrencies.extension.reObserve
import mustafaozhan.github.com.mycurrencies.extension.visible
import mustafaozhan.github.com.mycurrencies.model.Currency
import mustafaozhan.github.com.mycurrencies.tool.Toasty.showToasty
import javax.inject.Inject

/**
 * Created by Mustafa Ozhan on 2018-07-12.
 */
class SettingsFragment : BaseDBFragment<FragmentSettingsBinding>() {

    companion object {
        fun newInstance(): SettingsFragment = SettingsFragment()
    }

    @Inject
    lateinit var settingsViewModel: SettingsViewModel

    private val settingsAdapter: SettingsAdapter by lazy { SettingsAdapter() }

    override fun bind(container: ViewGroup?): FragmentSettingsBinding =
        FragmentSettingsBinding.inflate(layoutInflater, container, false)

    override fun onBinding(dataBinding: FragmentSettingsBinding) {
        binding.viewModel = settingsViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getBaseActivity()?.setSupportActionBar(binding.toolbarFragmentSettings)
        initViews()
        initViewState()
        initLiveData()
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

    private fun initLiveData() {
        settingsViewModel.searchQuery.reObserve(this, Observer { query ->
            settingsViewModel.filterList(query)
        })
    }

    private fun initViews() = with(binding) {
        recyclerViewSettings.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter = settingsAdapter
        }
        binding.adView.checkAd(R.string.banner_ad_unit_id_settings, settingsViewModel.isRewardExpired)
    }

    private fun setListeners() {
        with(binding) {
            btnSelectAll.setOnClickListener {
                settingsViewModel.updateCurrencyState(1)
                editTextSearch.setText("")
            }
            btnDeSelectAll.setOnClickListener {
                settingsViewModel.updateCurrencyState(0)
                editTextSearch.setText("")
                settingsViewModel.setCurrentBase(null)
            }
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
}
