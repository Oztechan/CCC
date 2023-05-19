package com.oztechan.ccc.client.core.analytics.model

sealed class ScreenName {
    object Calculator : ScreenName()
    object SelectCurrency : ScreenName()
    object Currencies : ScreenName()
    object Settings : ScreenName()
    object Watchers : ScreenName()
    object Premium : ScreenName()
    data class Slider(val position: Int) : ScreenName()

    fun getScreenName() = when (this) {
        is Slider -> "${this::class.simpleName} $position"
        else -> this::class.simpleName.toString()
    }
}
