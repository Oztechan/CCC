package com.oztechan.ccc.client.viewmodel.calculator.di

import com.oztechan.ccc.client.core.viewmodel.di.viewModelDefinition
import com.oztechan.ccc.client.viewmodel.calculator.CalculatorViewModel
import org.koin.dsl.module

val clientViewModelCalculatorModule = module {
    viewModelDefinition { CalculatorViewModel(get(), get(), get(), get(), get(), get()) }
}
