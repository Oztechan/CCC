package mustafaozhan.github.com.mycurrencies.ui.main.fragment.settings

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mustafaozhan.basemob.fragment.BaseDBFragment
import mustafaozhan.github.com.mycurrencies.databinding.FragmentSettingsBinding
import mustafaozhan.github.com.mycurrencies.databinding.ItemSettingBinding
import mustafaozhan.github.com.mycurrencies.model.Currency
import javax.inject.Inject

/**
 * Created by Mustafa Ozhan on 2018-07-12.
 */
class SettingsFragment : BaseDBFragment<FragmentSettingsBinding>(), SettingsItemView {

    companion object {
        fun newInstance(): SettingsFragment = SettingsFragment()
    }

    @Inject
    lateinit var settingsViewModel: SettingsViewModel

    private val settingsAdapter: SettingsAdapter by lazy { SettingsAdapter(this) }

    override fun bind(container: ViewGroup?): FragmentSettingsBinding =
        FragmentSettingsBinding.inflate(layoutInflater, container, false)

    override fun onBinding(dataBinding: FragmentSettingsBinding) {
        binding.viewModel = settingsViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getBaseActivity()?.setSupportActionBar(binding.toolbarFragmentSettings)
        initViews()
        setListeners()
    }

    private fun initViews() = with(binding) {
        recyclerViewSettings.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter = settingsAdapter
        }
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
    }

    override fun onSettingsItemClick(itemSettingBinding: ItemSettingBinding, currency: Currency) {
        when (currency.isActive) {
            0 -> {
                currency.isActive = 1
                settingsViewModel.updateCurrencyState(1, currency.name)
                itemSettingBinding.checkBox.isChecked = true
            }
            1 -> {
                currency.isActive = 0
                settingsViewModel.updateCurrencyState(0, currency.name)
                itemSettingBinding.checkBox.isChecked = false
            }
        }
    }
}
