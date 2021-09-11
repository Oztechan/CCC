package com.github.mustafaozhan.ccc.android.billing

import android.app.Activity
import android.content.Context
import androidx.lifecycle.LifecycleCoroutineScope
import com.android.billingclient.api.AcknowledgePurchaseParams
import com.android.billingclient.api.AcknowledgePurchaseResponseListener
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchaseHistoryRecord
import com.android.billingclient.api.PurchaseHistoryResponseListener
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.SkuDetails
import com.android.billingclient.api.SkuDetailsParams
import com.android.billingclient.api.SkuDetailsResponseListener
import com.github.mustafaozhan.ccc.client.model.PurchaseHistory
import com.github.mustafaozhan.ccc.client.model.RemoveAdData
import com.github.mustafaozhan.ccc.client.model.RemoveAdType
import com.github.mustafaozhan.logmob.kermit
import com.github.mustafaozhan.scopemob.mapTo
import com.github.mustafaozhan.scopemob.whether
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

internal class BillingManager(
    private val context: Context
) : AcknowledgePurchaseResponseListener,
    PurchasesUpdatedListener,
    BillingClientStateListener,
    PurchaseHistoryResponseListener,
    SkuDetailsResponseListener {

    private lateinit var billingClient: BillingClient
    private lateinit var scope: CoroutineScope

    private lateinit var skuDetails: List<SkuDetails>
    private var acknowledgePurchaseParams: AcknowledgePurchaseParams? = null

    private val _effect = MutableSharedFlow<BillingEffect>()
    val effect = _effect.asSharedFlow()

    internal fun setupBillingClient(lifecycleScope: LifecycleCoroutineScope) {
        this.scope = lifecycleScope

        scope.launch {
            _effect.emit(BillingEffect.ShowLoading)
        }

        billingClient = BillingClient
            .newBuilder(context.applicationContext)
            .enablePendingPurchases()
            .setListener(this)
            .build()

        billingClient.startConnection(this)
    }

    internal fun endConnection() {
        billingClient.endConnection()
    }

    internal fun launchBillingFlow(activity: Activity, skuId: String) {
        skuDetails
            .firstOrNull { it.sku == skuId }
            ?.let {
                val billingFlowParams = BillingFlowParams
                    .newBuilder()
                    .setSkuDetails(it)
                    .build()
                billingClient.launchBillingFlow(activity, billingFlowParams)
            }
    }

    internal fun acknowledgePurchase() {
        acknowledgePurchaseParams?.let {
            billingClient.acknowledgePurchase(it, this)
        } ?: run {
            scope.launch {
                _effect.emit(BillingEffect.RestartActivity)
            }
        }
    }

    override fun onAcknowledgePurchaseResponse(billingResult: BillingResult) {
        kermit.d { "AdRemoveBottomSheet onAcknowledgePurchaseResponse" }
        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
            scope.launch {
                _effect.emit(BillingEffect.RestartActivity)
            }
        }
    }

    override fun onPurchasesUpdated(
        billingResult: BillingResult,
        purchaseList: MutableList<Purchase>?
    ) {
        kermit.d { "AdRemoveBottomSheet onPurchasesUpdated ${billingResult.responseCode}" }

        purchaseList?.firstOrNull()
            ?.also {
                acknowledgePurchaseParams = AcknowledgePurchaseParams.newBuilder()
                    .setPurchaseToken(it.purchaseToken)
                    .build()
            }?.mapTo { RemoveAdType.getBySku(skus.firstOrNull()) }
            ?.let {
                scope.launch {
                    _effect.emit(BillingEffect.UpdateAddFreeDate(it))
                }
            }
    }

    override fun onBillingSetupFinished(billingResult: BillingResult) {
        kermit.d { "AdRemoveBottomSheet onBillingSetupFinished ${billingResult.responseCode}" }

        scope.launch {
            _effect.emit(BillingEffect.HideLoading)
        }

        billingClient.queryPurchaseHistoryAsync(BillingClient.SkuType.INAPP, this)

        billingClient.whether(
            { isReady },
            { billingResult.responseCode == BillingClient.BillingResponseCode.OK }
        )?.apply {
            val skuDetailsParams = SkuDetailsParams.newBuilder()
                .setSkusList(RemoveAdType.getSkuList())
                .setType(BillingClient.SkuType.INAPP)
                .build()
            querySkuDetailsAsync(skuDetailsParams, this@BillingManager)
        }
    }

    override fun onBillingServiceDisconnected() {
        kermit.d { "AdRemoveBottomSheet onBillingServiceDisconnected" }
        scope.launch {
            _effect.emit(BillingEffect.HideLoading)
        }
    }

    override fun onSkuDetailsResponse(
        billingResult: BillingResult,
        skuDetailsList: MutableList<SkuDetails>?
    ) {
        kermit.d { "AdRemoveBottomSheet onSkuDetailsResponse ${billingResult.responseCode}" }

        scope.launch {
            skuDetailsList?.whether {
                billingResult.responseCode == BillingClient.BillingResponseCode.OK
            }?.let { detailsList ->
                skuDetails = detailsList

                _effect.emit(BillingEffect.AddInAppBillingMethods(
                    detailsList.map {
                        RemoveAdData(it.price, it.description, it.sku)
                    }
                ))
            } ?: run {
                _effect.emit(BillingEffect.HideLoading)
            }
        }
    }

    override fun onPurchaseHistoryResponse(
        billingResult: BillingResult,
        purchaseHistoryList: MutableList<PurchaseHistoryRecord>?
    ) {
        kermit.d { "AdRemoveBottomSheet onPurchaseHistoryResponse ${billingResult.responseCode}" }

        purchaseHistoryList?.mapNotNull { historyRecord ->
            RemoveAdType.getBySku(historyRecord.skus.firstOrNull())?.let {
                PurchaseHistory(historyRecord.purchaseTime, it)
            }
        }?.let {
            scope.launch {
                _effect.emit(BillingEffect.RestorePurchase(it))
            }
        }
    }
}
