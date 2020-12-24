/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ccc.client.viewmodel

import com.github.mustafaozhan.ccc.client.base.BaseUseCaseTest
import com.github.mustafaozhan.ccc.client.ui.bar.BarUseCase
import com.github.mustafaozhan.ccc.client.ui.bar.ChangeBaseNavResultEffect
import com.github.mustafaozhan.ccc.client.ui.bar.OpenCurrenciesEffect
import com.github.mustafaozhan.ccc.common.di.getDependency
import com.github.mustafaozhan.ccc.common.model.Currency
import com.github.mustafaozhan.ccc.common.runTest
import com.github.mustafaozhan.ccc.common.settings.SettingsRepository
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.launch

class BarUseCaseTest : BaseUseCaseTest<BarUseCase>() {

    override val useCase: BarUseCase by lazy {
        koin.getDependency(SettingsRepository::class)
    }

    @Test
    fun onItemClick() = runTest {
        it.launch {
            val currency = Currency("USD", "Dollar", "$", 0.0, true)
            useCase.getEvent().onItemClick(currency)

            assertEquals(
                ChangeBaseNavResultEffect(currency.name),
                useCase.effect.single()
            )
        }.cancel()
    }

    @Test
    fun onSelectClick() = runTest {
        it.launch {
            useCase.getEvent().onSelectClick()
            assertEquals(OpenCurrenciesEffect, useCase.effect.single())
        }.cancel()
    }
}
