/*
 Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package mustafaozhan.github.com.mycurrencies.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import mustafaozhan.github.com.mycurrencies.data.db.CurrencyDao
import mustafaozhan.github.com.mycurrencies.data.preferences.PreferencesRepository
import mustafaozhan.github.com.mycurrencies.ui.main.settings.SettingsViewModel
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@ObsoleteCoroutinesApi
@RunWith(JUnit4::class)
class SettingsViewModelTest : ViewModelTest() {

    private lateinit var viewModel: SettingsViewModel

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    @RelaxedMockK
    lateinit var preferencesRepository: PreferencesRepository

    @RelaxedMockK
    lateinit var currencyDao: CurrencyDao

    @ExperimentalCoroutinesApi
    @Before
    fun setup() {
        MockKAnnotations.init(this)
        setMainThread()
        viewModel = SettingsViewModel(preferencesRepository, currencyDao)
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
