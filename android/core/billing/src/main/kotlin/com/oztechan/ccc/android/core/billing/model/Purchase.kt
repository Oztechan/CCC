package com.oztechan.ccc.android.core.billing.model

data class Purchase(
    var products: List<String>,
    var purchaseTime: Long,
    var purchaseToken: String
)
