package com.oztechan.ccc.android.core.billing.mapper

import com.android.billingclient.api.Purchase
import com.oztechan.ccc.android.core.billing.model.Purchase as PurchaseModel

internal fun Purchase.toPurchaseModel() = PurchaseModel(
    products,
    purchaseTime,
    purchaseToken
)
