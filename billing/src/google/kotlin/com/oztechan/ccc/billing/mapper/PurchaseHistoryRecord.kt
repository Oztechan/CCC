package com.oztechan.ccc.billing.mapper

import com.android.billingclient.api.PurchaseHistoryRecord as PurchaseHistoryRecordIAPModel
import com.oztechan.ccc.billing.model.PurchaseHistoryRecord as toPurchaseHistoryRecordModel

internal fun PurchaseHistoryRecordIAPModel.toPurchaseHistoryRecordModel() = toPurchaseHistoryRecordModel(
    products,
    purchaseTime
)
