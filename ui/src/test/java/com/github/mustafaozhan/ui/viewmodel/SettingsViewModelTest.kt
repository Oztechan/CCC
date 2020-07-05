/*
 Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ui.viewmodel

import com.github.mustafaozhan.data.db.CurrencyDao
import com.github.mustafaozhan.data.model.Currency
import com.github.mustafaozhan.data.preferences.PreferencesRepository
import com.github.mustafaozhan.ui.main.settings.BackEffect
import com.github.mustafaozhan.ui.main.settings.FewCurrencyEffect
import com.github.mustafaozhan.ui.main.settings.SettingsViewModel
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ObsoleteCoroutinesApi
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@ObsoleteCoroutinesApi
@RunWith(JUnit4::class)
class SettingsViewModelTest : BaseViewModelTest<SettingsViewModel>() {

    override lateinit var viewModel: SettingsViewModel

    @RelaxedMockK
    lateinit var preferencesRepository: PreferencesRepository

    @RelaxedMockK
    lateinit var currencyDao: CurrencyDao

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = SettingsViewModel(preferencesRepository, currencyDao)
    }

    // Event
    @Test
    fun `on long click`() = with(viewModel) {
        val currentValue = viewModel.state.selectionVisibility.value
        getEvent().onItemLongClick()
        currentValue?.let {
            Assert.assertEquals(!it, viewModel.state.selectionVisibility.value)
        } ?: Assert.fail()
    }

    @Test
    fun `update all currencies state`() {
        Assert.assertEquals(Unit, viewModel.getEvent().updateAllCurrenciesState(true))
        Assert.assertEquals(Unit, viewModel.getEvent().updateAllCurrenciesState(false))
    }

    @Test
    fun `on item click`() {
        val currency = Currency("EUR", "Euro", "€")
        Assert.assertEquals(Unit, viewModel.getEvent().onItemClick(currency))
    }

    @Test
    fun `on back click`() {
        viewModel.getEvent().onBackClick()
        Assert.assertEquals(BackEffect, viewModel.effect.value)
    }

    @Test
    fun `on done click`() {
        viewModel.getEvent().onDoneClick()
        Assert.assertEquals(FewCurrencyEffect, viewModel.effect.value)
    }
}
