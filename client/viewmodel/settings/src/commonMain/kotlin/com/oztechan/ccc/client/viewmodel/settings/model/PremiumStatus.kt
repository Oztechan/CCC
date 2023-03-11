package com.oztechan.ccc.client.viewmodel.settings.model

sealed class PremiumStatus {
    object NeverActivated : PremiumStatus()
    data class Expired(val at: String) : PremiumStatus()
    data class Active(val until: String) : PremiumStatus()
}
