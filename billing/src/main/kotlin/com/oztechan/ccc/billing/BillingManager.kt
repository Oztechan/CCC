package com.oztechan.ccc.billing

import android.app.Activity
import androidx.lifecycle.LifecycleCoroutineScope
import kotlinx.coroutines.flow.SharedFlow

interface BillingManager {
    val effect: SharedFlow<BillingEffect>

    fun startConnection(
        lifecycleScope: LifecycleCoroutineScope,
        skuList: List<String>
    )

    fun endConnection()

    fun launchBillingFlow(activity: Activity, skuId: String)

    fun acknowledgePurchase()
}
