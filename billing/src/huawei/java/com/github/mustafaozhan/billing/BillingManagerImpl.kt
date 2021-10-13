package com.github.mustafaozhan.billing

import android.app.Activity
import android.content.Context
import androidx.lifecycle.LifecycleCoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

@Suppress("unused")
class BillingManagerImpl(private val context: Context) : BillingManager {

    private val _effect = MutableSharedFlow<BillingEffect>()
    override val effect = _effect.asSharedFlow()

    override fun setupBillingClient(
        lifecycleScope: LifecycleCoroutineScope,
        skuList: List<String>
    ) = Unit

    override fun endConnection() = Unit

    override fun launchBillingFlow(activity: Activity, skuId: String) = Unit

    override fun acknowledgePurchase() = Unit
}
