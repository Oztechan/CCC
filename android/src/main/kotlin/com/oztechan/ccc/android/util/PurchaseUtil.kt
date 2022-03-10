package com.oztechan.ccc.android.util

import com.oztechan.ccc.billing.model.PurchaseHistory
import com.oztechan.ccc.billing.model.PurchaseMethod
import com.oztechan.ccc.client.model.OldPurchase
import com.oztechan.ccc.client.model.RemoveAdData
import com.oztechan.ccc.client.model.RemoveAdType

fun List<PurchaseMethod>.toRemoveAdDataList(): List<RemoveAdData> = map {
    RemoveAdData(it.price, it.description, it.id)
}

fun List<PurchaseHistory>.toOldPurchaseList(): List<OldPurchase> =
    mapNotNull { purchaseHistoryRecord ->
        RemoveAdType.getById(purchaseHistoryRecord.ids.firstOrNull())?.let {
            OldPurchase(purchaseHistoryRecord.date, it)
        }
    }
