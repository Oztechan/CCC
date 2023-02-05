package com.oztechan.ccc.android.viewmodel.premium.di

import com.oztechan.ccc.android.viewmodel.premium.PremiumViewModel
import com.oztechan.ccc.client.core.viewmodel.di.viewModelDefinition
import org.koin.dsl.module

val androidViewModelPremiumModule = module {
    viewModelDefinition { PremiumViewModel(get()) }
}
