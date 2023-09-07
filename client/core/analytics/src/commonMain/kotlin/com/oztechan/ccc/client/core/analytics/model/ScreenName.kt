package com.oztechan.ccc.client.core.analytics.model

sealed class ScreenName {
    data object Calculator : ScreenName()
    data object SelectCurrency : ScreenName()
    data object Currencies : ScreenName()
    data object Settings : ScreenName()
    data object Watchers : ScreenName()
    data object Premium : ScreenName()
    data class Slider(val position: Int) : ScreenName()

    fun getScreenName() = when (this) {
        is Slider -> "${this::class.simpleName} $position"
        else -> this::class.simpleName.toString()
    }
}
