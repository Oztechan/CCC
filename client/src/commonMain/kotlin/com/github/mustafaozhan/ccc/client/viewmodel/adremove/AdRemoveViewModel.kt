/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.client.viewmodel.adremove

import com.github.mustafaozhan.ccc.client.base.BaseData
import com.github.mustafaozhan.ccc.client.base.BaseSEEDViewModel
import com.github.mustafaozhan.ccc.client.model.PurchaseHistory
import com.github.mustafaozhan.ccc.client.model.RemoveAdData
import com.github.mustafaozhan.ccc.client.model.RemoveAdType
import com.github.mustafaozhan.ccc.client.util.calculateAdRewardEnd
import com.github.mustafaozhan.ccc.client.util.isRewardExpired
import com.github.mustafaozhan.ccc.client.util.launchIgnored
import com.github.mustafaozhan.ccc.common.settings.SettingsRepository
import com.github.mustafaozhan.ccc.common.util.nowAsLong
import com.github.mustafaozhan.logmob.kermit
import com.github.mustafaozhan.scopemob.whether
import com.github.mustafaozhan.scopemob.whetherNot
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AdRemoveViewModel(
    private val settingsRepository: SettingsRepository
) : BaseSEEDViewModel(), AdRemoveEvent {
    // region SEED
    private val _state = MutableStateFlow(AdRemoveState())
    override val state = _state.asStateFlow()

    private val _effect = MutableSharedFlow<AdRemoveEffect>()
    override val effect = _effect.asSharedFlow()

    override val event = this as AdRemoveEvent

    override val data: BaseData? = null
    // endregion

    init {
        kermit.d { "AdRemoveViewModel init" }
    }

    fun updateAddFreeDate(
        adType: RemoveAdType?,
        startDate: Long = nowAsLong()
    ) = adType?.let {
        clientScope.launch {
            settingsRepository.adFreeEndDate = it.calculateAdRewardEnd(startDate)
            _effect.emit(AdRemoveEffect.AdsRemoved(it))
        }
    }

    fun restorePurchase(purchaseHistoryList: List<PurchaseHistory>) = purchaseHistoryList
        .maxByOrNull {
            it.purchaseType.calculateAdRewardEnd(it.purchaseDate)
        }?.whether { purchaseHistory ->
            RemoveAdType.getBillingIds().any { it == purchaseHistory.purchaseType.data.id }
        }?.whether {
            purchaseDate > settingsRepository.adFreeEndDate
        }?.whetherNot {
            purchaseType.calculateAdRewardEnd(purchaseDate).isRewardExpired()
        }?.apply {
            clientScope.launch { _effect.emit(AdRemoveEffect.AlreadyAdFree) }
            updateAddFreeDate(RemoveAdType.getById(purchaseType.data.id), this.purchaseDate)
        }

    fun showLoadingView(shouldShow: Boolean) {
        _state.update(loading = shouldShow)
    }

    fun addPurchaseMethods(removeAdDataList: List<RemoveAdData>) = removeAdDataList
        .forEach { removeAdData ->
            val tempList = state.value.adRemoveTypes.toMutableList()
            RemoveAdType.getById(removeAdData.id)
                ?.apply {
                    data.cost = removeAdData.cost
                    data.reward = removeAdData.reward
                }?.let {
                    tempList.add(it)
                }
            tempList.sortBy { it.ordinal }
            _state.update(adRemoveTypes = tempList, loading = false)
        }

    override fun onCleared() {
        kermit.d { "AdRemoveViewModel onCleared" }
        super.onCleared()
    }

    override fun onAdRemoveItemClick(type: RemoveAdType) = clientScope.launchIgnored {
        _effect.emit(AdRemoveEffect.LaunchRemoveAdFlow(type))
    }
}
