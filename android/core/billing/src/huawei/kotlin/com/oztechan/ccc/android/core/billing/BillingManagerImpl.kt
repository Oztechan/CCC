package com.oztechan.ccc.android.core.billing

import android.app.Activity
import android.content.Context
import androidx.lifecycle.LifecycleOwner
import co.touchlab.kermit.Logger
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

@Suppress("unused")
internal class BillingManagerImpl(private val context: Context) : BillingManager {

    private val _effect = MutableSharedFlow<BillingEffect>()
    override val effect = _effect.asSharedFlow()

    override fun startConnection(
        lifecycleOwner: LifecycleOwner,
        skuList: List<String>
    ) {
        Logger.v { "BillingManagerImpl startConnection" }
    }

    override fun endConnection() {
        Logger.v { "BillingManagerImpl endConnection" }
    }

    override fun launchBillingFlow(activity: Activity, skuId: String) {
        Logger.v { "BillingManagerImpl launchBillingFlow" }
    }

    override fun acknowledgePurchase() {
        Logger.v { "BillingManagerImpl acknowledgePurchase" }
    }

    override fun consumePurchase(token: String) {
        Logger.v { "BillingManagerImpl consumePurchase" }
    }
}
