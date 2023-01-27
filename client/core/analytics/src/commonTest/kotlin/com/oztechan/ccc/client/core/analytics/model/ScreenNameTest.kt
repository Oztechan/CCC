package com.oztechan.ccc.client.core.analytics.model

import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals

internal class ScreenNameTest {
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
    fun `screenName Premium return correct name`() {
        val screenName = ScreenName.Premium
        assertEquals(screenName::class.simpleName.toString(), screenName.getScreenName())
    }

    @Test
    fun `screenName Slider return correct name`() {
        val position = Random.nextInt()
        val screenName = ScreenName.Slider(position)
        assertEquals("${screenName::class.simpleName} $position", screenName.getScreenName())
    }
}
