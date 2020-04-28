// Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
package mustafaozhan.github.com.mycurrencies.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import mustafaozhan.github.com.mycurrencies.data.preferences.PreferencesRepository
import mustafaozhan.github.com.mycurrencies.data.room.currency.CurrencyRepository
import mustafaozhan.github.com.mycurrencies.ui.main.settings.SettingsViewModel
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class SettingsViewModelTest {

    private lateinit var viewModel: SettingsViewModel

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    @RelaxedMockK
    lateinit var preferencesRepository: PreferencesRepository

    @RelaxedMockK
    lateinit var currencyRepository: CurrencyRepository

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = SettingsViewModel(preferencesRepository, currencyRepository)
    }

    @Test
    fun `is search query emitting`() {
        // state
        val mockSearchQuery = "abc"

        viewModel.state.apply {
            searchQuery.postValue(mockSearchQuery)
            Assert.assertEquals(searchQuery.value, mockSearchQuery)
        }
    }
}
