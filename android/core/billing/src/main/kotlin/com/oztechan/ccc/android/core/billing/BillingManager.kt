package com.oztechan.ccc.android.core.billing

import android.app.Activity
import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.flow.SharedFlow

interface BillingManager {
    val effect: SharedFlow<BillingEffect>

    fun startConnection(
        lifecycleOwner: LifecycleOwner,
        skuList: List<String>
    )

    fun endConnection()

    fun launchBillingFlow(activity: Activity, skuId: String)

    fun acknowledgePurchase()

    fun consumePurchase(token: String)
}
