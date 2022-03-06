package com.oztechan.ccc.analytics.model

enum class FirebaseEvent(val key: String) {
    BASE_CHANGE("base_change"),
    SHOW_CONVERSION("show_conversion"),
    OFFLINE_SYNC("offline_sync")
}
