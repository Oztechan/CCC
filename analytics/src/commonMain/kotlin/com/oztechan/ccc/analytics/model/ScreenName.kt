package com.oztechan.ccc.analytics.model

sealed class ScreenName {
    object Calculator : ScreenName()
    object SelectCurrency : ScreenName()
    object Currencies : ScreenName()
    object Settings : ScreenName()

    @Suppress("unused") // used in iOS
    object Watchers : ScreenName()
    object AdRemove : ScreenName()
    data class Slider(val position: Int) : ScreenName()

    fun getScreenName() = when (this) {
        is Slider -> "${this::class.simpleName} $position"
        else -> this::class.simpleName.toString()
    }
}
