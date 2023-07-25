package com.oztechan.ccc.client.viewmodel.settings.model

sealed class PremiumStatus {
    data object NeverActivated : PremiumStatus()
    data class Expired(val at: String) : PremiumStatus()
    data class Active(val until: String) : PremiumStatus()
}
