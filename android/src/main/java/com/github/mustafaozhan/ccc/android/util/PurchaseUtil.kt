package com.github.mustafaozhan.ccc.android.util

import com.github.mustafaozhan.billing.model.PurchaseHistory
import com.github.mustafaozhan.billing.model.PurchaseMethod
import com.github.mustafaozhan.ccc.client.model.OldPurchase
import com.github.mustafaozhan.ccc.client.model.RemoveAdData
import com.github.mustafaozhan.ccc.client.model.RemoveAdType

fun List<PurchaseMethod>.toRemoveAdDataList(): List<RemoveAdData> = map {
    RemoveAdData(it.price, it.description, it.id)
}

fun List<PurchaseHistory>.toOldPurchaseList(): List<OldPurchase> =
    mapNotNull { purchaseHistoryRecord ->
        RemoveAdType.getById(purchaseHistoryRecord.ids.firstOrNull())?.let {
            OldPurchase(purchaseHistoryRecord.date, it)
        }
    }
