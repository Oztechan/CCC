package com.oztechan.ccc.client.core.analytics.model

sealed class Event(val key: String) {
    data class BaseChange(val base: Param.Base) : Event("base_change")
    data class ShowConversion(val base: Param.Base) : Event("show_conversion")
    data object OfflineSync : Event("offline_sync")
    data object CopyClipboard : Event("copy_clipboard")
    data object PasteFromClipboard : Event("paste_from_clipboard")

    fun getParams(): Map<String, String>? = when (this) {
        is ShowConversion -> mapOf(base.key to base.value)
        is BaseChange -> mapOf(base.key to base.value)
        OfflineSync,
        PasteFromClipboard,
        CopyClipboard -> null
    }
}
