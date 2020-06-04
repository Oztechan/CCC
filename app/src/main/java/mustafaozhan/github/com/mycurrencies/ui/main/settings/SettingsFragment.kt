/*
 Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package mustafaozhan.github.com.mycurrencies.ui.main.settings

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mustafaozhan.basemob.util.Toast.show
import com.github.mustafaozhan.basemob.util.setNavigationResult
import com.github.mustafaozhan.basemob.view.fragment.BaseDBFragment
import mustafaozhan.github.com.mycurrencies.R
import mustafaozhan.github.com.mycurrencies.databinding.FragmentSettingsBinding
import mustafaozhan.github.com.mycurrencies.ui.main.MainData.Companion.KEY_BASE_CURRENCY
import javax.inject.Inject

class SettingsFragment : BaseDBFragment<FragmentSettingsBinding>() {

    @Inject
    lateinit var settingsViewModel: SettingsViewModel

    private lateinit var settingsAdapter: SettingsAdapter

    override fun bind(container: ViewGroup?): FragmentSettingsBinding =
        FragmentSettingsBinding.inflate(layoutInflater, container, false)

    override fun onBinding(dataBinding: FragmentSettingsBinding) {
        binding.vm = settingsViewModel
        settingsViewModel.getEvent().let {
            binding.event = it
            settingsAdapter = SettingsAdapter(it)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getBaseActivity()?.setSupportActionBar(binding.toolbarFragmentSettings)
        initView()
        observeEffect()
    }

    private fun initView() {
        binding.recyclerViewSettings.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter = settingsAdapter
        }

        settingsViewModel.state.currencyList.observe(viewLifecycleOwner) {
            settingsAdapter.submitList(it)
        }
    }

    private fun observeEffect() = settingsViewModel.effect
        .observe(viewLifecycleOwner) { viewEffect ->
            when (viewEffect) {
                FewCurrencyEffect -> show(requireContext(), R.string.choose_at_least_two_currency)
                CalculatorEffect -> navigate(
                    R.id.settingsFragment,
                    SettingsFragmentDirections.actionSettingsFragmentToCalculatorFragment()
                )
                is ChangeBaseNavResultEffect -> {
                    setNavigationResult(viewEffect.newBase, KEY_BASE_CURRENCY)
                }
            }
        }
}
