/*
 Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ui.viewmodel

import com.github.mustafaozhan.ui.main.settings.BackEffect
import com.github.mustafaozhan.ui.main.settings.CurrenciesEffect
import com.github.mustafaozhan.ui.main.settings.FeedBackEffect
import com.github.mustafaozhan.ui.main.settings.OnGitHubEffect
import com.github.mustafaozhan.ui.main.settings.SettingsViewModel
import com.github.mustafaozhan.ui.main.settings.SupportUsEffect
import io.mockk.MockKAnnotations
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class SettingsViewModelTest : BaseViewModelTest<SettingsViewModel>() {

    override lateinit var viewModel: SettingsViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = SettingsViewModel()
    }

    // Event
    @Test
    fun `back click`() = with(viewModel) {
        getEvent().onBackClick()
        assertEquals(BackEffect, effect.value)
    }

    @Test
    fun `currencies click`() = with(viewModel) {
        getEvent().onCurrenciesClick()
        assertEquals(CurrenciesEffect, effect.value)
    }

    @Test
    fun `feedback click`() = with(viewModel) {
        getEvent().onFeedBackClick()
        assertEquals(FeedBackEffect, effect.value)
    }

    @Test
    fun `support us click`() = with(viewModel) {
        getEvent().onSupportUsClick()
        assertEquals(SupportUsEffect, effect.value)
    }

    @Test
    fun `on github click`() = with(viewModel) {
        getEvent().onOnGitHubClick()
        assertEquals(OnGitHubEffect, effect.value)
    }
}
