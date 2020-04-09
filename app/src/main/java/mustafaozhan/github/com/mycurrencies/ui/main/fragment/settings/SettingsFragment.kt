package mustafaozhan.github.com.mycurrencies.ui.main.fragment.settings

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mustafaozhan.basemob.fragment.BaseVBFragment
import com.github.mustafaozhan.logmob.logError
import com.jakewharton.rxbinding2.widget.textChanges
import mustafaozhan.github.com.mycurrencies.R
import mustafaozhan.github.com.mycurrencies.databinding.FragmentSettingsBinding
import mustafaozhan.github.com.mycurrencies.extension.checkAd
import mustafaozhan.github.com.mycurrencies.extension.gone
import mustafaozhan.github.com.mycurrencies.extension.reObserve
import mustafaozhan.github.com.mycurrencies.extension.visible
import mustafaozhan.github.com.mycurrencies.tool.Toasty.showToasty
import mustafaozhan.github.com.mycurrencies.ui.main.fragment.settings.view.FewCurrency
import mustafaozhan.github.com.mycurrencies.ui.main.fragment.settings.view.NoFilter
import mustafaozhan.github.com.mycurrencies.ui.main.fragment.settings.view.NoResult
import mustafaozhan.github.com.mycurrencies.ui.main.fragment.settings.view.Success
import mustafaozhan.github.com.mycurrencies.ui.main.fragment.settings.view.ViewEvent
import javax.inject.Inject

/**
 * Created by Mustafa Ozhan on 2018-07-12.
 */
class SettingsFragment : BaseVBFragment<FragmentSettingsBinding>() {

    @Inject
    lateinit var settingsViewModel: SettingsViewModel

    private lateinit var viewEvent: ViewEvent

    private val settingsAdapter: SettingsAdapter by lazy { SettingsAdapter(viewEvent) }

    override fun bind() {
        binding = FragmentSettingsBinding.inflate(layoutInflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getBaseActivity()?.setSupportActionBar(binding.toolbarFragmentSettings)
        viewEvent = settingsViewModel
        initViews()
        initViewState()
        initViewEffect()
        initRx()
    }

    private fun initViewState() = settingsViewModel.viewStateLiveData
        .reObserve(viewLifecycleOwner, Observer { viewState ->
            binding.txtNoResult.gone()
            when (viewState) {
                NoResult -> {
                    settingsAdapter.submitList(mutableListOf())
                    binding.txtNoResult.visible()
                }
                is NoFilter -> {
                    binding.editTextSearch.setText("")
                    if (viewState.shouldCleanBase) {
                        settingsViewModel.setCurrentBase(null)
                    }
                }
                is Success -> settingsAdapter.submitList(viewState.currencyList)
            }
        })

    private fun initViewEffect() = settingsViewModel.viewEffectLiveData
        .reObserve(viewLifecycleOwner, Observer { viewEvent ->
            when (viewEvent) {
                FewCurrency -> showToasty(requireContext(), R.string.choose_at_least_two_currency)
            }
        })

    private fun initRx() = compositeDisposable.add(
        binding.editTextSearch
            .textChanges()
            .map { it.toString() }
            .subscribe(
                { settingsViewModel.filterList(it) },
                { logError(it) }
            )
    )

    private fun initViews() = with(binding) {
        recyclerViewSettings.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter = settingsAdapter
        }
        editTextSearch.setText("")
        btnSelectAll.setOnClickListener { viewEvent.updateAllStates(1) }
        btnDeSelectAll.setOnClickListener { viewEvent.updateAllStates(0) }
        adView.checkAd(R.string.banner_ad_unit_id_settings, settingsViewModel.isRewardExpired)
    }
}
