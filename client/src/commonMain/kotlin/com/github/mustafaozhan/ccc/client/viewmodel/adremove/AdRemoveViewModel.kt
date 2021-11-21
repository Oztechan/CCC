/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.client.viewmodel.adremove

import co.touchlab.kermit.Logger
import com.github.mustafaozhan.ccc.client.base.BaseData
import com.github.mustafaozhan.ccc.client.base.BaseSEEDViewModel
import com.github.mustafaozhan.ccc.client.model.OldPurchase
import com.github.mustafaozhan.ccc.client.model.RemoveAdData
import com.github.mustafaozhan.ccc.client.model.RemoveAdType
import com.github.mustafaozhan.ccc.client.util.calculateAdRewardEnd
import com.github.mustafaozhan.ccc.client.util.isRewardExpired
import com.github.mustafaozhan.ccc.client.util.launchIgnored
import com.github.mustafaozhan.ccc.common.settings.SettingsRepository
import com.github.mustafaozhan.ccc.common.util.nowAsLong
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

    fun updateAddFreeDate(
        adType: RemoveAdType?,
        startDate: Long = nowAsLong(),
        isRestorePurchase: Boolean = false
    ) = adType?.let {
        clientScope.launch {
            settingsRepository.adFreeEndDate = it.calculateAdRewardEnd(startDate)
            _effect.emit(AdRemoveEffect.AdsRemoved(it, isRestorePurchase))
        }
    }

    fun restorePurchase(oldPurchaseList: List<OldPurchase>) = oldPurchaseList
        .maxByOrNull {
            it.type.calculateAdRewardEnd(it.date)
        }?.whether { oldPurchase ->
            RemoveAdType.getPurchaseIds().any { it == oldPurchase.type.data.id }
        }?.whether {
            date > settingsRepository.adFreeEndDate
        }?.whetherNot {
            type.calculateAdRewardEnd(date).isRewardExpired()
        }?.apply {
            updateAddFreeDate(
                adType = RemoveAdType.getById(type.data.id),
                startDate = this.date,
                isRestorePurchase = true
            )
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

    override fun onAdRemoveItemClick(type: RemoveAdType) = clientScope.launchIgnored {
        Logger.d { "AdRemoveViewModel onAdRemoveItemClick ${type.data.reward}" }
        _effect.emit(AdRemoveEffect.LaunchRemoveAdFlow(type))
    }
}
