package com.oztechan.ccc.billing

import android.app.Activity
import android.content.Context
import androidx.lifecycle.LifecycleCoroutineScope
import co.touchlab.kermit.Logger
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

@Suppress("unused")
class BillingManagerImpl(private val context: Context) : BillingManager {

    private val _effect = MutableSharedFlow<BillingEffect>()
    override val effect = _effect.asSharedFlow()

    override fun startConnection(
        lifecycleScope: LifecycleCoroutineScope,
        skuList: List<String>
    ) {
        Logger.i { "BillingManagerImpl startConnection" }
    }

    override fun endConnection() {
        Logger.i { "BillingManagerImpl endConnection" }
    }

    override fun launchBillingFlow(activity: Activity, skuId: String) {
        Logger.i { "BillingManagerImpl launchBillingFlow" }
    }

    override fun acknowledgePurchase() {
        Logger.i { "BillingManagerImpl acknowledgePurchase" }
    }
}
