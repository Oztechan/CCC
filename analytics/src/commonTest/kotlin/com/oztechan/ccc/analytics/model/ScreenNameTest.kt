package com.oztechan.ccc.analytics.model

import com.oztechan.ccc.test.BaseTest
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals

internal class ScreenNameTest : BaseTest() {
    @Test
    fun `screenName Calculator return correct name`() {
        val screenName = ScreenName.Calculator
        assertEquals(screenName::class.simpleName.toString(), screenName.getScreenName())
    }

    @Test
    fun `screenName SelectCurrency return correct name`() {
        val screenName = ScreenName.SelectCurrency
        assertEquals(screenName::class.simpleName.toString(), screenName.getScreenName())
    }

    @Test
    fun `screenName Currencies return correct name`() {
        val screenName = ScreenName.Currencies
        assertEquals(screenName::class.simpleName.toString(), screenName.getScreenName())
    }

    @Test
    fun `screenName Settings return correct name`() {
        val screenName = ScreenName.Settings
        assertEquals(screenName::class.simpleName.toString(), screenName.getScreenName())
    }

    @Test
    fun `screenName Watchers return correct name`() {
        val screenName = ScreenName.Watchers
        assertEquals(screenName::class.simpleName.toString(), screenName.getScreenName())
    }

    @Test
    fun `screenName AdRemove return correct name`() {
        val screenName = ScreenName.AdRemove
        assertEquals(screenName::class.simpleName.toString(), screenName.getScreenName())
    }

    @Test
    fun `screenName Slider return correct name`() {
        val position = Random.nextInt()
        val screenName = ScreenName.Slider(position)
        assertEquals("${screenName::class.simpleName} $position", screenName.getScreenName())
    }
}
