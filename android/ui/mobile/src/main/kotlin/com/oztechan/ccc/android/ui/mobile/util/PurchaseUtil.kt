package com.oztechan.ccc.android.ui.mobile.util

import com.oztechan.ccc.android.core.billing.model.ProductDetails
import com.oztechan.ccc.android.core.billing.model.PurchaseHistoryRecord
import com.oztechan.ccc.client.core.shared.model.OldPurchase
import com.oztechan.ccc.client.core.shared.model.PremiumData
import com.oztechan.ccc.client.core.shared.model.PremiumType

internal fun List<ProductDetails>.toPremiumDataList(): List<PremiumData> = map {
    PremiumData(it.price, it.description, it.id)
}

internal fun List<PurchaseHistoryRecord>.toOldPurchaseList(): List<OldPurchase> =
    mapNotNull { purchaseHistoryRecord ->
        PremiumType.getById(purchaseHistoryRecord.ids.firstOrNull())?.let {
            OldPurchase(purchaseHistoryRecord.date, it)
        }
    }
