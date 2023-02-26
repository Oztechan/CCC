package com.oztechan.ccc.client.viewmodel.premium.di

import com.oztechan.ccc.client.core.viewmodel.di.viewModelDefinition
import com.oztechan.ccc.client.viewmodel.premium.PremiumViewModel
import org.koin.dsl.module

val clientViewModelPremiumModule = module {
    viewModelDefinition { PremiumViewModel(get()) }
}
