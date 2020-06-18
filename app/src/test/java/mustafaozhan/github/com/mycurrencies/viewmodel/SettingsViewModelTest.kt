/*
 Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package mustafaozhan.github.com.mycurrencies.viewmodel

import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ObsoleteCoroutinesApi
import mustafaozhan.github.com.mycurrencies.data.db.CurrencyDao
import mustafaozhan.github.com.mycurrencies.data.preferences.PreferencesRepository
import mustafaozhan.github.com.mycurrencies.ui.main.settings.SettingsViewModel
import org.junit.Assert.assertEquals
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

    @Test
    fun `is search query emitting`() = with(viewModel.state) {
        val mockSearchQuery = "abc"
        searchQuery.postValue(mockSearchQuery)
        assertEquals(searchQuery.value, mockSearchQuery)
    }
}
