/*
 Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ui.main.settings

import android.view.ViewGroup
import com.github.mustafaozhan.basemob.view.fragment.BaseDBFragment
import com.github.mustafaozhan.ui.databinding.FragmentSettingsBinding
import javax.inject.Inject

class SettingsFragment : BaseDBFragment<FragmentSettingsBinding>() {
    @Inject
    lateinit var settingsViewModel: SettingsViewModel

    override fun bind(container: ViewGroup?): FragmentSettingsBinding =
        FragmentSettingsBinding.inflate(layoutInflater, container, false)

    override fun onBinding(dataBinding: FragmentSettingsBinding) {
        binding.vm = settingsViewModel
        binding.event = settingsViewModel.getEvent()
    }
}
