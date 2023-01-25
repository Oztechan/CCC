package com.oztechan.ccc.android.core.billing.mapper

import com.android.billingclient.api.PurchaseHistoryRecord as PurchaseHistoryRecordIAPModel
import com.oztechan.ccc.android.core.billing.model.PurchaseHistoryRecord as toPurchaseHistoryRecordModel

internal fun PurchaseHistoryRecordIAPModel.toPurchaseHistoryRecordModel() = toPurchaseHistoryRecordModel(
    products,
    purchaseTime
)
