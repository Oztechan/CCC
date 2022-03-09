package com.oztechan.ccc.android.util

import com.github.mustafaozhan.ccc.client.model.OldPurchase
import com.github.mustafaozhan.ccc.client.model.RemoveAdData
import com.github.mustafaozhan.ccc.client.model.RemoveAdType
import com.oztechan.ccc.billing.model.PurchaseHistory
import com.oztechan.ccc.billing.model.PurchaseMethod

fun List<PurchaseMethod>.toRemoveAdDataList(): List<RemoveAdData> = map {
    RemoveAdData(it.price, it.description, it.id)
}

fun List<PurchaseHistory>.toOldPurchaseList(): List<OldPurchase> =
    mapNotNull { purchaseHistoryRecord ->
        RemoveAdType.getById(purchaseHistoryRecord.ids.firstOrNull())?.let {
            OldPurchase(purchaseHistoryRecord.date, it)
        }
    }
