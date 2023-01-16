package com.oztechan.ccc.android.app.util

import com.oztechan.ccc.billing.model.PurchaseHistory
import com.oztechan.ccc.billing.model.PurchaseMethod
import com.oztechan.ccc.client.model.OldPurchase
import com.oztechan.ccc.client.model.PremiumData
import com.oztechan.ccc.client.model.PremiumType

fun List<PurchaseMethod>.toPremiumDataList(): List<PremiumData> = map {
    PremiumData(it.price, it.description, it.id)
}

fun List<PurchaseHistory>.toOldPurchaseList(): List<OldPurchase> =
    mapNotNull { purchaseHistoryRecord ->
        PremiumType.getById(purchaseHistoryRecord.ids.firstOrNull())?.let {
            OldPurchase(purchaseHistoryRecord.date, it)
        }
    }
