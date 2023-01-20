package com.oztechan.ccc.android.app.util

import com.oztechan.ccc.billing.model.ProductDetails
import com.oztechan.ccc.billing.model.PurchaseHistoryRecord
import com.oztechan.ccc.client.model.OldPurchase
import com.oztechan.ccc.client.model.PremiumData
import com.oztechan.ccc.client.model.PremiumType

internal fun List<ProductDetails>.toPremiumDataList(): List<PremiumData> = map {
    PremiumData(it.price, it.description, it.id)
}

internal fun List<PurchaseHistoryRecord>.toOldPurchaseList(): List<OldPurchase> =
    mapNotNull { purchaseHistoryRecord ->
        PremiumType.getById(purchaseHistoryRecord.ids.firstOrNull())?.let {
            OldPurchase(purchaseHistoryRecord.date, it)
        }
    }
