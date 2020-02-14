package mustafaozhan.github.com.mycurrencies.ui.main.fragment.settings

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.jakewharton.rxbinding2.widget.textChanges
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.item_setting.view.checkBox
import mustafaozhan.github.com.mycurrencies.R
import mustafaozhan.github.com.mycurrencies.base.fragment.BaseViewBindingFragment
import mustafaozhan.github.com.mycurrencies.databinding.FragmentSettingsBinding
import mustafaozhan.github.com.mycurrencies.extensions.checkAd
import mustafaozhan.github.com.mycurrencies.model.Currency

/**
 * Created by Mustafa Ozhan on 2018-07-12.
 */
class SettingsFragment : BaseViewBindingFragment<SettingsViewModel, FragmentSettingsBinding>() {

    companion object {
        fun newInstance(): SettingsFragment = SettingsFragment()
    }

    override fun getLayoutResId(): Int = R.layout.fragment_settings

    override fun bind() {
        binding = FragmentSettingsBinding.inflate(layoutInflater)
    }

    private val settingsAdapter: SettingsAdapter by lazy { SettingsAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setSupportActionBar(binding.toolbarFragmentSettings)
        initViews()
        initRx()
        setListeners()
    }

    private fun initRx() {
        binding.editTextSearch
            .textChanges()
            .map { it.toString() }
            .subscribe({ txt ->
                viewModel.currencyList.filter { currency ->
                    currency.name.contains(txt, true) ||
                        currency.longName.contains(txt, true) ||
                        currency.symbol.contains(txt, true)
                }.toMutableList()
                    .let { settingsAdapter.refreshList(it) }
            }, {
                logException(it)
            })
            .addTo(compositeDisposable)
    }

    private fun initViews() {
        context?.let { ctx ->
            binding.recyclerViewSettings.apply {
                layoutManager = LinearLayoutManager(ctx)
                setHasFixedSize(true)
                adapter = settingsAdapter
            }
        }
    }

    private fun setListeners() {
        binding.btnSelectAll.setOnClickListener {
            viewModel.updateCurrencyState(1)
            binding.editTextSearch.setText("")
        }
        binding.btnDeSelectAll.setOnClickListener {
            viewModel.updateCurrencyState(0)
            binding.editTextSearch.setText("")
            viewModel.setCurrentBase(null)
        }

        settingsAdapter.onItemClickListener = { currency: Currency, itemView, _ ->
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

    override fun onResume() {
        viewModel.refreshData()
        binding.editTextSearch.setText("")
        binding.adView.checkAd(R.string.banner_ad_unit_id_settings, viewModel.isRewardExpired)
        super.onResume()
    }
}
